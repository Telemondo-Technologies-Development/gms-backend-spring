package com.gms.backend.impl.service.asset

import com.gms.backend.domain.application.rest.asset.AssetController
import com.gms.backend.domain.domain.repository.asset.AssetRepository
import com.gms.backend.domain.impl.domain.service.asset.AssetServiceImpl
import com.gms.backend.impl.service.BaseTest
import io.nats.client.Connection
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.test.annotation.DirtiesContext
import java.time.Instant
import java.util.NoSuchElementException
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class AssetServiceImplTest @Autowired constructor(
    private val assetServiceImpl: AssetServiceImpl,
    private val assetRepository: AssetRepository,
    private val nc: Connection
) : BaseTest() {

    @Test
    fun natsConnection() {
        assertEquals(Connection.Status.CONNECTED, nc.status)
    }

    @Test
    fun testReadAssetLogs() {
        val logs = assetServiceImpl.getAssets(Pageable.unpaged())
        assertEquals(3, logs.totalElements)
    }

    @Test
    fun testGetAssetById() {
        val id = UUID.fromString("019ba2f5-b6e6-7271-893c-ffab220055b1") //Treadmill
        val result = assetServiceImpl.getAssetById(id)

        assertNotNull(result)
        assertEquals(id, result.id)
    }

    @Test
    fun testCreateAsset() {
        val branchId = UUID.fromString("019ba279-a6e6-7271-893c-ffab220040a2")
        val categoryId = UUID.fromString("019ba2f1-a6e6-7271-893c-ffab220040a1")
        val adminId = UUID.fromString("f520a8fb-3824-4339-8bb4-3732c8a3f617")

        val body = AssetController.AssetPostDTO(
            name = "New Equipment",
            branchId = branchId,
            assetCategoryId = categoryId,
            manufacturedDate = Instant.parse("2025-01-01T10:00:00Z"),
            endOfLife = Instant.parse("2026-01-01T00:00:00Z"),
            remarks = "Asset Remarks",
            createdById = adminId,
            objectIds = emptyList()
        )

        val saved = assetServiceImpl.createAsset(body)
        assertNotNull(saved.id)
        assertEquals("New Equipment", saved.name)
    }

    @Test
    fun testUpdateAsset() {
        // Given
        val id = UUID.fromString("019ba2f5-b6e6-7271-893c-ffab220055b3") //Stationary Bike
        val branchId = UUID.fromString("019ba279-a6e6-7271-893c-ffab220040a2")
        val categoryId = UUID.fromString("019ba2f1-a6e6-7271-893c-ffab220040a1")
        val adminId = UUID.fromString("f520a8fb-3824-4339-8bb4-3732c8a3f617")

        val body = AssetController.AssetPutDTO(
            name = "Updated Asset Name",
            branchId = branchId,
            assetCategoryId = categoryId,
            manufacturedDate = Instant.parse("2025-01-01T10:00:00Z"),
            endOfLife = Instant.parse("2026-01-01T00:00:00Z"),
            remarks = "Updated Asset Remarks",
            updatedById = adminId,
            objectIds = emptyList()
        )

        val updated = assetServiceImpl.updateAsset(id, body)

        assertNotNull(updated)
        assertEquals(id, updated.id)
        assertEquals("Updated Asset Name", updated.name)
    }

    @Test
    fun testDeleteAsset() {
        // Given
        val id = UUID.fromString("019ba2f5-b6e6-7271-893c-ffab220055b3") //Stationary Bike
        val initialCount = assetRepository.count()

        // When
        assetServiceImpl.deleteAsset(id)

        // Then
        assertEquals(initialCount - 1, assetRepository.count())
        assertEquals(null, assetRepository.findById(id).getOrNull())
        assertThrows<NoSuchElementException> {
            assetServiceImpl.deleteAsset(id)
        }
    }

    @Test
    fun testGetAssetSchedules() {
        // Given
        val assetId = UUID.fromString("019ba2f5-b6e6-7271-893c-ffab220055b1")

        // When
        val result = assetServiceImpl.getAssetSchedules(assetId, Pageable.unpaged())

        // Then
        assertNotNull(result.content)
        assertEquals(4, result.content.size)
    }
}
