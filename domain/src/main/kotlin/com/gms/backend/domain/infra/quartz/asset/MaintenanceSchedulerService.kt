package com.gms.backend.domain.infra.quartz.asset

import com.gms.backend.domain.domain.model.asset.AssetMaintenance
import com.gms.backend.domain.domain.model.asset.MaintenanceSchedule
import com.gms.backend.domain.domain.model.user.Actor
import com.gms.backend.domain.domain.repository.asset.AssetMaintenanceRepository
import com.gms.backend.domain.domain.repository.asset.MaintenanceScheduleRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit

@Service
@PreAuthorize("denyAll()")
class MaintenanceSchedulerService(
    private val scheduleRepository: MaintenanceScheduleRepository,
    private val maintenanceRepository: AssetMaintenanceRepository,
    private val actorRepository: ActorRepository
) {

    @Transactional
    @PreAuthorize("hasAuthority('maintenanceSchedule_update')")
    fun processSchedules() {
        val now = Instant.now().truncatedTo(ChronoUnit.MINUTES) // Truncate now too
        val systemActor = actorRepository.findByType(Actor.ActorType.SYSTEM)
            .orElseThrow { IllegalStateException("System Actor not found in database") }
        val schedules = scheduleRepository.findAllByIsActiveTrue()

        schedules.forEach { schedule ->
            var nextTargetDate = findNextDateAfterLastMaintenance(schedule)

            // Generate records only if we are within the lead-time window
            while (true) {
                val generationThreshold = nextTargetDate.minus(Duration.ofHours(schedule.leadTimeHours.toLong()))
                if (now.isBefore(generationThreshold)) break

                // If current time is within the lead-time window, and record has not existed, create asset maintenance
                val alreadyExists = maintenanceRepository.existsByMaintenanceScheduleAndMaintenanceDateBetween(
                    schedule,
                    nextTargetDate.minusSeconds(30),
                    nextTargetDate.plusSeconds(30)
                )

                if (!alreadyExists) {
                    println("DEBUG: CREATE MAINTENANCE for Date: $nextTargetDate")
                    val maintenance = AssetMaintenance().apply {
                        this.asset = schedule.asset
                        this.maintenanceSchedule = schedule
                        this.maintenanceDate = nextTargetDate
                        this.dueDate = nextTargetDate.plus(Duration.ofHours(schedule.timeToCompleteHours.toLong()))
                        this.status = AssetMaintenance.AssetMaintenanceStatus.PENDING
                        this.createdBy = systemActor
                        this.updatedBy = systemActor
                    }
                    maintenanceRepository.save(maintenance)
                }
                nextTargetDate = calculateNextOccurrence(nextTargetDate, schedule)
                if (schedule.intervalValue <= 0) break
            }
        }
    }

    // Finds the starting point (either the original startdate or the latest asset mainteanance's date)
    private fun findNextDateAfterLastMaintenance(schedule: MaintenanceSchedule): Instant {
        val lastMaintenance = maintenanceRepository.findFirstByMaintenanceScheduleOrderByMaintenanceDateDesc(schedule)
        if (lastMaintenance == null) return schedule.startDate.truncatedTo(ChronoUnit.MINUTES)
        return calculateNextOccurrence(lastMaintenance.maintenanceDate, schedule)
    }

    // Projects the next occurence
    private fun calculateNextOccurrence(current: Instant, schedule: MaintenanceSchedule): Instant {
        val zonedDateTime = current.atZone(ZoneId.of("UTC"))
        val value = schedule.intervalValue.toLong()

        val next = when (schedule.intervalUnit) {
            MaintenanceSchedule.IntervalUnit.MINUTE -> zonedDateTime.plusMinutes(value)
            MaintenanceSchedule.IntervalUnit.HOUR   -> zonedDateTime.plusHours(value)
            MaintenanceSchedule.IntervalUnit.DAY    -> zonedDateTime.plusDays(value)
            MaintenanceSchedule.IntervalUnit.WEEK   -> zonedDateTime.plusWeeks(value)
            MaintenanceSchedule.IntervalUnit.MONTH  -> zonedDateTime.plusMonths(value)
            MaintenanceSchedule.IntervalUnit.YEAR   -> zonedDateTime.plusYears(value)
        }
        return next.toInstant().truncatedTo(ChronoUnit.MINUTES)
    }

    // update status from pending to overdue when it's already over the due date
    @Transactional
    @PreAuthorize("hasAuthority('maintenanceSchedule_update')")
    fun updateOverdueStatuses() {
        val now = Instant.now()
        maintenanceRepository.findAllByStatus(AssetMaintenance.AssetMaintenanceStatus.PENDING)
            .filter { it.dueDate.isBefore(now) }
            .forEach { maintenance ->
                maintenance.status = AssetMaintenance.AssetMaintenanceStatus.OVERDUE
                maintenanceRepository.save(maintenance)
            }
    }
}