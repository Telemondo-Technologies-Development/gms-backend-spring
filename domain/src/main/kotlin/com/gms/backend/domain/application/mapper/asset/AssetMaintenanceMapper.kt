package com.gms.backend.domain.application.mapper.asset

import com.gms.backend.domain.application.rest.asset.AssetMaintenanceController
import com.gms.backend.domain.domain.model.asset.AssetMaintenance
import com.gms.backend.domain.domain.model.storage.ObjectStorage
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy
import java.util.UUID

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface AssetMaintenanceMapper {
    @Mapping(target = "objectIds", source = "assetMaintenanceObjects")
    fun assetMaintenanceToDTO(entity: AssetMaintenance): AssetMaintenanceController.AssetMaintenanceTableDTO
    @Mapping(target = "objectIds", source = "assetMaintenanceObjects")
    fun assetMaintenancesToDTOs(entities: List<AssetMaintenance>): List<AssetMaintenanceController.AssetMaintenanceTableDTO>
    fun mapObjectsToIds(objects: Set<ObjectStorage>?): List<UUID> {
        return objects?.map { it.id } ?: emptyList()
    }
}