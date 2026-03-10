package com.gms.backend.domain.application.mapper.asset

import com.gms.backend.domain.application.rest.asset.AssetController
import com.gms.backend.domain.domain.model.asset.Asset
import com.gms.backend.domain.domain.model.storage.ObjectStorage
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy
import java.util.*

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface AssetMapper {
    fun assetPostDTOToAsset(dto: AssetController.AssetPostDTO): Asset
    fun assetPutDTOToAsset(dto: AssetController.AssetPutDTO, @MappingTarget asset: Asset): Asset
    fun assetToDTO(asset: Asset): AssetController.AssetSummaryDTO
}
