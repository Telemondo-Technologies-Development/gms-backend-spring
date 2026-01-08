package com.gms.backend.domain.domain.repository.asset

import com.gms.backend.domain.domain.model.asset.Asset
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AssetRepository : JpaRepository<Asset, UUID> {
    fun findAllByAssetObjectsId(id: UUID): List<Asset>
}
