package com.gms.backend.impl.service.subscription

import com.gms.backend.domain.application.rest.subscription.SubscriptionController
import com.gms.backend.domain.domain.repository.subscription.SubscriptionRepository
import com.gms.backend.domain.impl.domain.service.subscription.SubscriptionServiceImpl
import com.gms.backend.impl.service.BaseTest
import io.nats.client.Connection
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Pageable
import java.math.BigDecimal
import java.util.*
import kotlin.jvm.optionals.getOrNull

class SubscriptionServiceImplTest
@Autowired constructor(
    private val subscriptionServiceImpl: SubscriptionServiceImpl,
    private val subscriptionRepository: SubscriptionRepository,
    private val entityManager: EntityManager,
    private val nc: Connection
) : BaseTest() {

    @Test
    fun natsConnection() {
        assertEquals(nc.status, Connection.Status.CONNECTED)
    }

    @Test
    fun testReadSubscriptions() {
        // Given
        // When
        val subscriptions = subscriptionServiceImpl.getSubscriptions(Pageable.unpaged())
        // Then
        assertEquals(2, subscriptions.size)
    }

    @Test
    fun testCreateSubscription() {
        // Given
        val billingCycleId = UUID.fromString("019bd471-6bcc-799c-8995-3f5e7b76cabf")
        val createdById = UUID.fromString("16868948-705e-4011-9f42-bcc324c01ce0")
        val subscription = SubscriptionController.SubscriptionPostDTO(
            billingCycleId = billingCycleId,
            name = "Testing",
            description = "testing",
            amount = BigDecimal(1829.1),
            createdById = createdById
        )
        // When
        val saved = subscriptionServiceImpl.createSubscription(subscription)
        // Then
        assertEquals("Testing", saved.name)
        assertEquals(0, saved.amount.compareTo(BigDecimal(1829.1)))
        assertEquals(createdById, saved.createdById)
        assertThrows<DataIntegrityViolationException>({
            subscriptionServiceImpl.createSubscription(subscription)
            entityManager.flush()
        })
    }

    @Test
    fun testUpdateSubscriptions() {
        // Given
        val billingCycleId = UUID.fromString("019bd471-6bcc-799c-8995-3f5e7b76cabf")
        val updatedById = UUID.fromString("16868948-705e-4011-9f42-bcc324c01ce0")
        val subscription = SubscriptionController.SubscriptionPutDTO(
            billingCycleId = billingCycleId,
            name = "Updated",
            description = "testing",
            amount = BigDecimal(1829.1),
            updatedById = updatedById
        )
        val id = UUID.fromString("019bd473-3fd7-456e-8f6b-4b5216afcf22")
        // When
        val updated = subscriptionServiceImpl.updateSubscription(id, subscription)
        // Then
        assertEquals("Updated", updated.name)
        assertEquals(0, updated.amount.compareTo(BigDecimal(1829.1)))
        assertEquals(updatedById, updated.updatedById)
    }

    @Test
    fun testDeleteSubscription() {
        // Then
        val id = UUID.fromString("019bd473-3fd7-456e-8f6b-4b5216afcf22")
        val count = subscriptionRepository.count()
        // When
        val subscription = subscriptionServiceImpl.deleteSubscription(id)
        // Then
        assertEquals(count - 1, subscriptionRepository.count())
        assertEquals(null, subscriptionRepository.findById(id).getOrNull())
        assertThrows<NoSuchElementException> {
            subscriptionServiceImpl.deleteSubscription(id)
        }
    }
}