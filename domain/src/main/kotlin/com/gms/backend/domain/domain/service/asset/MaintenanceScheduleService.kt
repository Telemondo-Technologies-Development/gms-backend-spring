package com.gms.backend.domain.domain.service.asset

import com.gms.backend.domain.application.rest.asset.MaintenanceScheduleController
import java.util.UUID

interface MaintenanceScheduleService {
    fun updateMaintenanceSchedule(
        assetId: UUID,
        body: MaintenanceScheduleController.MaintenanceSchedulePutDTO
    ): MaintenanceScheduleController.MaintenanceScheduleTableDTO
}
