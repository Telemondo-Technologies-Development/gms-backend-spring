package com.gms.backend.domain.domain.repository.asset

import com.gms.backend.domain.application.rest.asset.AssetController
import com.gms.backend.domain.domain.model.asset.Asset
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface AssetRepository : JpaRepository<Asset, UUID> {
    @Query(
        value = $$"""
        SELECT new com.gms.backend.domain.application.rest.asset.AssetController$AssetTableDTO(
                a.id,
                a.name,
                a.branch.id,
                a.assetCategory.id,
                a.manufacturedDate,
                a.endOfLife,
                a.acquisitionDate,
                a.status,
                a.remarks,
                a.createdById,
                a.updatedById,
                a.createdAt,
                a.updatedAt
            )
            FROM Asset a
        """, countQuery = "SELECT COUNT(a) FROM Asset a")
    fun findAllProjectedBy(pageable: Pageable): Page<AssetController.AssetTableDTO>

    @Query($$"""
        SELECT new com.gms.backend.domain.application.rest.asset.AssetController$AssetMappingDTO(a.id, b.id)
            FROM Asset a 
            JOIN a.brands b 
            WHERE a.id IN :assetIds
        """)
    fun findAllBrandIdsByAssetIds(@Param("assetIds") assetIds: List<UUID>): List<AssetController.AssetMappingDTO>

    @Query($$"""
        SELECT new com.gms.backend.domain.application.rest.asset.AssetController$AssetMappingDTO(a.id, o.id)
            FROM Asset a 
            JOIN a.assetObjects o 
            WHERE a.id IN :assetIds
        """)
    fun findAllObjectIdsByAssetIds(@Param("assetIds") assetIds: List<UUID>): List<AssetController.AssetMappingDTO>

    @Query(
        value = $$"""
        SELECT new com.gms.backend.domain.application.rest.asset.AssetController$AssetTableDTO(
                a.id,
                a.name,
                a.branch.id,
                a.assetCategory.id,
                a.manufacturedDate,
                a.endOfLife,
                a.acquisitionDate,
                a.status,
                a.remarks,
                a.createdById,
                a.updatedById,
                a.createdAt,
                a.updatedAt
            )
            FROM Asset a
            WHERE a.id = :id
        """)
    fun findProjectedBy(@Param("id") id: UUID): Optional<AssetController.AssetTableDTO>

    @Query("""
        SELECT b.id 
            FROM Asset a 
            JOIN a.brands b 
            WHERE a.id = :assetId
        """)
    fun findBrandIdsByAssetId(@Param("assetId") assetId: UUID): List<UUID>

    @Query("""
        SELECT o.id 
            FROM Asset a 
            JOIN a.assetObjects o 
            WHERE a.id = :assetId
        """)
    fun findObjectIdsByAssetId(@Param("assetId") assetId: UUID): List<UUID>
    fun findAllByAssetObjectsId(id: UUID): List<Asset>
    fun findAllBy(): List<Asset>
    fun findWithMaintenanceScheduleById(id: UUID): Optional<Asset>
}
