package com.gms.backend.domain.infra.quartz.asset

import com.gms.backend.domain.domain.model.asset.AssetMaintenance
import com.gms.backend.domain.domain.model.user.Actor
import com.gms.backend.domain.domain.repository.asset.AssetMaintenanceRepository
import com.gms.backend.domain.domain.repository.asset.MaintenanceScheduleRepository
import com.gms.backend.domain.domain.repository.asset.ScheduleWithLatestMaintenanceDTO
import com.gms.backend.domain.domain.repository.user.ActorRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*

@Service
@PreAuthorize("denyAll()")
class MaintenanceSchedulerService(
    private val scheduleRepository: MaintenanceScheduleRepository,
    private val maintenanceRepository: AssetMaintenanceRepository,
    private val actorRepository: ActorRepository,
    private val scheduler: org.quartz.Scheduler
) {
    // logic for the main job (runs every 12 AM)
    @Transactional
    @PreAuthorize("hasAuthority('maintenanceSchedule_update')")
    fun processSchedules() {
        val now = Instant.now()
        val planningHorizon = now.plus(Duration.ofHours(24))
        val scheds = scheduleRepository.findAllWithLatestMaintenance()// a list of all active schedules and their latest asset maintenance

        scheds.forEach { sched -> // loops for each maintenance schedule
            // if this is the first asset maintenance record fir this schedule
            var nextTargetDate = if (sched.latestMaintenanceDate == null) {
                // if advanced settings exist:
                if (sched.dayOfWeek != null && sched.weekRank != null) {
                    calculateAdvancedOccurrence(sched.startDate, sched, true)
                } else {
                    // if advanced settings don't exist:
                    sched.startDate.truncatedTo(ChronoUnit.MINUTES)
                }
            } else { // if this is not the first asset maintenance record
                if (sched.dayOfWeek != null && sched.weekRank != null) {
                    calculateAdvancedOccurrence(sched.latestMaintenanceDate, sched, false)
                } else {
                    calculateNextOccurrence(sched.latestMaintenanceDate, sched.intervalValue, sched.intervalUnit)
                }
            }

            //check
            if (nextTargetDate.isBefore(sched.startDate.truncatedTo(ChronoUnit.MINUTES))) {
                nextTargetDate = if (sched.dayOfWeek != null && sched.weekRank != null) {
                    calculateAdvancedOccurrence(nextTargetDate, sched)
                } else {
                    calculateNextOccurrence(nextTargetDate, sched.intervalValue, sched.intervalUnit)
                }
            }

            // Fill the calendar for the next 24 hours
            while (nextTargetDate.isBefore(planningHorizon)) {

                // Define creation and overdue
                val creationTime = nextTargetDate.minus(Duration.ofHours(sched.leadTimeHours.toLong()))
                val dueDate = nextTargetDate.plus(Duration.ofHours(sched.timeToCompleteHours.toLong()))
                val overdueRunTime = dueDate.plus(Duration.ofMinutes(1)) // adds 1 minute for status to be updated to overdue (this is to prevent errors)

                // Logic for "creation" schedule (worker that creates asset maintenance)
                // If the next target date is right now or in the past, spawn a worker that triggers now
                if (nextTargetDate <= now) {
                    val exists = maintenanceRepository.existsByMaintenanceScheduleIdAndMaintenanceDate(
                        sched.scheduleId,
                        nextTargetDate
                    )

                    if (!exists) spawnWorker(sched.scheduleId, now, nextTargetDate, "CREATE")
                } else {
                    // if the next target date is in the future, then spawn the worker that triggerts at nextTargetDate
                    val runAt = if (creationTime > now) creationTime else now
                    spawnWorker(sched.scheduleId, runAt, nextTargetDate, "CREATE")
                }

                // Logic for "overdue" schedule (worker that updates asset maintenance's status to overdue)
                if (overdueRunTime <= now) {
                    spawnWorker(sched.scheduleId, now, nextTargetDate, "OVERDUE")
                } else {
                    spawnWorker(sched.scheduleId, overdueRunTime, nextTargetDate, "OVERDUE")
                }

                // Move to the next occurrence and loop again
                nextTargetDate = if (sched.dayOfWeek != null && sched.weekRank != null) {
                    calculateAdvancedOccurrence(nextTargetDate, sched) // if it has advanced settins
                } else {
                    calculateNextOccurrence(nextTargetDate, sched.intervalValue, sched.intervalUnit) // if it doesn't have advanced settigngs
                }

                if (sched.intervalValue <= 0) break
            }
        }
    }

    // executes worker task
    @Transactional
    @PreAuthorize("hasAuthority('maintenanceSchedule_update')")
    fun executeWorkerTask(scheduleId: UUID, targetDate: Instant, action: String) {
        val systemActor = actorRepository.findByType(Actor.ActorType.SYSTEM).get()

        when (action) {
            "CREATE" -> {
                val schedule = scheduleRepository.findById(scheduleId).orElse(null) ?: return
                if (!schedule.isActive) return
                if (!maintenanceRepository.existsByMaintenanceScheduleIdAndMaintenanceDate(scheduleId, targetDate)) {
                    val maintenance = AssetMaintenance().apply {
                        this.asset = schedule.asset
                        this.maintenanceSchedule = schedule
                        this.maintenanceDate = targetDate
                        this.dueDate = targetDate.plus(Duration.ofHours(schedule.timeToCompleteHours.toLong()))
                        this.status = AssetMaintenance.AssetMaintenanceStatus.PENDING
                        this.createdBy = systemActor
                        this.updatedBy = systemActor
                    }
                    maintenanceRepository.save(maintenance)
                }
            }
            "OVERDUE" -> {
                maintenanceRepository.updateStatusToOverdue(
                    scheduleId = scheduleId,
                    targetDate = targetDate,
                    systemActor = systemActor,
                    now = Instant.now()
                )
            }
        }
    }

    @Transactional
    @PreAuthorize("hasAuthority('maintenanceSchedule_update')")
    // to cancel future workers, if maintenance schedule is updated or deleted
    fun cancelFutureWorkers(scheduleId: UUID) {
        val matcher = org.quartz.impl.matchers.GroupMatcher.jobGroupEquals("maintenance-workers")
        val jobKeys = scheduler.getJobKeys(matcher)

        val keysToDelete = jobKeys.filter { it.name.contains(scheduleId.toString()) }

        if (keysToDelete.isNotEmpty()) {
            scheduler.deleteJobs(keysToDelete.toList())
        }
    }

    // spawns worker
    private fun spawnWorker(scheduleId: UUID, runAt: Instant, targetDate: Instant, action: String) {
        val jobKey = org.quartz.JobKey("$action-$scheduleId-${targetDate.toEpochMilli()}", "maintenance-workers")

        if (scheduler.checkExists(jobKey)) {
            return
        }

        val jobDetail = org.quartz.JobBuilder.newJob(MaintenanceWorkerJob::class.java)
            .withIdentity("$action-$scheduleId-${targetDate.toEpochMilli()}", "maintenance-workers")
            .usingJobData("scheduleId", scheduleId.toString())
            .usingJobData("targetDate", targetDate.toEpochMilli())
            .usingJobData("action", action)
            .build()

        val trigger = org.quartz.TriggerBuilder.newTrigger()
            .startAt(java.util.Date.from(runAt))
            .build()

        scheduler.scheduleJob(jobDetail, trigger)
    }

    // calculate the next nextTargetDate
    private fun calculateNextOccurrence(
        current: Instant,
        intervalValue: Int,
        intervalUnit: java.time.temporal.ChronoUnit
    ): Instant {
        val zonedDateTime = current.atZone(ZoneId.of("UTC"))
        val next = zonedDateTime.plus(intervalValue.toLong(), intervalUnit)
        return next.toInstant().truncatedTo(ChronoUnit.MINUTES)
    }

    // calculate the next nextTargetDate for schedules that have advanced settings
    private fun calculateAdvancedOccurrence(
        current: Instant,
        sched: ScheduleWithLatestMaintenanceDTO,
        isFirstRun: Boolean = false
    ): Instant {
        val zonedDateTime = current.atZone(ZoneId.of("UTC"))

        // if it is the first asset maintenance
        var nextBase = if (isFirstRun) {
            zonedDateTime
        } else {
            zonedDateTime.plus(sched.intervalValue.toLong(), sched.intervalUnit)
        }

        sched.monthOfYear?.let { nextBase = nextBase.withMonth(it) }

        val dayToFind = java.time.DayOfWeek.of(sched.dayOfWeek!!)
        val adjuster = if (sched.weekRank == 5) {
            java.time.temporal.TemporalAdjusters.lastInMonth(dayToFind)
        } else {
            java.time.temporal.TemporalAdjusters.dayOfWeekInMonth(sched.weekRank!!, dayToFind)
        }

        val occurrence = nextBase.with(adjuster).toInstant().truncatedTo(ChronoUnit.MINUTES)

        // check
        return if (isFirstRun && occurrence.isBefore(current)) {
            calculateAdvancedOccurrence(occurrence, sched, false)
        } else {
            occurrence
        }
    }
}