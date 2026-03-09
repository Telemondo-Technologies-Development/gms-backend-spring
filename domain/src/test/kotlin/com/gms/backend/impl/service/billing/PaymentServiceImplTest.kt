package com.gms.backend.impl.service.billing

import com.gms.backend.domain.application.rest.billing.PaymentController
import com.gms.backend.domain.domain.model.billing.Payment
import com.gms.backend.domain.domain.repository.billing.PaymentRepository
import com.gms.backend.domain.impl.domain.service.billing.PaymentServiceImpl
import com.gms.backend.impl.service.BaseTest
import io.nats.client.Connection
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import java.math.BigDecimal
import java.time.Instant
import java.util.*
import kotlin.jvm.optionals.getOrNull

class PaymentServiceImplTest
@Autowired constructor(
    private val paymentServiceImpl: PaymentServiceImpl,
    private val paymentRepository: PaymentRepository,
    private val entityManager: EntityManager,
    private val nc: Connection
) : BaseTest() {

    @Test
    fun natsConnection() {
        assertEquals(nc.status, Connection.Status.CONNECTED)
    }

    @Test
    fun testReadPayments() {
        // Given
        // When
        val payments = paymentServiceImpl.getPayments(
            Pageable.unpaged(),
            status = null,
            paymentMethodName =  null,
            dateFrom =  null,
            dateTo =  null
        )
        // Then
        assertEquals(1, payments.size)
    }

    @Test
    fun testCreatePayment() {
        // Given
        val createdById = UUID.fromString("16868948-705e-4011-9f42-bcc324c01ce0")
        val invoiceId = UUID.fromString("019c716e-86d4-7aeb-a8d2-517c464a9eab")
        val paymentMethodId = UUID.fromString("019c748b-6dd7-7f57-8135-c86b077d7f77")
        val payment = PaymentController.PaymentPostDTO(
            invoiceId = invoiceId,
            status = Payment.PaymentStatus.WAITING,
            paymentMethodId = paymentMethodId,
            amount = BigDecimal(100),
            paidAt = Instant.now(),
            createdById = createdById,
            referenceNum = null,
            failureReason = null
        )
        // When
        val saved = paymentServiceImpl.createPayment(payment)
        // Then
        assertEquals(0, saved.amount.compareTo(BigDecimal(100)))
        assertEquals(createdById, saved.createdById)
    }

    @Test
    fun testUpdatePayments() {
        // Given
        val updatedById = UUID.fromString("16868948-705e-4011-9f42-bcc324c01ce0")
        val paymentMethodId = UUID.fromString("019c748b-6dd7-7f57-8135-c86b077d7f77")
        val payment = PaymentController.PaymentPutDTO(
            status = Payment.PaymentStatus.WAITING,
            paymentMethodId = paymentMethodId,
            amount = BigDecimal(200),
            paidAt = Instant.now(),
            failureReason = "No Balance",
            updatedById = updatedById
        )
        val id = UUID.fromString("019c748e-57dd-71cc-8ad3-01923aa45f12")
        // When
        val updated = paymentServiceImpl.updatePayment(id, payment)
        // Then
        assertEquals(0, updated.amount.compareTo(BigDecimal(200)))
        assertEquals(updatedById, updated.updatedById)
    }

    @Test
    fun testDeletePayment() {
        // Then
        val id = UUID.fromString("019c748e-57dd-71cc-8ad3-01923aa45f12")
        val count = paymentRepository.count()
        // When
        val payment = paymentServiceImpl.deletePayment(id)
        // Then
        assertEquals(count - 1, paymentRepository.count())
        assertEquals(null, paymentRepository.findById(id).getOrNull())
        assertThrows<NoSuchElementException> {
            paymentServiceImpl.deletePayment(id)
        }
    }
}