package com.gms.backend.domain.domain.service.asset

import com.gms.backend.domain.application.rest.asset.AssetMaintenanceController
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface AssetMaintenanceService {
    fun getMaintenanceLogById(id: UUID): AssetMaintenanceController.AssetMaintenanceTableDTO
    fun getMaintenanceLogs(pageable: Pageable): Page<AssetMaintenanceController.AssetMaintenanceTableDTO>
    fun updateMaintenanceStatus(id: UUID, request: AssetMaintenanceController.AssetMaintenancePatchDTO): AssetMaintenanceController.AssetMaintenanceTableDTO
}
