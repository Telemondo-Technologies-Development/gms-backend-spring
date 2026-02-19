package com.gms.backend.impl.service.member.report

import com.gms.backend.domain.application.rest.member.report.ReportTypeController
import com.gms.backend.domain.domain.repository.member.report.ReportTypeRepository
import com.gms.backend.domain.impl.domain.service.member.report.ReportTypeServiceImpl
import com.gms.backend.impl.service.BaseTest
import io.nats.client.Connection
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import java.util.*
import kotlin.jvm.optionals.getOrNull

class ReportTypeServiceImplTest
@Autowired constructor(
    private val reportTypeServiceImpl: ReportTypeServiceImpl,
    private val reportTypeRepository: ReportTypeRepository,
    private val nc: Connection
) : BaseTest() {

    @Test
    fun natsConnection() {
        assertEquals(Connection.Status.CONNECTED, nc.status)
    }

    @Test
    fun testReadReportTypes() {
        // When
        val reportTypes = reportTypeServiceImpl.getReportTypes(Pageable.unpaged())

        // Then
        assertEquals(2, reportTypes.totalElements)
    }

    @Test
    fun testGetReportTypeById() {
        // Given
        val id = UUID.fromString("019c4120-d55b-7ac7-8b31-34dcf3a9e69d")

        // When
        val result = reportTypeServiceImpl.getReportTypeById(id)

        // Then
        assertNotNull(result)
        assertEquals(id, result.id)
        assertEquals("Negative", result.name)
    }

    @Test
    fun testCreateReportType() {
        // Given
        val adminId = UUID.fromString("f520a8fb-3824-4339-8bb4-3732c8a3f617")
        val body = ReportTypeController.ReportTypePostDTO(
            name = "Warning",
            createdById = adminId
        )

        // When
        val saved = reportTypeServiceImpl.createReportType(body)

        // Then
        assertNotNull(saved.id)
        assertEquals("Warning", saved.name)
        assertEquals(3, reportTypeRepository.count())
    }

    @Test
    fun testUpdateReportType() {
        // Given
        val id = UUID.fromString("019c4121-8669-78b2-a456-ba590a1c5467")
        val adminId = UUID.fromString("f520a8fb-3824-4339-8bb4-3732c8a3f617")
        val body = ReportTypeController.ReportTypePutDTO(
            name = "Updated Report Type",
            updatedById = adminId
        )

        // When
        val updated = reportTypeServiceImpl.updateReportType(id, body)

        // Then
        assertEquals("Updated Report Type", updated.name)
        assertEquals(id, updated.id)
    }

    @Test
    fun testDeleteReportType() {
        // Given
        val id = UUID.fromString("019c4121-8669-78b2-a456-ba590a1c5467")
        val initialCount = reportTypeRepository.count()

        // When
        reportTypeServiceImpl.deleteReportType(id)

        // Then
        assertEquals(initialCount - 1, reportTypeRepository.count())
        assertEquals(null, reportTypeRepository.findById(id).getOrNull())

        assertThrows<NoSuchElementException> {
            reportTypeServiceImpl.deleteReportType(id)
        }
    }
}