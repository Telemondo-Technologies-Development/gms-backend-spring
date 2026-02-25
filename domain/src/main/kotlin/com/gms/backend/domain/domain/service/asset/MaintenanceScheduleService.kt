package com.gms.backend.domain.domain.service.asset

import com.gms.backend.domain.application.rest.asset.MaintenanceScheduleController
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface MaintenanceScheduleService {
    fun createSchedule(body: MaintenanceScheduleController.SchedulePostDTO): MaintenanceScheduleController.ScheduleTableDTO
    fun updateSchedule(id: UUID, body: MaintenanceScheduleController.SchedulePutDTO): MaintenanceScheduleController.ScheduleTableDTO
    fun getSchedules(pageable: Pageable): Page<MaintenanceScheduleController.ScheduleTableDTO>
    fun getScheduleById(id: UUID): MaintenanceScheduleController.ScheduleTableDTO
    fun deleteSchedule(id: UUID)
}
