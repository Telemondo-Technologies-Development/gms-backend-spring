package com.gms.backend.impl.service.expense

import com.gms.backend.domain.application.rest.expense.UtilityExpenseController
import com.gms.backend.domain.domain.repository.expense.UtilityExpenseRepository
import com.gms.backend.domain.impl.domain.service.expense.UtilityExpenseServiceImpl
import com.gms.backend.impl.service.BaseTest
import io.nats.client.Connection
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.util.*

class UtilityExpenseServiceImplTest @Autowired constructor(
    private val utilityExpenseRepository: UtilityExpenseRepository,
    private val utilityExpenseServiceImpl: UtilityExpenseServiceImpl,
    private val nc: Connection
) : BaseTest() {

    @Test
    fun natsConnection() {
        assertEquals(nc.status, Connection.Status.CONNECTED)
    }

    @Test
    fun testCreateUtilityExpense() {
        // Given
        val branchId = UUID.fromString("019ba279-a6e6-7271-893c-ffab220040a2")
        val actorId = UUID.fromString("8140f50d-a33f-4569-b76a-20c348b77222")
        val utilityTypeId = UUID.fromString("019ba279-a6e6-7271-893c-ffab2200cc11")

        val dto = UtilityExpenseController.UtilityExpenseCreateDTO(
            meter = "ELEC-VOLT-12345",
            amount = BigDecimal("4500.50"),
            period = LocalDate.now(),
            paidAt = Instant.now(),
            branchId = branchId,
            utilityTypeId = utilityTypeId,
            actorId = actorId,
            objectIds = emptySet()
        )

        // When
        val saved = utilityExpenseServiceImpl.createUtilityExpense(dto)

        // Then
        assertNotNull(saved.createdAt)
        assertEquals(BigDecimal("4500.50"), saved.amount)
        assertEquals("ELEC-VOLT-12345", saved.meter)
    }

    @Test
    fun testGetUtilityExpenses() {
        // Given
        val initialCount = utilityExpenseRepository.count()

        // When
        val resultPage = utilityExpenseServiceImpl.getUtilityExpense(Pageable.unpaged())

        // Then
        assertNotNull(resultPage)
        assertEquals(initialCount, resultPage.totalElements)
    }

    @Test
    fun testUpdateUtilityExpense() {
        // 1. Arrange: Create a record first
        val branchId = UUID.fromString("019ba279-a6e6-7271-893c-ffab220040a2")
        val actorId = UUID.fromString("8140f50d-a33f-4569-b76a-20c348b77222")
        val utilityTypeId = UUID.fromString("019ba279-a6e6-7271-893c-ffab2200cc22")

        utilityExpenseServiceImpl.createUtilityExpense(
            UtilityExpenseController.UtilityExpenseCreateDTO(
                meter = "INITIAL-METER",
                amount = BigDecimal("100.00"),
                period = LocalDate.now(),
                paidAt = Instant.now(),
                branchId = branchId,
                utilityTypeId = utilityTypeId,
                actorId = actorId
            )
        )
        // Fetch the ID of the record we just saved
        val idToUpdate = utilityExpenseRepository.findAll().last().id

        // 2. Act: Prepare and execute update
        val updateDto = UtilityExpenseController.UtilityExpenseUpdateDTO(
            meter = "UPDATED-METER",
            amount = BigDecimal("200.00"),
            period = LocalDate.now(),
            paidAt = Instant.now(),
            branchId = branchId,
            utilityTypeId = utilityTypeId,
            actorId = actorId
        )
        val updated = utilityExpenseServiceImpl.updateUtilityExpense(idToUpdate, updateDto)

        // 3. Assert
        assertEquals("UPDATED-METER", updated.meter)
        assertEquals(BigDecimal("200.00"), updated.amount)
    }

    @Test
    fun testDeleteUtilityExpense() {
        // 1. Arrange
        val branchId = UUID.fromString("019ba279-a6e6-7271-893c-ffab220040a2")
        val actorId = UUID.fromString("8140f50d-a33f-4569-b76a-20c348b77222")
        val utilityTypeId = UUID.fromString("019ba279-a6e6-7271-893c-ffab2200cc33")

        utilityExpenseServiceImpl.createUtilityExpense(
            UtilityExpenseController.UtilityExpenseCreateDTO(
                meter = "DELETE-ME",
                amount = BigDecimal("50.00"),
                period = LocalDate.now(),
                paidAt = Instant.now(),
                branchId = branchId,
                utilityTypeId = utilityTypeId,
                actorId = actorId
            )
        )
        val idToDelete = utilityExpenseRepository.findAll().last().id

        // 2. Act
        utilityExpenseServiceImpl.deleteUtilityExpense(idToDelete)

        // 3. Assert
        val exists = utilityExpenseRepository.existsById(idToDelete)
        assertEquals(false, exists, "Utility expense should have been deleted")
    }
}