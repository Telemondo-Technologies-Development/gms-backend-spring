package com.gms.backend.impl.service.member

import com.gms.backend.domain.application.rest.member.ProgressController
import com.gms.backend.domain.domain.repository.member.ProgressRepository
import com.gms.backend.domain.impl.domain.service.member.ProgressServiceImpl
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

class ProgressServiceImplTest
@Autowired constructor(
    private val progressServiceImpl: ProgressServiceImpl,
    private val progressRepository: ProgressRepository,
    private val entityManager: EntityManager,
    private val nc: Connection
) : BaseTest() {

    @Test
    fun natsConnection() {
        assertEquals(nc.status, Connection.Status.CONNECTED)
    }

    @Test
    fun testReadProgress() {
        // Given
        // When
        val progress = progressServiceImpl.getProgress(Pageable.unpaged())
        // Then
        assertEquals(3, progress.size)
    }

    @Test
    fun testCreateProgress() {
        // Given
        val createdBy = UUID.fromString("16868948-705e-4011-9f42-bcc324c01ce0")
        val progressOptionId  = UUID.fromString("019c75ed-c467-797d-a79b-938091385e88")
        val progress = ProgressController.ProgressPostDTO("What", progressOptionId, createdBy)
        // When
        val saved = progressServiceImpl.createProgress(progress)
        // Then
        assertEquals("What", saved.name)
        assertEquals(createdBy, saved.createdById)
        assertThrows<DataIntegrityViolationException>({
            progressServiceImpl.createProgress(progress)
            entityManager.flush()
        })
    }

    @Test
    fun testUpdateProgress() {
        // Given
        val updatedBy = UUID.fromString("16868948-705e-4011-9f42-bcc324c01ce0")
        val progressOptionId  = UUID.fromString("019c75ed-c467-797d-a79b-938091385e88")
        val progress = ProgressController.ProgressPutDTO("What", progressOptionId, updatedBy)
        val id = UUID.fromString("019c75ee-a5ad-7936-b391-7fd0de59a2cc")
        // When
        val updated = progressServiceImpl.updateProgress(id, progress)
        // Then
        assertEquals("What", updated.name)
        assertEquals(updatedBy, progress.updatedById)
    }

    @Test
    fun testDeleteProgress() {
        // Given
        val id = UUID.fromString("019c75ee-d938-718f-8243-ee2d7fd1443f")
        val count = progressRepository.count()
        // When
        val progress = progressServiceImpl.deleteProgress(id)
        // Then
        assertEquals(count - 1, progressRepository.count())
        assertEquals(null, progressRepository.findById(id).getOrNull())
        assertThrows<NoSuchElementException> {
            progressServiceImpl.deleteProgress(id)
        }
    }
}