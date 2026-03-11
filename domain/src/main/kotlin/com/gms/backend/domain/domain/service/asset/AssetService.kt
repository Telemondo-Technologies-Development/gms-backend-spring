package com.gms.backend.domain.domain.service.asset

import com.gms.backend.domain.application.rest.asset.AssetController
import com.gms.backend.domain.application.rest.asset.AssetMaintenanceController
import com.gms.backend.domain.application.rest.asset.MaintenanceScheduleController
import com.gms.backend.domain.domain.model.asset.Asset
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.Instant
import java.util.*

interface AssetService {
    fun createAsset(body: AssetController.AssetPostDTO): AssetController.AssetSummaryDTO
    fun updateAsset(id: UUID, body: AssetController.AssetPutDTO): AssetController.AssetSummaryDTO
    fun getAssets(
        pageable: Pageable,
        name: String?,
        branchId: UUID?,
        categoryId: UUID?,
        condition: Asset.AssetCondition?,
        status: Asset.AssetStatus?,
        dateFrom: Instant?,
        dateTo: Instant?
    ): Page<AssetController.AssetTableDTO>
    fun getAssetById(id: UUID): AssetController.AssetTableDTO
    fun deleteAsset(id: UUID)
    fun getAssetMaintenance(assetId: UUID, pageable: Pageable): Page<AssetMaintenanceController.AssetMaintenanceTableDTO>
    fun getAssetSchedules(assetId: UUID, pageable: Pageable): Page<MaintenanceScheduleController.ScheduleTableDTO>
}
