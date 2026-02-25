package com.gms.backend.impl.service.expense

import com.gms.backend.domain.application.rest.expense.AssetExpenseController
import com.gms.backend.domain.domain.repository.expense.AssetExpenseRepository
import com.gms.backend.domain.impl.domain.service.expense.AssetExpenseServiceImpl
import com.gms.backend.impl.service.BaseTest
import org.springframework.beans.factory.annotation.Autowired
import io.nats.client.Connection
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.data.domain.Pageable
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

class AssetExpenseServiceImplTest
@Autowired constructor(
    private val assetExpenseRepository: AssetExpenseRepository,
    private val assetExpenseServiceImpl: AssetExpenseServiceImpl,
    private val nc: Connection
): BaseTest() {
    @Test
    fun natsConnection() {
        assertEquals(nc.status, Connection.Status.CONNECTED)
    }
    @Test
    fun testCreateAssetExpense() {
        val branchId = UUID.fromString("019ba279-a6e6-7271-893c-ffab220040a2")
        val assetId = UUID.fromString("019ba2f5-b6e6-7271-893c-ffab220055b1")
        val actorId = UUID.fromString("8140f50d-a33f-4569-b76a-20c348b77222")

        val dto = AssetExpenseController.AssetExpenseCreateDTO(
            amount = BigDecimal("1500.00"),
            paidAt = Instant.now(),
            branchId = branchId,
            assetId = assetId,
            actorId = actorId,
            objectIds = emptySet()
        )

        // When
        val saved = assetExpenseServiceImpl.createAssetExpense(dto)

        // Then
        assertNotNull(saved.id)
        assertEquals(BigDecimal("1500.00"), saved.amount)
        assertEquals(branchId, saved.branchId)
    }

    @Test
    fun testGetAssetExpenses() {
        // Given: Ensure there is data.
        val initialCount = assetExpenseRepository.count()

        // When
        val resultPage = assetExpenseServiceImpl.getAssetExpense(Pageable.unpaged())

        // Then
        assertNotNull(resultPage)
        assertEquals(initialCount, resultPage.totalElements)

        if (initialCount > 0) {
            val firstItem = resultPage.content[0]
            assertNotNull(firstItem.amount)
            assertNotNull(firstItem.assetId)
        }
    }

    @Test
    fun testUpdateAssetExpense() {
        // 1. Arrange: Create and save a record first so we have an ID to work with
        val branchId = UUID.fromString("019ba279-a6e6-7271-893c-ffab220040a2")
        val assetId = UUID.fromString("019ba2f5-b6e6-7271-893c-ffab220055b1")
        val actorId = UUID.fromString("8140f50d-a33f-4569-b76a-20c348b77222")

        val dto = AssetExpenseController.AssetExpenseCreateDTO(
            amount = BigDecimal("100.00"),
            paidAt = Instant.now(),
            branchId = branchId,
            assetId = assetId,
            actorId = actorId
        )
        val savedRecord = assetExpenseServiceImpl.createAssetExpense(dto)
        val idToUpdate = savedRecord.id // Now we have a guaranteed valid ID

        // 2. Act: Prepare the update DTO
        val updateDto = AssetExpenseController.AssetExpenseUpdateDTO(
            amount = BigDecimal("250.00"), // Changing the amount
            paidAt = Instant.now(),
            branchId = branchId,
            assetId = assetId,
            actorId = actorId
        )
        val updated = assetExpenseServiceImpl.updateAssetExpense(idToUpdate, updateDto)

        // 3. Assert
        assertEquals(BigDecimal("250.00"), updated.amount)
    }
    @Test
    fun testDeleteAssetExpense() {
        // 1. Arrange: Create a fresh record so we have something to delete
        val branchId = UUID.fromString("019ba279-a6e6-7271-893c-ffab220040a2")
        val assetId = UUID.fromString("019ba2f5-b6e6-7271-893c-ffab220055b1")
        val actorId = UUID.fromString("8140f50d-a33f-4569-b76a-20c348b77222")

        val saved = assetExpenseServiceImpl.createAssetExpense(
            AssetExpenseController.AssetExpenseCreateDTO(
                amount = BigDecimal("99.99"),
                paidAt = Instant.now(),
                branchId = branchId,
                assetId = assetId,
                actorId = actorId
            )
        )
        val idToDelete = saved.id

        // 2. Act: Delete it
        assetExpenseServiceImpl.deleteAssetExpense(idToDelete)

        // 3. Assert: Verify it's gone
        val exists = assetExpenseRepository.existsById(idToDelete)
        assertEquals(false, exists, "The expense should no longer exist in the database")
    }
}