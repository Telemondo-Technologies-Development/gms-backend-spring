package com.gms.backend.domain.application.mapper.asset

import com.gms.backend.domain.application.rest.asset.AssetCategoryController
import com.gms.backend.domain.domain.model.asset.AssetCategory
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface AssetCategoryMapper {
    fun assetCategoryDTOToAssetCategory(dto: AssetCategoryController.AssetCategoryPostDTO): AssetCategory
    fun assetCategoryPutDTOToAssetCategory(
        dto: AssetCategoryController.AssetCategoryPutDTO,
        @MappingTarget assetCategory: AssetCategory
    ): AssetCategory

    fun assetCategoryToDTO(assetCategory: AssetCategory): AssetCategoryController.AssetCategoryTableDTO
    fun assetCategoriesToDTO(assetCategories: List<AssetCategory>): List<AssetCategoryController.AssetCategoryTableDTO>
}
