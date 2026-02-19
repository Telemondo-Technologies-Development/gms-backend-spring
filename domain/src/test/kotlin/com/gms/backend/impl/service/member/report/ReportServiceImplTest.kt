package com.gms.backend.impl.service.member.report

import com.gms.backend.domain.application.rest.member.report.ReportController
import com.gms.backend.domain.domain.repository.member.report.ReportRepository
import com.gms.backend.domain.impl.domain.service.member.report.ReportServiceImpl
import com.gms.backend.impl.service.BaseTest
import io.nats.client.Connection
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import java.time.Instant
import java.util.*
import kotlin.jvm.optionals.getOrNull

class ReportServiceImplTest
@Autowired constructor(
    private val reportServiceImpl: ReportServiceImpl,
    private val reportRepository: ReportRepository,
    private val nc: Connection
) : BaseTest() {

    @Test
    fun natsConnection() {
        assertEquals(Connection.Status.CONNECTED, nc.status)
    }

    @Test
    fun testReadReports() {
        // When
        val reports = reportServiceImpl.getReports(Pageable.unpaged())

        // Then
        assertEquals(1, reports.totalElements)
    }

    @Test
    fun testGetReportById() {
        val id = UUID.fromString("019c412a-055a-78f2-b2a8-5cc652587123")
        val result = reportServiceImpl.getReportById(id)

        assertNotNull(result)
        assertEquals(id, result.id)
        assertEquals("Member intentionally threw a dumbbell at a mirror", result.description)
    }

    @Test
    fun testCreateReport() {
        // Given
        val branchId = UUID.fromString("019ba279-a6e6-7271-893c-ffab220040a2")
        val memberId = UUID.fromString("8140f50d-a33f-4569-b76a-20c348b77222")
        val typeId = UUID.fromString("019c4121-8669-78b2-a456-ba590a1c5467")
        val adminId = UUID.fromString("f520a8fb-3824-4339-8bb4-3732c8a3f617")

        val reportDTO = ReportController.ReportPostDTO(
            branchId = branchId,
            actorId = memberId,
            reportTypeId = typeId,
            description = "Positive Report",
            occurredAt = Instant.now(),
            objectIds = emptyList(),
            createdById = adminId
        )

        // When
        val saved = reportServiceImpl.createReport(reportDTO)

        // Then
        assertNotNull(saved.id)
        assertEquals("Positive Report", saved.description)
        assertEquals(2, reportRepository.count())
    }

    @Test
    fun testUpdateReport() {
        // Given
        val id = UUID.fromString("019c412a-055a-78f2-b2a8-5cc652587123")
        val branchId = UUID.fromString("019ba279-a6e6-7271-893c-ffab220040a2")
        val memberId = UUID.fromString("8140f50d-a33f-4569-b76a-20c348b77222")
        val typeId = UUID.fromString("019c4120-d55b-7ac7-8b31-34dcf3a9e69d")
        val adminId = UUID.fromString("f520a8fb-3824-4339-8bb4-3732c8a3f617")

        val reportPutDTO = ReportController.ReportPutDTO(
            branchId = branchId,
            actorId = memberId,
            reportTypeId = typeId,
            description = "Updated Report",
            occurredAt = Instant.now(),
            objectIds = emptyList(),
            updatedById = adminId
        )

        // When
        val updated = reportServiceImpl.updateReport(id, reportPutDTO)

        // Then
        assertEquals("Updated Report", updated.description)
        assertEquals(id, updated.id)
    }

    @Test
    fun testDeleteReport() {
        // Given
        val id = UUID.fromString("019c412a-055a-78f2-b2a8-5cc652587123")
        val initialCount = reportRepository.count()

        // When
        reportServiceImpl.deleteReport(id)

        // Then
        assertEquals(initialCount - 1, reportRepository.count())
        assertEquals(null, reportRepository.findById(id).getOrNull())
        assertThrows<NoSuchElementException> {
            reportServiceImpl.deleteReport(id)
        }
    }
}