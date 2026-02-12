package com.gms.backend.domain.application.mapper.asset

import com.gms.backend.domain.application.rest.asset.AssetController
import com.gms.backend.domain.domain.model.asset.Asset
import com.gms.backend.domain.domain.model.storage.ObjectStorage
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy
import java.util.UUID

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface AssetMapper {
    fun assetPostDTOToAsset(dto: AssetController.AssetPostDTO): Asset
    fun assetPutDTOToAsset(dto: AssetController.AssetPutDTO, @MappingTarget asset: Asset): Asset
    @Mapping(target = "objectIds", source = "assetObjects")
    fun assetToDTO(asset: Asset): AssetController.AssetTableDTO
    fun mapObjectsToIds(objects: Set<ObjectStorage>?): List<UUID> {
        return objects?.map { it.id } ?: emptyList()
    }
}
