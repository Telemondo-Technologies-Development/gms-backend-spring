package com.gms.backend.impl.service.expense

import com.gms.backend.domain.application.rest.expense.SalaryExpenseController
import com.gms.backend.domain.domain.model.expense.SalaryExpense
import com.gms.backend.domain.domain.repository.expense.SalaryExpenseRepository
import com.gms.backend.domain.impl.domain.service.expense.SalaryExpenseServiceImpl
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

class SalaryExpenseServiceImplTest @Autowired constructor(
    private val salaryExpenseRepository: SalaryExpenseRepository,
    private val salaryExpenseServiceImpl: SalaryExpenseServiceImpl,
    private val nc: Connection
) : BaseTest() {

    @Test
    fun natsConnection() {
        assertEquals(nc.status, Connection.Status.CONNECTED)
    }

    @Test
    fun testCreateSalaryExpense() {
        // Given
        val branchId = UUID.fromString("019ba279-a6e6-7271-893c-ffab220040a2")
        val actorId = UUID.fromString("8140f50d-a33f-4569-b76a-20c348b77222") // Employee ID

        val dto = SalaryExpenseController.SalaryExpenseCreateDTO(
            salaryType = SalaryExpense.SalaryType.FULL,
            amount = BigDecimal("25000.00"),
            period = LocalDate.now(),
            paidAt = Instant.now(),
            branchId = branchId,
            actorId = actorId,
            objectIds = emptySet()
        )

        // When
        val saved = salaryExpenseServiceImpl.createSalaryExpense(dto)

        // Then
        assertNotNull(saved.createdAt)
        assertEquals(BigDecimal("25000.00"), saved.amount)
        assertEquals(branchId, saved.branchId)
    }

    @Test
    fun testGetSalaryExpenses() {
        // Given
        val initialCount = salaryExpenseRepository.count()

        // When
        val resultPage = salaryExpenseServiceImpl.getSalaryExpense(Pageable.unpaged())

        // Then
        assertNotNull(resultPage)
        assertEquals(initialCount, resultPage.totalElements)
    }

    @Test
    fun testUpdateSalaryExpense() {
        // 1. Arrange: Create first
        val branchId = UUID.fromString("019ba279-a6e6-7271-893c-ffab220040a2")
        val actorId = UUID.fromString("8140f50d-a33f-4569-b76a-20c348b77222")

        val createDto = SalaryExpenseController.SalaryExpenseCreateDTO(
            salaryType = SalaryExpense.SalaryType.ADVANCED,
            amount = BigDecimal("10000.00"),
            period = LocalDate.now(),
            paidAt = Instant.now(),
            branchId = branchId,
            actorId = actorId
        )
        val savedReadDto = salaryExpenseServiceImpl.createSalaryExpense(createDto)

        val idToUpdate = salaryExpenseRepository.findAll().last().id

        // 2. Act
        val updateDto = SalaryExpenseController.SalaryExpenseUpdateDTO(
            salaryType = SalaryExpense.SalaryType.PARTIAL,
            amount = BigDecimal("15000.00"),
            period = LocalDate.now(),
            paidAt = Instant.now(),
            branchId = branchId,
            actorId = actorId
        )
        val updated = salaryExpenseServiceImpl.updateSalaryExpense(idToUpdate, updateDto)

        // 3. Assert
        assertEquals(BigDecimal("15000.00"), updated.amount)
        assertEquals(SalaryExpense.SalaryType.PARTIAL, updated.salaryType)
    }

    @Test
    fun testDeleteSalaryExpense() {
        // 1. Arrange
        val branchId = UUID.fromString("019ba279-a6e6-7271-893c-ffab220040a2")
        val actorId = UUID.fromString("8140f50d-a33f-4569-b76a-20c348b77222")

        salaryExpenseServiceImpl.createSalaryExpense(
            SalaryExpenseController.SalaryExpenseCreateDTO(
                salaryType = SalaryExpense.SalaryType.ADVANCED,
                amount = BigDecimal("500.00"),
                period = LocalDate.now(),
                paidAt = Instant.now(),
                branchId = branchId,
                actorId = actorId
            )
        )
        val idToDelete = salaryExpenseRepository.findAll().last().id

        // 2. Act
        salaryExpenseServiceImpl.deleteSalaryExpense(idToDelete)

        // 3. Assert
        val exists = salaryExpenseRepository.existsById(idToDelete)
        assertEquals(false, exists, "The salary expense should no longer exist")
    }
}