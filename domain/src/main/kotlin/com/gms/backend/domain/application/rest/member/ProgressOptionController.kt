package com.gms.backend.domain.application.rest.member

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.domain.service.member.ProgressOptionService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/member/progress-option")
@Tag(name = "Progress Option")
class ProgressOptionController(private val progressOptionService: ProgressOptionService) {

    @Schema(description = "Format for Personnel Role read")
    data class ProgressOptionTableDTO(
        val id: UUID,
        val name: String,
        val createdById: UUID?,
        val updatedById: UUID?
    )

    @Schema(description = "Format for Personnel Role create")
    data class ProgressOptionPostDTO(
        @field:NotBlank(message = "Name must not be empty")
        val name: String,
        val createdById: UUID
    )

    @Schema(description = "Format for Personnel Role update")
    data class ProgressOptionPutDTO(
        @field:NotBlank(message = "Name must not be empty")
        val name: String,
        val updatedById: UUID
    )

    @GetMapping
    @Operation(summary = "Get all Personnel Roles")
    fun getAllProgressOptions(pageable: Pageable) =
        progressOptionService.getProgressOptions(pageable).toPaginatedResponse()


    @GetMapping("/{id}")
    @Operation(summary = "Get a Personnel Role by id")
    fun getProgressOption(@PathVariable id: UUID) =
        progressOptionService.getProgressOptionById(id).toOkResponse()

    @PostMapping
    @Operation(summary = "Create a new Personnel Role")
    fun createProgressOption(@Valid @RequestBody body: ProgressOptionPostDTO) =
        progressOptionService.createProgressOption(body).toCreatedResponse()

    @Operation(summary = "Update a Personnel Role by id")
    @PutMapping("/{id}")
    fun updateProgressOption(@PathVariable id: UUID, @Valid @RequestBody body: ProgressOptionPutDTO) =
        progressOptionService.updateProgressOption(id, body).toOkResponse()

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Personnel Role by id")
    fun deleteProgressOption(@PathVariable id: UUID) =
        progressOptionService.deleteProgressOption(id).toOkResponse("Deleted Successfully")

}