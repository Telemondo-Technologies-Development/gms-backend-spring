package com.gms.backend.impl.service.asset

import com.gms.backend.domain.application.rest.asset.BrandController
import com.gms.backend.domain.domain.repository.asset.BrandRepository
import com.gms.backend.domain.impl.domain.service.asset.BrandServiceImpl
import com.gms.backend.impl.service.BaseTest
import io.nats.client.Connection
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import java.util.*
import kotlin.jvm.optionals.getOrNull

class BrandServiceImplTest
@Autowired constructor(
    private val brandServiceImpl: BrandServiceImpl,
    private val brandRepository: BrandRepository,
    private val entityManager: EntityManager,
    private val nc: Connection
) : BaseTest() {

    @Test
    fun natsConnection() {
        assertEquals(Connection.Status.CONNECTED, nc.status)
    }

    @Test
    fun testReadBrands() {
        // Given
        // When
        val brands = brandServiceImpl.getBrands(Pageable.unpaged())
        // Then
        assertEquals(3, brands.totalElements)
    }

    @Test
    fun testGetBrandById() {
        val id = UUID.fromString("0195574c-878e-718b-9354-0d41ca454c0e")
        val result = brandServiceImpl.getBrandById(id)

        assertNotNull(result)
        assertEquals(id, result.id)
        assertEquals("Precor", result.name)
    }

    @Test
    fun testCreateBrand() {
        // Given
        val actorId = UUID.fromString("f520a8fb-3824-4339-8bb4-3732c8a3f617")
        val brandDTO = BrandController.BrandPostDTO(
            name = "New Brand",
            createdById = actorId
        )

        // When
        val saved = brandServiceImpl.createBrand(brandDTO)

        // Then
        assertNotNull(saved.id)
        assertEquals("New Brand", saved.name)
    }

    @Test
    fun testUpdateBrand() {
        // Given
        val id = UUID.fromString("0195574c-878e-79c0-9939-e6a0d6323c2a")
        val actorId = UUID.fromString("f520a8fb-3824-4339-8bb4-3732c8a3f617")
        val brandPutDTO = BrandController.BrandPutDTO(
            name = "Updated Brand Name",
            updatedById = actorId
        )

        // When
        val updated = brandServiceImpl.updateBrand(id, brandPutDTO)

        // Then
        assertEquals("Updated Brand Name", updated.name)
        assertNotNull(updated.id)
    }

    @Test
    fun testDeleteBrand() {
        // Given
        val id = UUID.fromString("0195574c-878e-79c0-9939-e6a0d6323c2a")
        val initialCount = brandRepository.count()

        // When
        brandServiceImpl.deleteBrand(id)

        // Then
        assertEquals(initialCount - 1, brandRepository.count())
        assertEquals(null, brandRepository.findById(id).getOrNull())

        assertThrows<NoSuchElementException> {
            brandServiceImpl.deleteBrand(id)
        }
    }
}