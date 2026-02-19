package com.gms.backend.impl.service.subscription

import com.gms.backend.domain.application.rest.subscription.SubscriptionAvailedController
import com.gms.backend.domain.domain.repository.subscription.SubscriptionAvailedRepository
import com.gms.backend.domain.impl.domain.service.subscription.SubscriptionAvailedServiceImpl
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

class SubscriptionAvailedServiceImplTest
@Autowired constructor(
    private val subscriptionAvailedServiceImpl: SubscriptionAvailedServiceImpl,
    private val subscriptionAvailedRepository: SubscriptionAvailedRepository,
    private val entityManager: EntityManager,
    private val nc: Connection
) : BaseTest() {

    @Test
    fun natsConnection() {
        assertEquals(nc.status, Connection.Status.CONNECTED)
    }

    @Test
    fun testReadSubscriptionAvailed() {
        // Given
        // When
        val subscriptionAvailed = subscriptionAvailedServiceImpl.getSubscriptionAvailed(Pageable.unpaged())
        // Then
        assertEquals(2, subscriptionAvailed.size)
    }

    @Test
    fun testCreateSubscriptionAvailed() {
        // Given
        val subscriptionId = UUID.fromString("019bd473-3fd7-456e-8f6b-4b5216afcf22")
        val subscriptionAvailed = SubscriptionAvailedController.SubscriptionAvailedPostDTO(
            subscriptionId = subscriptionId
        )
        // When
        val saved = subscriptionAvailedServiceImpl.createSubscriptionAvailed(subscriptionAvailed)
        // Then
        assertEquals("Test", saved.name)
        assertThrows<DataIntegrityViolationException>({
            subscriptionAvailedServiceImpl.createSubscriptionAvailed(subscriptionAvailed)
            entityManager.flush()
        })
    }

    @Test
    fun testDeleteSubscriptionAvailed() {
        // Then
        val id = UUID.fromString("019c6fd8-694b-7864-9177-ae92006e476f")
        val count = subscriptionAvailedRepository.count()
        // When
        val subscriptionAvailed = subscriptionAvailedServiceImpl.deleteSubscriptionAvailed(id)
        // Then
        assertEquals(count - 1, subscriptionAvailedRepository.count())
        assertEquals(null, subscriptionAvailedRepository.findById(id).getOrNull())
        assertThrows<NoSuchElementException> {
            subscriptionAvailedServiceImpl.deleteSubscriptionAvailed(id)
        }
    }
}