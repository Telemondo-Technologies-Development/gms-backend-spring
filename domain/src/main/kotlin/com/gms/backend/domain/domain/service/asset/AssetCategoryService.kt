package com.gms.backend.domain.domain.service.asset

import com.gms.backend.domain.application.rest.asset.AssetCategoryController
import java.util.UUID

interface AssetCategoryService {
    fun createAssetCategory(body: AssetCategoryController.AssetCategoryPostDTO): AssetCategoryController.AssetCategoryTableDTO
    fun updateAssetCategory(id: UUID, body: AssetCategoryController.AssetCategoryPutDTO): AssetCategoryController.AssetCategoryTableDTO
    fun getAssetCategories(): List<AssetCategoryController.AssetCategoryTableDTO>
    fun getAssetCategoryById(id: UUID): AssetCategoryController.AssetCategoryTableDTO
    fun deleteAssetCategory(id: UUID)
}
