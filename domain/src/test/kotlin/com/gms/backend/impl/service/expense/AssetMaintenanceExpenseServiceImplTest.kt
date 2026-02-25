package com.gms.backend.impl.service.expense

import com.gms.backend.domain.application.rest.expense.AssetMaintenanceExpenseController
import com.gms.backend.domain.domain.repository.expense.AssetMaintenanceExpenseRepository
import com.gms.backend.domain.impl.domain.service.expense.AssetMaintenanceExpenseServiceImpl
import com.gms.backend.impl.service.BaseTest
import io.nats.client.Connection
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import java.math.BigDecimal
import java.time.Instant
import java.util.*

class AssetMaintenanceExpenseServiceImplTest @Autowired constructor(
    private val assetMaintenanceExpenseRepository: AssetMaintenanceExpenseRepository,
    private val assetMaintenanceExpenseServiceImpl: AssetMaintenanceExpenseServiceImpl,
    private val nc: Connection
) : BaseTest() {

    @Test
    fun natsConnection() {
        assertEquals(nc.status, Connection.Status.CONNECTED)
    }

    @Test
    fun testCreateAssetMaintenanceExpense() {
        val branchId = UUID.fromString("019ba279-a6e6-7271-893c-ffab220040a2")
        val actorId = UUID.fromString("8140f50d-a33f-4569-b76a-20c348b77222")
        val assetMaintenanceId = UUID.fromString("019ba279-a6e6-7271-893c-ffab220011bb")

        val dto = AssetMaintenanceExpenseController.AssetMaintenanceExpenseCreateDTO(
            amount = BigDecimal("250.75"),
            paidAt = Instant.now(),
            branchId = branchId,
            assetMaintenanceId = assetMaintenanceId,
            actorId = actorId,
            objectIds = emptySet()
        )

        // When
        val saved = assetMaintenanceExpenseServiceImpl.createAssetMaintenanceExpense(dto)

        // Then
        assertNotNull(saved)
        assertEquals(BigDecimal("250.75"), saved.amount)
        assertNotNull(saved.createdAt)
    }

    @Test
    fun testGetAssetMaintenanceExpenses() {
        // Given
        val initialCount = assetMaintenanceExpenseRepository.count()

        // When
        val resultPage = assetMaintenanceExpenseServiceImpl.getAssetMaintenanceExpense(Pageable.unpaged())

        // Then
        assertNotNull(resultPage)
        assertEquals(initialCount, resultPage.totalElements)
    }

    @Test
    fun testUpdateAssetMaintenanceExpense() {
        // 1. Arrange: Create a fresh record to update
        val branchId = UUID.fromString("019ba279-a6e6-7271-893c-ffab220040a2")
        val actorId = UUID.fromString("8140f50d-a33f-4569-b76a-20c348b77222")
        val assetMaintenanceId = UUID.fromString("019ba279-a6e6-7271-893c-ffab220011bb")

        assetMaintenanceExpenseServiceImpl.createAssetMaintenanceExpense(
            AssetMaintenanceExpenseController.AssetMaintenanceExpenseCreateDTO(
                amount = BigDecimal("100.00"),
                paidAt = Instant.now(),
                branchId = branchId,
                assetMaintenanceId = assetMaintenanceId,
                actorId = actorId
            )
        )
        val idToUpdate = assetMaintenanceExpenseRepository.findAll().last().id

        // 2. Act: Prepare and execute update
        val updateDto = AssetMaintenanceExpenseController.AssetMaintenanceExpenseUpdateDTO(
            amount = BigDecimal("500.00"), // Change amount
            paidAt = Instant.now(),
            branchId = branchId,
            assetMaintenanceId = assetMaintenanceId,
            actorId = actorId
        )
        val updated = assetMaintenanceExpenseServiceImpl.updateAssetMaintenanceExpense(idToUpdate, updateDto)

        // 3. Assert
        assertEquals(BigDecimal("500.00"), updated.amount)
    }

    @Test
    fun testDeleteAssetMaintenanceExpense() {
        // 1. Arrange
        val branchId = UUID.fromString("019ba279-a6e6-7271-893c-ffab220040a2")
        val actorId = UUID.fromString("8140f50d-a33f-4569-b76a-20c348b77222")
        val assetMaintenanceId = UUID.fromString("019ba279-a6e6-7271-893c-ffab220011bb")

        assetMaintenanceExpenseServiceImpl.createAssetMaintenanceExpense(
            AssetMaintenanceExpenseController.AssetMaintenanceExpenseCreateDTO(
                amount = BigDecimal("1.00"),
                paidAt = Instant.now(),
                branchId = branchId,
                assetMaintenanceId = assetMaintenanceId,
                actorId = actorId
            )
        )
        val idToDelete = assetMaintenanceExpenseRepository.findAll().last().id

        // 2. Act
        assetMaintenanceExpenseServiceImpl.deleteAssetMaintenanceExpense(idToDelete)

        // 3. Assert
        val exists = assetMaintenanceExpenseRepository.existsById(idToDelete)
        assertEquals(false, exists, "The maintenance expense should have been deleted")
    }
}