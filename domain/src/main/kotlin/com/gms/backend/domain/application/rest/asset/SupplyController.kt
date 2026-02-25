package com.gms.backend.domain.application.rest.asset

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.domain.service.asset.SupplyService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.*

@RestController
@RequestMapping("/api/supply")
@Tag(name = "Supplies")
class SupplyController(
    private val supplyService: SupplyService
) {

    @Schema(description = "Format for Supply read")
    data class SupplyTableDTO(
        val id: UUID,
        val name: String,
        val description: String?,
        val quantity: Int,
        val branchId: UUID,
        val objectIds: List<UUID> = emptyList(),
        val createdById: UUID?,
        val updatedById: UUID?,
        val createdAt: Instant,
        val updatedAt: Instant
    )

    @Schema(description = "Format for Supply create")
    data class SupplyPostDTO(
        @field:NotBlank(message = "Name must not be empty")
        val name: String,
        val description: String?,
        val branchId: UUID,
        val objectIds: List<UUID> = emptyList(),
        val createdById: UUID,
    )

    @Schema(description = "Format for Supply update")
    data class SupplyPutDTO(
        @field:NotBlank(message = "Name must not be empty")
        val name: String,
        val description: String?,
        val branchId: UUID,
        val objectIds: List<UUID> = emptyList(),
        val updatedById: UUID
    )

    // Basic CRUD
    @PostMapping
    @Operation(summary = "Create a Supply")
    fun createSupply(@Valid @RequestBody body: SupplyPostDTO) =
        supplyService.createSupply(body).toCreatedResponse("Supply Created")

    @PutMapping("/{id}")
    @Operation(summary = "Update a Supply by id")
    fun updateSupply(@PathVariable id: UUID, @Valid @RequestBody body: SupplyPutDTO) =
        supplyService.updateSupply(id, body).toOkResponse("Supply Updated")

    @GetMapping
    @Operation(summary = "Get all Supplies")
    fun getAllSupplies(pageable: Pageable) =
        supplyService.getSupplies(pageable).toPaginatedResponse()
    
    @GetMapping("/{id}")
    @Operation(summary = "Get a Supply by id")
    fun getSupply(@PathVariable id: UUID) =
        supplyService.getSupplyById(id).toOkResponse()

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Supply by id")
    fun deleteSupply(@PathVariable id: UUID) =
        supplyService.deleteSupply(id).toOkResponse("Supply Deleted")

    // Extended endpoints
    @GetMapping("/{id}/log")
    @Operation(summary = "Get Supply Logs by Supply ID")
    fun getSupplyLogs(
        @PathVariable id: UUID,
        pageable: Pageable
    ) = supplyService.getSupplyLogs(id, pageable).toPaginatedResponse()

}