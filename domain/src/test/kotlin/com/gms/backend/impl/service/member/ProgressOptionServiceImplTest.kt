package com.gms.backend.impl.service.member

import com.gms.backend.domain.application.rest.member.ProgressOptionController
import com.gms.backend.domain.domain.repository.member.ProgressOptionRepository
import com.gms.backend.domain.impl.domain.service.member.ProgressOptionServiceImpl
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

class ProgressOptionServiceImplTest
@Autowired constructor(
    private val progressOptionServiceImpl: ProgressOptionServiceImpl,
    private val progressOptionRepository: ProgressOptionRepository,
    private val entityManager: EntityManager,
    private val nc: Connection
) : BaseTest() {

    @Test
    fun natsConnection() {
        assertEquals(nc.status, Connection.Status.CONNECTED)
    }

    @Test
    fun testReadProgressOptions() {
        // Given
        // When
        val progressOptions = progressOptionServiceImpl.getProgressOptions(Pageable.unpaged())
        // Then
        assertEquals(2, progressOptions.size)
    }

    @Test
    fun testCreateProgressOption() {
        // Given
        val createdBy = UUID.fromString("16868948-705e-4011-9f42-bcc324c01ce0")
        val progressOption = ProgressOptionController.ProgressOptionPostDTO("What", createdBy)
        // When
        val saved = progressOptionServiceImpl.createProgressOption(progressOption)
        // Then
        assertEquals("What", saved.name)
        assertEquals(createdBy, saved.createdById)
        assertThrows<DataIntegrityViolationException>({
            progressOptionServiceImpl.createProgressOption(progressOption)
            entityManager.flush()
        })
    }

    @Test
    fun testUpdateProgressOptions() {
        // Given
        val updatedBy = UUID.fromString("16868948-705e-4011-9f42-bcc324c01ce0")
        val progressOption = ProgressOptionController.ProgressOptionPutDTO("What", updatedBy)
        val id = UUID.fromString("019c75ed-c467-797d-a79b-938091385e88")
        // When
        val updated = progressOptionServiceImpl.updateProgressOption(id, progressOption)
        // Then
        assertEquals("What", updated.name)
        assertEquals(updatedBy, progressOption.updatedById)
    }

    @Test
    fun testDeleteProgressOption() {
        // Given
        val id = UUID.fromString("019c75ed-c467-797d-a79b-938091385e88")
        val count = progressOptionRepository.count()
        // When
        val progressOption = progressOptionServiceImpl.deleteProgressOption(id)
        // Then
        assertEquals(count - 1, progressOptionRepository.count())
        assertEquals(null, progressOptionRepository.findById(id).getOrNull())
        assertThrows<NoSuchElementException> {
            progressOptionServiceImpl.deleteProgressOption(id)
        }
    }
}