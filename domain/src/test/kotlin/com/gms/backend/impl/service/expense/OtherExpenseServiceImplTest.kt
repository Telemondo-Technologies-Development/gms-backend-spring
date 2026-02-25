package com.gms.backend.impl.service.expense

import com.gms.backend.domain.application.rest.expense.OtherExpenseController
import com.gms.backend.domain.domain.repository.expense.OtherExpenseRepository
import com.gms.backend.domain.impl.domain.service.expense.OtherExpenseServiceImpl
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

class OtherExpenseServiceImplTest @Autowired constructor(
    private val otherExpenseRepository: OtherExpenseRepository,
    private val otherExpenseServiceImpl: OtherExpenseServiceImpl,
    private val nc: Connection
) : BaseTest() {

    @Test
    fun natsConnection() {
        assertEquals(nc.status, Connection.Status.CONNECTED)
    }

    @Test
    fun testCreateOtherExpense() {
        // Given
        val branchId = UUID.fromString("019ba279-a6e6-7271-893c-ffab220040a2")
        val actorId = UUID.fromString("8140f50d-a33f-4569-b76a-20c348b77222")
        val otherExpenseTypeId = UUID.fromString("019ba279-a6e6-7271-893c-ffab220099bb")

        val dto = OtherExpenseController.OtherExpenseCreateDTO(
            amount = BigDecimal("1200.00"),
            paidAt = Instant.now(),
            branchId = branchId,
            otherExpenseTypeId = otherExpenseTypeId,
            actorId = actorId,
            objectIds = emptySet()
        )

        // When
        val saved = otherExpenseServiceImpl.createOtherExpense(dto)

        // Then
        assertNotNull(saved.id)
        assertEquals(BigDecimal("1200.00"), saved.amount)
        assertEquals(branchId, saved.branchId)
    }

    @Test
    fun testGetOtherExpenses() {
        // Given
        val initialCount = otherExpenseRepository.count()

        // When
        val resultPage = otherExpenseServiceImpl.getOtherExpense(Pageable.unpaged())

        // Then
        assertNotNull(resultPage)
        assertEquals(initialCount, resultPage.totalElements)
    }

    @Test
    fun testUpdateOtherExpense() {
        // 1. Arrange: Create first
        val branchId = UUID.fromString("019ba279-a6e6-7271-893c-ffab220040a2")
        val actorId = UUID.fromString("8140f50d-a33f-4569-b76a-20c348b77222")
        val otherExpenseTypeId = UUID.fromString("019ba279-a6e6-7271-893c-ffab220099bb")

        val saved = otherExpenseServiceImpl.createOtherExpense(
            OtherExpenseController.OtherExpenseCreateDTO(
                amount = BigDecimal("500.00"),
                paidAt = Instant.now(),
                branchId = branchId,
                otherExpenseTypeId = otherExpenseTypeId,
                actorId = actorId
            )
        )
        val idToUpdate = saved.id

        // 2. Act
        val updateDto = OtherExpenseController.OtherExpenseUpdateDTO(
            amount = BigDecimal("750.00"),
            paidAt = Instant.now(),
            branchId = branchId,
            otherExpenseTypeId = otherExpenseTypeId,
            actorId = actorId
        )
        val updated = otherExpenseServiceImpl.updateOtherExpense(idToUpdate, updateDto)

        // 3. Assert
        assertEquals(BigDecimal("750.00"), updated.amount)
    }

    @Test
    fun testDeleteOtherExpense() {
        // 1. Arrange
        val branchId = UUID.fromString("019ba279-a6e6-7271-893c-ffab220040a2")
        val actorId = UUID.fromString("8140f50d-a33f-4569-b76a-20c348b77222")
        val otherExpenseTypeId = UUID.fromString("019ba279-a6e6-7271-893c-ffab220099bb")

        val saved = otherExpenseServiceImpl.createOtherExpense(
            OtherExpenseController.OtherExpenseCreateDTO(
                amount = BigDecimal("10.00"),
                paidAt = Instant.now(),
                branchId = branchId,
                otherExpenseTypeId = otherExpenseTypeId,
                actorId = actorId
            )
        )
        val idToDelete = saved.id

        // 2. Act
        otherExpenseServiceImpl.deleteOtherExpense(idToDelete)

        // 3. Assert
        val exists = otherExpenseRepository.existsById(idToDelete)
        assertEquals(false, exists, "The other expense should have been deleted")
    }
}