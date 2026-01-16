package com.gms.backend.domain.application.mapper.asset

import com.gms.backend.domain.application.rest.asset.MaintenanceScheduleController
import com.gms.backend.domain.domain.model.asset.MaintenanceSchedule
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface MaintenanceScheduleMapper {
    fun maintenanceSchedulePutDTOToMaintenanceSchedule(
        dto: MaintenanceScheduleController.MaintenanceSchedulePutDTO,
        @MappingTarget ms: MaintenanceSchedule
    ): MaintenanceSchedule

    fun maintenanceScheduleToDTO(ms: MaintenanceSchedule): MaintenanceScheduleController.MaintenanceScheduleTableDTO
}
