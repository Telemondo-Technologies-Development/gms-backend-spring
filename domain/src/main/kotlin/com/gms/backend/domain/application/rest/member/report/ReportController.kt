package com.gms.backend.domain.application.rest.member.report

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.impl.domain.service.member.report.ReportServiceImpl
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.*

@RestController
@RequestMapping("/api/report")
@Tag(name = "Report")
class ReportController(private val reportService: ReportServiceImpl) {

    @Schema(description = "Format for Report read")
    data class ReportTableDTO(
        val id: UUID,
        val branchId: UUID,
        val actorId: UUID,
        val reportTypeId: UUID,
        val description: String?,
        val occurredAt: Instant?,
        val objectIds: List<UUID> = emptyList(),
        val createdById: UUID?,
        val updatedById: UUID?
    )

    @Schema(description = "Format for Report create")
    data class ReportPostDTO(
        val branchId: UUID,
        val actorId: UUID,
        val reportTypeId: UUID,
        val description: String?,
        val occurredAt: Instant?,
        val objectIds: List<UUID> = emptyList(),
        val createdById: UUID
    )

    @Schema(description = "Format for Report update")
    data class ReportPutDTO(
        val branchId: UUID,
        val actorId: UUID,
        val reportTypeId: UUID,
        val description: String?,
        val occurredAt: Instant?,
        val objectIds: List<UUID> = emptyList(),
        val updatedById: UUID
    )

    @PostMapping
    fun createReport(@Valid @RequestBody body: ReportPostDTO) =
        reportService.createReport(body).toCreatedResponse("Report Created")

    @PutMapping("/{id}")
    fun updateReport(@PathVariable id: UUID, @Valid @RequestBody body: ReportPutDTO) =
        reportService.updateReport(id, body).toOkResponse("Report Updated")

    @GetMapping
    fun getAllReports(pageable: Pageable) = reportService.getReports(pageable).toPaginatedResponse()

    @GetMapping("/{id}")
    fun getReport(@PathVariable id: UUID) = reportService.getReportById(id).toOkResponse()

    @DeleteMapping("/{id}")
    fun deleteReport(@PathVariable id: UUID) = reportService.deleteReport(id).toOkResponse("Report Deleted")
}