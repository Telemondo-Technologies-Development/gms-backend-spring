package com.gms.backend.impl.service.asset

import com.gms.backend.domain.application.rest.asset.AssetCategoryController
import com.gms.backend.domain.domain.repository.asset.AssetCategoryRepository
import com.gms.backend.domain.impl.domain.service.asset.AssetCategoryServiceImpl
import com.gms.backend.impl.service.BaseTest
import io.nats.client.Connection
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.test.annotation.DirtiesContext
import java.util.*
import kotlin.jvm.optionals.getOrNull

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class AssetCategoryServiceImplTest @Autowired constructor(
    private val assetCategoryServiceImpl: AssetCategoryServiceImpl,
    private val assetCategoryRepository: AssetCategoryRepository,
    private val entityManager: EntityManager,
    private val nc: Connection
) : BaseTest() {

    @Test
    fun natsConnection() {
        assertEquals(Connection.Status.CONNECTED, nc.status)
    }

    @Test
    fun testReadAssetCategories() {
        val categories = assetCategoryServiceImpl.getAssetCategories()
        assertEquals(3, categories.size)
    }

    @Test
    fun testGetAssetCategoryById() {
        val id = UUID.fromString("019ba2f1-a6e6-7271-893c-ffab220040a1") //Cardio Equipment
        val result = assetCategoryServiceImpl.getAssetCategoryById(id)

        assertNotNull(result)
        assertEquals("Cardio Equipment", result.name)
    }

    @Test
    fun testCreateAssetCategory() {
        // Given
        val adminId = UUID.fromString("f520a8fb-3824-4339-8bb4-3732c8a3f617")
        val body = AssetCategoryController.AssetCategoryPostDTO(
            name = "New Asset Category",
            createdById = adminId
        )

        // When
        val saved = assetCategoryServiceImpl.createAssetCategory(body)

        // Then
        assertNotNull(saved.id)
        assertEquals("New Asset Category", saved.name)

        assertThrows<DataIntegrityViolationException> {
            assetCategoryServiceImpl.createAssetCategory(body)
            entityManager.flush()
        }
    }

    @Test
    fun testUpdateAssetCategory() {
        // Given
        val id = UUID.fromString("019ba2f1-a6e6-7271-893c-ffab220040a3") //Yoga equipmet
        val adminId = UUID.fromString("f520a8fb-3824-4339-8bb4-3732c8a3f617")
        val body = AssetCategoryController.AssetCategoryPutDTO(
            name = "Updated Asset Category",
            updatedById = adminId
        )

        // When
        val updated = assetCategoryServiceImpl.updateAssetCategory(id, body)

        // Then
        assertEquals("Updated Asset Category", updated.name)
    }

    @Test
    fun testDeleteAssetCategory() {
        // Given
        val id = UUID.fromString("019ba2f1-a6e6-7271-893c-ffab220040a3") //Yoga equipment
        val count = assetCategoryRepository.count()

        // When
        assetCategoryServiceImpl.deleteAssetCategory(id)

        // Then
        assertEquals(count - 1, assetCategoryRepository.count())
        assertEquals(null, assetCategoryRepository.findById(id).getOrNull())
        assertThrows<NoSuchElementException> {
            assetCategoryServiceImpl.deleteAssetCategory(id)
        }
    }
}