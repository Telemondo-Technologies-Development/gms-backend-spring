package com.gms.backend.impl.service.asset

import com.gms.backend.domain.application.rest.asset.SupplyController
import com.gms.backend.domain.domain.repository.asset.SupplyRepository
import com.gms.backend.domain.impl.domain.service.asset.SupplyServiceImpl
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

class SupplyServiceImplTest
@Autowired constructor(
    private val supplyServiceImpl: SupplyServiceImpl,
    private val supplyRepository: SupplyRepository,
    private val entityManager: EntityManager,
    private val nc: Connection
) : BaseTest() {

    @Test
    fun natsConnection() {
        assertEquals(Connection.Status.CONNECTED, nc.status)
    }

    @Test
    fun testReadSupplies() {
        // Given
        // When
        val supplies = supplyServiceImpl.getSupplies(Pageable.unpaged())
        // Then
        assertEquals(2, supplies.totalElements)
    }

    @Test
    fun testGetSupplyById() {
        val id = UUID.fromString("019c21b4-6b08-75df-9a08-c7a10ed983f0")
        val result = supplyServiceImpl.getSupplyById(id)

        assertNotNull(result)
        assertEquals(id, result.id)
        assertEquals("Gallon Drinking Water", result.name)
    }

    @Test
    fun testCreateSupply() {
        // Given
        val branchId = UUID.fromString("019ba279-a6e6-7271-893c-ffab220040a2")
        val actorId = UUID.fromString("f520a8fb-3824-4339-8bb4-3732c8a3f617")
        val supplyDTO = SupplyController.SupplyPostDTO(
            name = "Disposable Gloves",
            description = "Disposable Gloves",
            branchId = branchId,
            createdById = actorId,
            objectIds = emptyList()
        )

        // When
        val saved = supplyServiceImpl.createSupply(supplyDTO)

        // Then
        assertNotNull(saved.id)
        assertEquals("Disposable Gloves", saved.name)
        assertEquals(0, saved.quantity)
    }

    @Test
    fun testUpdateSupply() {
        // Given
        val id = UUID.fromString("019c2caa-2880-7436-b2aa-fea1e4dc528c")
        val branchId = UUID.fromString("019ba279-a6e6-7271-893c-ffab220040a2")
        val actorId = UUID.fromString("f520a8fb-3824-4339-8bb4-3732c8a3f617")
        val supplyPutDTO = SupplyController.SupplyPutDTO(
            name = "Updated Supply Name",
            description = "Updated Description",
            branchId = branchId,
            updatedById = actorId,
            objectIds = emptyList()
        )

        // When
        val updated = supplyServiceImpl.updateSupply(id, supplyPutDTO)

        // Then
        assertEquals("Updated Supply Name", updated.name)
        assertNotNull(updated.id)
    }

    @Test
    fun testGetSupplyLogs() {
        // Given
        val supplyId = UUID.fromString("019c21b4-6b08-75df-9a08-c7a10ed983f0")

        // When
        val result = supplyServiceImpl.getSupplyLogs(supplyId, Pageable.unpaged())

        // Then
        assertNotNull(result.content)
        assertEquals(2, result.content.size)
    }

    @Test
    fun testDeleteSupply() {
        // Given
        val id = UUID.fromString("019c2caa-2880-7436-b2aa-fea1e4dc528c")
        val initialCount = supplyRepository.count()

        // When
        supplyServiceImpl.deleteSupply(id)

        // Then
        assertEquals(initialCount - 1, supplyRepository.count())
        assertEquals(null, supplyRepository.findById(id).getOrNull())
        assertThrows<NoSuchElementException> {
            supplyServiceImpl.deleteSupply(id)
        }
    }
}