package com.gms.backend.impl.service.expense

import com.gms.backend.domain.application.rest.expense.SuppliesExpenseController
import com.gms.backend.domain.domain.repository.expense.SuppliesExpenseRepository
import com.gms.backend.domain.impl.domain.service.expense.SuppliesExpenseServiceImpl
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

class SuppliesExpenseServiceImplTest @Autowired constructor(
    private val suppliesExpenseRepository: SuppliesExpenseRepository,
    private val suppliesExpenseServiceImpl: SuppliesExpenseServiceImpl,
    private val nc: Connection
) : BaseTest() {

    @Test
    fun natsConnection() {
        assertEquals(nc.status, Connection.Status.CONNECTED)
    }

    @Test
    fun testCreateSuppliesExpense() {
        // Given
        val branchId = UUID.fromString("019ba279-a6e6-7271-893c-ffab220040a2")
        val actorId = UUID.fromString("8140f50d-a33f-4569-b76a-20c348b77222")
        val suppliesLogId = UUID.fromString("019c21c9-cb27-7c48-bcac-03e5a6fa7924")

        val dto = SuppliesExpenseController.SuppliesExpenseCreateDTO(
            amount = BigDecimal("3200.50"),
            paidAt = Instant.now(),
            branchId = branchId,
            suppliesLogId = suppliesLogId,
            actorId = actorId,
            objectIds = emptySet()
        )

        // When
        val saved = suppliesExpenseServiceImpl.createSuppliesExpense(dto)

        // Then
        assertNotNull(saved.id)
        assertEquals(BigDecimal("3200.50"), saved.amount)
        assertEquals(branchId, saved.branchId)
    }

    @Test
    fun testGetSuppliesExpenses() {
        // Given
        val initialCount = suppliesExpenseRepository.count()

        // When
        val resultPage = suppliesExpenseServiceImpl.getSuppliesExpense(Pageable.unpaged())

        // Then
        assertNotNull(resultPage)
        assertEquals(initialCount, resultPage.totalElements)
    }

    @Test
    fun testUpdateSuppliesExpense() {
        // 1. Arrange: Create first
        val branchId = UUID.fromString("019ba279-a6e6-7271-893c-ffab220040a2")
        val actorId = UUID.fromString("8140f50d-a33f-4569-b76a-20c348b77222")
        val suppliesLogId = UUID.fromString("019c21c9-cb27-7c48-bcac-03e5a6fa7924")

        val saved = suppliesExpenseServiceImpl.createSuppliesExpense(
            SuppliesExpenseController.SuppliesExpenseCreateDTO(
                amount = BigDecimal("1000.00"),
                paidAt = Instant.now(),
                branchId = branchId,
                suppliesLogId = suppliesLogId,
                actorId = actorId
            )
        )
        val idToUpdate = saved.id
        val newSuppliesLogId = UUID.fromString("019c21c9-f9bf-706d-b37d-8c01ee611f92")

        // 2. Act
        val updateDto = SuppliesExpenseController.SuppliesExpenseUpdateDTO(
            amount = BigDecimal("1500.00"),
            paidAt = Instant.now(),
            branchId = branchId,
            suppliesLogId = newSuppliesLogId,
            actorId = actorId
        )
        val updated = suppliesExpenseServiceImpl.updateSuppliesExpense(idToUpdate, updateDto)

        // 3. Assert
        assertEquals(BigDecimal("1500.00"), updated.amount)
    }

    @Test
    fun testDeleteSuppliesExpense() {
        // 1. Arrange
        val branchId = UUID.fromString("019ba279-a6e6-7271-893c-ffab220040a2")
        val actorId = UUID.fromString("8140f50d-a33f-4569-b76a-20c348b77222")
        val suppliesLogId = UUID.fromString("019c21c9-cb27-7c48-bcac-03e5a6fa7924")

        val saved = suppliesExpenseServiceImpl.createSuppliesExpense(
            SuppliesExpenseController.SuppliesExpenseCreateDTO(
                amount = BigDecimal("50.00"),
                paidAt = Instant.now(),
                branchId = branchId,
                suppliesLogId = suppliesLogId,
                actorId = actorId
            )
        )
        val idToDelete = saved.id

        // 2. Act
        suppliesExpenseServiceImpl.deleteSuppliesExpense(idToDelete)

        // 3. Assert
        val exists = suppliesExpenseRepository.existsById(idToDelete)
        assertEquals(false, exists, "The supplies expense should no longer exist in the database")
    }
}