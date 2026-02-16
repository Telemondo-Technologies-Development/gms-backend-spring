package com.gms.backend.domain.application.rest.asset

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.domain.service.asset.SuppliesLogService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.*

@RestController
@RequestMapping("/api/supply/log")
@Tag(name = "Supplies Logs")
class SuppliesLogController(
    private val suppliesLogService: SuppliesLogService
) {

    @Schema(description = "Format for Supplies Log read")
    data class SuppliesLogTableDTO(
        val id: UUID,
        val name: String,
        val quantity: Int,
        val remarks: String?,
        val suppliesId: UUID,
        val objectIds: List<UUID> = emptyList(),
        val createdById: UUID?,
        val updatedById: UUID?,
        val createdAt: Instant,
        val updatedAt: Instant
    )

    @Schema(description = "Format for Supplies Log create")
    data class SuppliesLogPostDTO(
        @field:NotBlank(message = "Name must not be empty")
        val name: String,
        @field:NotNull(message = "Quantity is required")
        val quantity: Int,
        val remarks: String?,
        val suppliesId: UUID,
        val objectIds: List<UUID> = emptyList(),
        val createdById: UUID,
    ){
        @AssertTrue(message = "Quantity cannot be zero")
        fun isQuantityValid(): Boolean = quantity != 0
    }

    @Schema(description = "Format for Supplies Log update")
    data class SuppliesLogPutDTO(
        @field:NotBlank(message = "Name must not be empty")
        val name: String,
        @field:NotNull(message = "Quantity is required")
        val quantity: Int,
        val remarks: String?,
        val suppliesId: UUID,
        val objectIds: List<UUID> = emptyList(),
        val updatedById: UUID
    ){
        @AssertTrue(message = "Quantity cannot be zero")
        fun isQuantityValid(): Boolean = quantity != 0
    }

    // Basic CRUD
    @PostMapping
    @Operation(summary = "Create a Supplies Log")
    fun createSuppliesLog(@Valid @RequestBody body: SuppliesLogPostDTO) =
        suppliesLogService.createSuppliesLog(body).toCreatedResponse("Supplies Log Created")

    @PutMapping("/{id}")
    @Operation(summary = "Update a Supplies Log by id")
    fun updateSupply(@PathVariable id: UUID, @Valid @RequestBody body: SuppliesLogPutDTO) =
        suppliesLogService.updateSuppliesLog(id, body).toOkResponse("Supply Updated")

    @GetMapping
    @Operation(summary = "Get all Supplies Logs")
    fun getAllSuppliesLogs(pageable: Pageable) =
        suppliesLogService.getSuppliesLogs(pageable).toPaginatedResponse()

    @GetMapping("/{id}")
    @Operation(summary = "Get a Supplies Log by id")
    fun getSuppliesLog(@PathVariable id: UUID) =
        suppliesLogService.getSuppliesLogById(id).toOkResponse()

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Supplies Log by id")
    fun deleteSuppliesLog(@PathVariable id: UUID) =
        suppliesLogService.deleteSuppliesLog(id).toOkResponse("Supplies Log Deleted")
}