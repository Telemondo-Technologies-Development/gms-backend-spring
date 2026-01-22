package com.gms.backend.domain.application.mapper.asset

import com.gms.backend.domain.application.rest.asset.AssetController
import com.gms.backend.domain.domain.model.asset.Asset
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface AssetMapper {
    fun assetPostDTOToAsset(dto: AssetController.AssetPostDTO): Asset
    fun assetPutDTOToAsset(dto: AssetController.AssetPutDTO, @MappingTarget asset: Asset): Asset
    fun assetToDTO(asset: Asset): AssetController.AssetTableDTO
}
