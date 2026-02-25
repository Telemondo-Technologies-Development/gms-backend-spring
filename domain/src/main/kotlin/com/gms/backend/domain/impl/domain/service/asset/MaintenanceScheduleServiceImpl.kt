package com.gms.backend.domain.impl.domain.service.asset

import com.gms.backend.domain.application.mapper.asset.MaintenanceScheduleMapper
import com.gms.backend.domain.application.rest.asset.MaintenanceScheduleController
import com.gms.backend.domain.domain.repository.asset.AssetRepository
import com.gms.backend.domain.domain.repository.asset.MaintenanceScheduleRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.asset.MaintenanceScheduleService
import com.gms.backend.domain.infra.quartz.asset.MaintenanceSchedulerService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@PreAuthorize("denyAll()")
class MaintenanceScheduleServiceImpl(
    private val scheduleRepository: MaintenanceScheduleRepository,
    private val assetRepository: AssetRepository,
    private val actorRepository: ActorRepository,
    private val scheduleMapper: MaintenanceScheduleMapper,
    private val maintenanceSchedulerService: MaintenanceSchedulerService
) : MaintenanceScheduleService {

    @Transactional
    @PreAuthorize("hasAuthority('maintenanceSchedule_create')")
    override fun createSchedule(body: MaintenanceScheduleController.SchedulePostDTO): MaintenanceScheduleController.ScheduleTableDTO {
        val schedule = scheduleMapper.schedulePostDTOToSchedule(body).apply {
            asset = assetRepository.getReferenceById(body.assetId)
            createdBy = actorRepository.getReferenceById(body.createdById)
            updatedBy = actorRepository.getReferenceById(body.createdById)
        }
        val savedSchedule = scheduleMapper.scheduleToDTO(scheduleRepository.saveAndFlush(schedule))
        maintenanceSchedulerService.processSchedules()
        return savedSchedule
    }

    @Transactional
    @PreAuthorize("hasAuthority('maintenanceSchedule_update')")
    override fun updateSchedule(id: UUID, body: MaintenanceScheduleController.SchedulePutDTO): MaintenanceScheduleController.ScheduleTableDTO {
        val schedule = scheduleRepository.findById(id).orElseThrow { NoSuchElementException("Schedule not found") }
        scheduleMapper.schedulePutDTOToSchedule(body, schedule)
        schedule.updatedBy = actorRepository.getReferenceById(body.updatedById)
        val savedSchedule = scheduleMapper.scheduleToDTO(scheduleRepository.saveAndFlush(schedule))
        maintenanceSchedulerService.cancelFutureWorkers(id)
        maintenanceSchedulerService.processSchedules()
        return savedSchedule
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('maintenanceSchedule_read')")
    override fun getSchedules(pageable: Pageable): Page<MaintenanceScheduleController.ScheduleTableDTO> =
        scheduleRepository.findAll(pageable).map { schedule ->
            scheduleMapper.scheduleToDTO(schedule)
        }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('maintenanceSchedule_read')")
    override fun getScheduleById(id: UUID) = scheduleMapper.scheduleToDTO(scheduleRepository.findById(id).get())

    @Transactional
    @PreAuthorize("hasAuthority('maintenanceSchedule_update')")
    override fun deleteSchedule(id: UUID){
        val schedule = scheduleRepository.findById(id).orElseThrow {
            NoSuchElementException("Maintenance Schedule not found with ID: $id")
        }
        maintenanceSchedulerService.cancelFutureWorkers(id)
        scheduleRepository.delete(schedule)
    }
}
