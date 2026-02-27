package com.gms.backend.impl.service.member

import com.gms.backend.domain.application.rest.member.MemberProgressController
import com.gms.backend.domain.domain.model.member.MemberProgress
import com.gms.backend.domain.domain.repository.member.MemberProgressRepository
import com.gms.backend.domain.impl.domain.service.member.MemberProgressServiceImpl
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

class MemberProgressServiceImplTest
@Autowired constructor(
    private val memberProgressServiceImpl: MemberProgressServiceImpl,
    private val memberProgressRepository: MemberProgressRepository,
    private val entityManager: EntityManager,
    private val nc: Connection
) : BaseTest() {

    @Test
    fun natsConnection() {
        assertEquals(nc.status, Connection.Status.CONNECTED)
    }

    @Test
    fun testReadMemberProgress() {
        // Given
        // When
        val memberProgress = memberProgressServiceImpl.getMemberProgress(Pageable.unpaged())
        // Then
        assertEquals(2, memberProgress.size)
    }

    @Test
    fun testCreateMemberProgress() {
        // Given
        val actorId = UUID.fromString("d594bb11-21f4-4511-a3fb-184b9b557410")
        val branchId = UUID.fromString("019ba279-a6e6-7271-893c-ffab220040a2")
        val createdById = UUID.fromString("16868948-705e-4011-9f42-bcc324c01ce0")
        val progressOptionId  = UUID.fromString("019c75ed-c467-797d-a79b-938091385e88")
        val progressId = UUID.fromString("019c75ee-a5ad-7936-b391-7fd0de59a2cc")
        val memberProgress = MemberProgressController.MemberProgressPostDTO(
            actorId = actorId,
            progressOptionId = progressOptionId,
            progressId = progressId,
            branchId = branchId,
            remarks = null,
            status = MemberProgress.MemberProgressStatus.IN,
            createdById = createdById
        )
        // When
        val saved = memberProgressServiceImpl.createMemberProgress(memberProgress)
        // Then
        assertEquals(createdById, saved.createdById)
        assertThrows<DataIntegrityViolationException>({
            memberProgressServiceImpl.createMemberProgress(memberProgress)
            entityManager.flush()
        })
    }

    @Test
    fun testUpdateMemberProgress() {
        // Given
        val updatedById = UUID.fromString("16868948-705e-4011-9f42-bcc324c01ce0")
        val progressId = UUID.fromString("019c75ee-a5ad-7936-b391-7fd0de59a2cc")
        val memberProgress = MemberProgressController.MemberProgressPutDTO(
            progressId = progressId,
            remarks = null,
            status = MemberProgress.MemberProgressStatus.IN,
            updatedById = updatedById
        )
        val id = UUID.fromString("019c7648-9e29-7f86-b68c-3557330445ea")
        // When
        val updated = memberProgressServiceImpl.updateMemberProgress(id, memberProgress)
        // Then
        assertEquals(updatedById, updated.updatedById)
    }

    @Test
    fun testDeleteMemberProgress() {
        // Given
        val id = UUID.fromString("c4f0f20d-4fc8-420b-bab8-dfc0b4c00ffc")
        val count = memberProgressRepository.count()
        // When
        val memberProgress = memberProgressServiceImpl.deleteMemberProgress(id)
        // Then
        assertEquals(count - 1, memberProgressRepository.count())
        assertEquals(null, memberProgressRepository.findById(id).getOrNull())
        assertThrows<NoSuchElementException> {
            memberProgressServiceImpl.deleteMemberProgress(id)
        }
    }
}