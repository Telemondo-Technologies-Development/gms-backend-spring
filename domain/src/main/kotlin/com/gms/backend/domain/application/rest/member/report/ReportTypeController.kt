package com.gms.backend.domain.application.rest.member.report

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.domain.service.member.report.ReportTypeService
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/report/type")
@Tag(name = "Report Type")
class ReportTypeController(
    private val reportTypeService: ReportTypeService
) {

    @Schema(description = "Format for Report Type read")
    data class ReportTypeTableDTO(
        val id: UUID,
        val name: String,
        val createdById: UUID?,
        val updatedById: UUID?
    )

    @Schema(description = "Format for ReportType create")
    data class ReportTypePostDTO(
        @field:NotBlank(message = "Name is required")
        val name: String,
        val createdById: UUID
    )

    @Schema(description = "Format for ReportType update")
    data class ReportTypePutDTO(
        @field:NotBlank(message = "Name is required")
        val name: String,
        val updatedById: UUID
    )

    @PostMapping
    fun create(@Valid @RequestBody body: ReportTypePostDTO) =
        reportTypeService.createReportType(body).toCreatedResponse("Report Type Created")

    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @Valid @RequestBody body: ReportTypePutDTO) =
        reportTypeService.updateReportType(id, body).toOkResponse("Report Type Updated")

    @GetMapping
    fun getAll(pageable: Pageable) =
        reportTypeService.getReportTypes(pageable).toPaginatedResponse()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID) =
        reportTypeService.getReportTypeById(id).toOkResponse()

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID) =
        reportTypeService.deleteReportType(id).toOkResponse("Report Type Deleted")
}