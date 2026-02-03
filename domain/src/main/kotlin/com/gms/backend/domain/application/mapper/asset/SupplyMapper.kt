package com.gms.backend.domain.application.mapper.asset

import com.gms.backend.domain.application.rest.asset.SupplyController
import com.gms.backend.domain.domain.model.asset.Supply
import com.gms.backend.domain.domain.model.storage.ObjectStorage
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy
import java.util.UUID

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface SupplyMapper {
    fun supplyPostDTOToSupply(dto: SupplyController.SupplyPostDTO): Supply
    fun supplyPutDTOToSupply(
        dto: SupplyController.SupplyPutDTO,
        @MappingTarget supply: Supply
    ): Supply
    @Mapping(target = "objectIds", source = "suppliesObjects")
    fun supplyToDTO(supply: Supply): SupplyController.SupplyTableDTO
    fun mapObjectsToIds(objects: Set<ObjectStorage>?): List<UUID> {
        return objects?.map { it.id } ?: emptyList()
    }
}