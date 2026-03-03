package com.gms.backend.impl.service.member

import com.gms.backend.domain.application.rest.member.MemberController
import com.gms.backend.domain.domain.model.member.Member
import com.gms.backend.domain.domain.repository.member.MemberRepository
import com.gms.backend.domain.impl.domain.service.member.MemberServiceImpl
import com.gms.backend.impl.service.BaseTest
import io.nats.client.Connection
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Pageable
import java.util.*
import kotlin.jvm.optionals.getOrNull

class MemberServiceImplTest
@Autowired constructor(
    private val memberServiceImpl: MemberServiceImpl,
    private val memberRepository: MemberRepository,
    private val entityManager: EntityManager,
    private val nc: Connection
) : BaseTest() {

    @Test
    fun natsConnection() {
        assertEquals(nc.status, Connection.Status.CONNECTED)
    }

    @Test
    fun testReadMembers() {
        // Given
        // When
        val members = memberServiceImpl.getMembers(Pageable.unpaged())
        // Then
        assertEquals(3, members.size)
    }

    @Test
    fun testCreateMember() {
        // Given
        val createdById = UUID.fromString("16868948-705e-4011-9f42-bcc324c01ce0")
        val member = MemberController.MemberPostDTO(
            surname = "Ford",
            firstName = "Michaela",
            status = Member.MemberStatus.UNDECIDED,
            createdById = createdById,
            middleName = null,
            suffix = null,
            profilePictureId = null
        )
        // When
        val saved = memberServiceImpl.createMember(member)
        // Then
        assertEquals("Ford", saved.surname)
        assertEquals(Member.MemberStatus.UNDECIDED, saved.status)
        assertThrows<DataIntegrityViolationException>({
            memberServiceImpl.createMember(member)
            entityManager.flush()
        })
    }

    @Test
    fun testUpdateMembers() {
        // Given
        val updatedById = UUID.fromString("16868948-705e-4011-9f42-bcc324c01ce0")
        val member = MemberController.MemberPutDTO(
            surname = "Ford",
            firstName = "Michaela",
            status = Member.MemberStatus.UNDECIDED,
            updatedById = updatedById,
            middleName = null,
            suffix = null,
            profilePictureId = null
        )
        val id = UUID.fromString("019ba2c9-ef0f-7c2f-8483-45ca1d5a42f5")
        // When
        val updated = memberServiceImpl.updateMember(id, member)
        // Then
        assertNotNull(updated.actorId)
        assertEquals("Ford", updated.surname)
        assertEquals(Member.MemberStatus.UNDECIDED, updated.status)
    }

    @Test
    fun testDeleteMember() {
        // Then
        val id = UUID.fromString("019ba2c9-ef0f-7c2f-8483-45ca1d5a42f5")
        val count = memberRepository.count()
        // When
        val member = memberServiceImpl.deleteMember(id)
        // Then
        assertEquals(count - 1, memberRepository.count())
        assertEquals(null, memberRepository.findById(id).getOrNull())
        assertThrows<NoSuchElementException> {
            memberServiceImpl.deleteMember(id)
        }
    }
}