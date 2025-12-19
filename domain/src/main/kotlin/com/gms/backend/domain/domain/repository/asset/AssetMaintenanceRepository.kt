package com.gms.backend.domain.domain.repository.asset

import com.gms.backend.domain.domain.model.asset.AssetMaintenance
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AssetMaintenanceRepository : JpaRepository<AssetMaintenance, UUID> {
    fun findAllByAssetMaintenanceObjectsId(id: UUID): List<AssetMaintenance>
}
