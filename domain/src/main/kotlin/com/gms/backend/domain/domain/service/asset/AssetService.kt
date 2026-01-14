package com.gms.backend.domain.domain.service.asset

import com.gms.backend.domain.application.rest.asset.AssetController
import java.util.UUID

interface AssetService {
    fun createAsset(body: AssetController.AssetPostDTO): AssetController.AssetTableDTO
    fun updateAsset(id: UUID, body: AssetController.AssetPutDTO): AssetController.AssetTableDTO
    fun getAssets(): List<AssetController.AssetTableDTO>
    fun getAssetById(id: UUID): AssetController.AssetTableDTO
    fun deleteAsset(id: UUID)
}
