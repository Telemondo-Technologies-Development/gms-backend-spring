package com.gms.backend.domain.application.rest.member

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.domain.service.member.ProgressService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/member/progress-option/progress")
@Tag(name = "Progress")
class ProgressController(private val progressService: ProgressService) {

    @Schema(description = "Format for Personnel Role read")
    data class ProgressTableDTO(
        val id: UUID,
        val name: String,
        val progressOptionId: UUID?,
        val createdById: UUID?,
        val updatedById: UUID?
    )

    @Schema(description = "Format for Personnel Role create")
    data class ProgressPostDTO(
        @field:NotBlank(message = "Name must not be empty")
        val name: String,
        val progressOptionId: UUID,
        val createdById: UUID
    )

    @Schema(description = "Format for Personnel Role update")
    data class ProgressPutDTO(
        @field:NotBlank(message = "Name must not be empty")
        val name: String,
        val progressOptionId: UUID,
        val updatedById: UUID
    )

    @GetMapping
    @Operation(summary = "Get all Personnel Roles")
    fun getAllProgress(pageable: Pageable) = progressService.getProgress(pageable).toPaginatedResponse()


    @GetMapping("/{id}")
    @Operation(summary = "Get a Personnel Role by id")
    fun getProgress(@PathVariable id: UUID) =
        progressService.getProgressById(id).toOkResponse()

    @PostMapping
    @Operation(summary = "Create a new Personnel Role")
    fun createProgress(@Valid @RequestBody body: ProgressPostDTO) =
        progressService.createProgress(body).toCreatedResponse()

    @Operation(summary = "Update a Personnel Role by id")
    @PutMapping("/{id}")
    fun updateProgress(@PathVariable id: UUID, @Valid @RequestBody body: ProgressPutDTO) =
        progressService.updateProgress(id, body).toOkResponse()

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Personnel Role by id")
    fun deleteProgress(@PathVariable id: UUID) =
        progressService.deleteProgress(id).toOkResponse("Deleted Successfully")

}