package com.gms.backend.impl.service.billing

import com.gms.backend.domain.application.rest.billing.PaymentMethodController
import com.gms.backend.domain.domain.repository.billing.PaymentMethodRepository
import com.gms.backend.domain.impl.domain.service.billing.PaymentMethodServiceImpl
import com.gms.backend.impl.service.BaseTest
import io.nats.client.Connection
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Pageable
import java.util.*
import kotlin.jvm.optionals.getOrNull

class PaymentMethodServiceImplTest
@Autowired constructor(
    private val paymentMethodServiceImpl: PaymentMethodServiceImpl,
    private val paymentMethodRepository: PaymentMethodRepository,
    private val entityManager: EntityManager,
    private val nc: Connection
) : BaseTest() {

    @Test
    fun natsConnection() {
        assertEquals(nc.status, Connection.Status.CONNECTED)
    }

    @Test
    fun testReadPaymentMethods() {
        // Given
        // When
        val paymentMethods = paymentMethodServiceImpl.getPaymentMethods(Pageable.unpaged())
        // Then
        assertEquals(3, paymentMethods.size)
    }

    @Test
    fun testCreatePaymentMethod() {
        // Given
        val createdById = UUID.fromString("16868948-705e-4011-9f42-bcc324c01ce0")
        val paymentMethod = PaymentMethodController.PaymentMethodPostDTO(
            name = "Test",
            createdById = createdById
        )
        // When
        val saved = paymentMethodServiceImpl.createPaymentMethod(paymentMethod)
        // Then
        assertEquals("Test", saved.name)
        assertEquals(createdById, saved.createdById)
        assertThrows<DataIntegrityViolationException>({
            paymentMethodServiceImpl.createPaymentMethod(paymentMethod)
            entityManager.flush()
        })
    }

    @Test
    fun testUpdatePaymentMethods() {
        // Given
        val updatedById = UUID.fromString("16868948-705e-4011-9f42-bcc324c01ce0")
        val paymentMethod = PaymentMethodController.PaymentMethodPutDTO(
            name = "Test",
            updatedById = updatedById
        )
        val id = UUID.fromString("019c748b-4b92-7982-a8d0-41ebfa856dd8")
        // When
        val updated = paymentMethodServiceImpl.updatePaymentMethod(id, paymentMethod)
        // Then
        assertEquals("Test", updated.name)
        assertEquals(updatedById, updated.updatedById)
    }

    @Test
    fun testDeletePaymentMethod() {
        // Then
        val id = UUID.fromString("019c748b-848f-79ac-82df-9cd6fa601a45")
        val count = paymentMethodRepository.count()
        // When
        val paymentMethod = paymentMethodServiceImpl.deletePaymentMethod(id)
        // Then
        assertEquals(count - 1, paymentMethodRepository.count())
        assertEquals(null, paymentMethodRepository.findById(id).getOrNull())
        assertThrows<NoSuchElementException> {
            paymentMethodServiceImpl.deletePaymentMethod(id)
        }
    }
}