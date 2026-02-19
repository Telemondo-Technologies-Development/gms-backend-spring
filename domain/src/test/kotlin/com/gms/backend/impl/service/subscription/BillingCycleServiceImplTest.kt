package com.gms.backend.impl.service.subscription

import com.gms.backend.domain.application.rest.subscription.BillingCycleController
import com.gms.backend.domain.domain.model.subscription.BillingCycle
import com.gms.backend.domain.domain.repository.subscription.BillingCycleRepository
import com.gms.backend.domain.impl.domain.service.subscription.BillingCycleServiceImpl
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

class BillingCycleServiceImplTest
@Autowired constructor(
    private val billingCycleServiceImpl: BillingCycleServiceImpl,
    private val billingCycleRepository: BillingCycleRepository,
    private val entityManager: EntityManager,
    private val nc: Connection
) : BaseTest() {

    @Test
    fun natsConnection() {
        assertEquals(nc.status, Connection.Status.CONNECTED)
    }

    @Test
    fun testReadBillingCycles() {
        // Given
        // When
        val billingCycles = billingCycleServiceImpl.getBillingCycles(Pageable.unpaged())
        // Then
        assertEquals(2, billingCycles.size)
    }

    @Test
    fun testCreateBillingCycle() {
        // Given
        val createdById = UUID.fromString("16868948-705e-4011-9f42-bcc324c01ce0")
        val billingCycle = BillingCycleController.BillingCyclePostDTO(
            name = "Daily",
            intervals = BillingCycle.Interval.DAILY,
            intervalCount = 1,
            gracePeriodDays = 1,
            createdById = createdById
        )
        // When
        val saved = billingCycleServiceImpl.createBillingCycle(billingCycle)
        // Then
        assertEquals("Daily", saved.name)
        assertEquals(1, saved.intervalCount)
        assertEquals(1, saved.gracePeriodDays)
        assertThrows<DataIntegrityViolationException>({
            billingCycleServiceImpl.createBillingCycle(billingCycle)
            entityManager.flush()
        })
    }

    @Test
    fun testUpdateBillingCycles() {
        // Given
        val updatedById = UUID.fromString("16868948-705e-4011-9f42-bcc324c01ce0")
        val billingCycle = BillingCycleController.BillingCyclePutDTO(
            name = "Daily",
            intervals = BillingCycle.Interval.DAILY,
            intervalCount = 1,
            gracePeriodDays = 1,
            updatedById = updatedById
        )
        val id = UUID.fromString("019bd471-6bcc-799c-8995-3f5e7b76cabf")
        // When
        val updated = billingCycleServiceImpl.updateBillingCycle(id, billingCycle)
        // Then
        assertEquals("Daily", updated.name)
        assertEquals(1, updated.intervalCount)
        assertEquals(1, updated.gracePeriodDays)
    }

    @Test
    fun testDeleteBillingCycle() {
        // Then
        val id = UUID.fromString("019bd471-6bcc-799c-8995-3f5e7b76cabf")
        val count = billingCycleRepository.count()
        // When
        val billingCycle = billingCycleServiceImpl.deleteBillingCycle(id)
        // Then
        assertEquals(count - 1, billingCycleRepository.count())
        assertEquals(null, billingCycleRepository.findById(id).getOrNull())
        assertThrows<NoSuchElementException> {
            billingCycleServiceImpl.deleteBillingCycle(id)
        }
    }
}