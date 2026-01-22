package com.gms.backend.domain.application.mapper.asset

import com.gms.backend.domain.application.rest.asset.AssetMaintenanceController
import com.gms.backend.domain.domain.model.asset.AssetMaintenance
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface AssetMaintenanceMapper {
    fun assetMaintenanceToDTO(entity: AssetMaintenance): AssetMaintenanceController.AssetMaintenanceTableDTO
    fun assetMaintenancesToDTOs(entities: List<AssetMaintenance>): List<AssetMaintenanceController.AssetMaintenanceTableDTO>
}