package com.gms.backend.impl.service.member

import com.gms.backend.domain.application.rest.member.MemberSubscriptionController
import com.gms.backend.domain.domain.model.member.MemberSubscription
import com.gms.backend.domain.domain.repository.member.MemberSubscriptionRepository
import com.gms.backend.domain.impl.domain.service.member.MemberSubscriptionServiceImpl
import com.gms.backend.impl.service.BaseTest
import io.nats.client.Connection
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.jvm.optionals.getOrNull

class MemberSubscriptionServiceImplTest
@Autowired constructor(
    private val memberSubscriptionServiceImpl: MemberSubscriptionServiceImpl,
    private val memberSubscriptionRepository: MemberSubscriptionRepository,
    private val entityManager: EntityManager,
    private val nc: Connection
) : BaseTest() {

    @Test
    fun natsConnection() {
        assertEquals(nc.status, Connection.Status.CONNECTED)
    }

    @Test
    fun testReadMemberSubscriptions() {
        // Given
        // When
        val memberMemberSubscriptions = memberSubscriptionServiceImpl.getMemberSubscriptions(Pageable.unpaged())
        // Then
        assertEquals(2, memberMemberSubscriptions.size)
    }

    @Test
    fun testCreateMemberSubscription() {
        // Given
        val actorId = UUID.fromString("d594bb11-21f4-4511-a3fb-184b9b557410")
        val subscriptionId = UUID.fromString("019bd473-3fd7-7ffe-8f6b-4b5216aecc62")
        val branchId = UUID.fromString("019ba279-a6e6-7271-893c-ffab220040a2")
        val createdById = UUID.fromString("16868948-705e-4011-9f42-bcc324c01ce0")
        val memberSubscription = MemberSubscriptionController.MemberSubscriptionPostDTO(
            actorId = actorId,
            subscriptionId = subscriptionId,
            branchId = branchId,
            startDate = Instant.now(),
            endDate = Instant.now().plus(5, ChronoUnit.DAYS),
            status = MemberSubscription.MemberSubscriptionStatus.ACTIVE,
            createdById = createdById
        )
        // When
        val saved = memberSubscriptionServiceImpl.createMemberSubscription(memberSubscription)
        // Then
        assertEquals(createdById, saved.createdById)
    }

    @Test
    fun testUpdateMemberSubscriptions() {
        // Given
        val actorId = UUID.fromString("d594bb11-21f4-4511-a3fb-184b9b557410")
        val subscriptionId = UUID.fromString("019bd473-3fd7-7ffe-8f6b-4b5216aecc62")
        val branchId = UUID.fromString("019ba279-a6e6-7271-893c-ffab220040a2")
        val updatedById = UUID.fromString("16868948-705e-4011-9f42-bcc324c01ce0")
        val memberSubscription = MemberSubscriptionController.MemberSubscriptionPutDTO(
            actorId = actorId,
            updateCurrentSubscription = true,
            subscriptionId = subscriptionId,
            branchId = branchId,
            startDate = Instant.now(),
            endDate = Instant.now().plus(5, ChronoUnit.DAYS),
            status = MemberSubscription.MemberSubscriptionStatus.ACTIVE,
            updatedById = updatedById
        )
        val id = UUID.fromString("019bd47a-eacd-7d47-8140-f50da33f4569")
        // When
        val updated = memberSubscriptionServiceImpl.updateMemberSubscription(id, memberSubscription)
        // Then
        assertEquals(updatedById, updated.updatedById)
    }

    @Test
    fun testDeleteMemberSubscription() {
        // Then
        val id = UUID.fromString("019bd47a-eacd-7d47-8140-f50da33f4569")
        val count = memberSubscriptionRepository.count()
        // When
        val memberMemberSubscription = memberSubscriptionServiceImpl.deleteMemberSubscription(id)
        // Then
        assertEquals(count - 1, memberSubscriptionRepository.count())
        assertEquals(null, memberSubscriptionRepository.findById(id).getOrNull())
        assertThrows<NoSuchElementException> {
            memberSubscriptionServiceImpl.deleteMemberSubscription(id)
        }
    }
}