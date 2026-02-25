package com.gms.backend.domain.application.rest.asset

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.domain.service.asset.SupplyService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.NotBlank
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
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
    )

    @Schema(description = "Format for Supply create")
    data class SupplyPostDTO(
        @field:NotBlank(message = "Name is required")
        val name: String,
        val description: String?,
        val branchId: UUID,
        val objectIds: List<UUID> = emptyList(),
        val createdById: UUID,
    )

    @Schema(description = "Format for Supply update")
    data class SupplyPutDTO(
        @field:NotBlank(message = "Name is required")
        val name: String,
        val description: String?,
        val branchId: UUID,
        val objectIds: List<UUID> = emptyList(),
        val updatedById: UUID
    )

    @Schema(description = "Wrapper for Supply and its Logs")
    data class SupplyWithLogsDTO(
        val supply: SupplyTableDTO,
        val logs: List<SuppliesLogController.SuppliesLogTableDTO>?
    )

    @PostMapping
    @Operation(summary = "Create a new Supply")
    fun createSupply(@RequestBody body: SupplyPostDTO) =
        supplyService.createSupply(body).toCreatedResponse("Supply Created")

    @PutMapping("/{id}")
    @Operation(summary = "Update a Supply by id")
    fun updateSupply(@PathVariable id: UUID, @RequestBody body: SupplyPutDTO) =
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

    @GetMapping("/{id}/log")
    @Operation(summary = "Get supply info and its logs")
    fun getSupplyLogs(
        @PathVariable id: UUID,
        pageable: Pageable
    ) = supplyService.getSupplyLogs(id, pageable).toOkResponse()

}