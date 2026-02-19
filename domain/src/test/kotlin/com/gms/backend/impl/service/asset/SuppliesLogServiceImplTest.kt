package com.gms.backend.impl.service.asset

import com.gms.backend.domain.application.rest.asset.SuppliesLogController
import com.gms.backend.domain.domain.repository.asset.SuppliesLogRepository
import com.gms.backend.domain.impl.domain.service.asset.SuppliesLogServiceImpl
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

class SuppliesLogServiceImplTest
@Autowired constructor(
    private val suppliesLogServiceImpl: SuppliesLogServiceImpl,
    private val suppliesLogRepository: SuppliesLogRepository,
    private val entityManager: EntityManager,
    private val nc: Connection
) : BaseTest() {

    @Test
    fun natsConnection() {
        assertEquals(Connection.Status.CONNECTED, nc.status)
    }

    @Test
    fun testReadSuppliesLogs() {
        // Given
        // When
        val logs = suppliesLogServiceImpl.getSuppliesLogs(Pageable.unpaged())

        // Then
        assertEquals(2, logs.totalElements)
    }

    @Test
    fun testGetSuppliesLogById() {
        // Given
        val id = UUID.fromString("019c21c9-cb27-7c48-bcac-03e5a6fa7924")

        // When
        val result = suppliesLogServiceImpl.getSuppliesLogById(id)

        // Then
        assertNotNull(result)
        assertEquals(id, result.id)
    }

    @Test
    fun testCreateSuppliesLog() {
        // Given
        val supplyId = UUID.fromString("019c21b4-6b08-75df-9a08-c7a10ed983f0")
        val actorId = UUID.fromString("f520a8fb-3824-4339-8bb4-3732c8a3f617")
        val body = SuppliesLogController.SuppliesLogPostDTO(
            name = "Gallon Drinking Water Restocked",
            suppliesId = supplyId,
            createdById = actorId,
            quantity = 10,
            remarks = "Initial stock",
            objectIds = emptyList()
        )

        // When
        val saved = suppliesLogServiceImpl.createSuppliesLog(body)

        // Then
        assertNotNull(saved.id)
        assertEquals(10, saved.quantity)
    }

    @Test
    fun testUpdateSuppliesLog() {
        // Given
        val id = UUID.fromString("019c21c9-f9bf-706d-b37d-8c01ee611f92")
        val supplyId = UUID.fromString("019c21b4-6b08-75df-9a08-c7a10ed983f0")
        val actorId = UUID.fromString("f520a8fb-3824-4339-8bb4-3732c8a3f617")
        val body = SuppliesLogController.SuppliesLogPutDTO(
            name = "Updated Supply Log Name",
            suppliesId = supplyId,
            updatedById = actorId,
            quantity = 50,
            remarks = "Updated remarks",
            objectIds = emptyList()
        )

        // When
        val updated = suppliesLogServiceImpl.updateSuppliesLog(id, body)

        // Then
        assertEquals(50, updated.quantity)
    }

    @Test
    fun testDeleteSuppliesLog() {
        // Given
        val id = UUID.fromString("019c21c9-f9bf-706d-b37d-8c01ee611f92")
        val initialCount = suppliesLogRepository.count()

        // When
        suppliesLogServiceImpl.deleteSuppliesLog(id)

        // Then
        assertEquals(initialCount - 1, suppliesLogRepository.count())
        assertEquals(null, suppliesLogRepository.findById(id).getOrNull())
        assertThrows<NoSuchElementException> {
            suppliesLogServiceImpl.deleteSuppliesLog(id)
        }
    }
}