package com.gms.backend.domain.application.rest.asset

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.domain.service.asset.AssetService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.PastOrPresent
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.*

@RestController
@RequestMapping("/api/asset")
@Tag(name = "Asset")
class AssetController(
    private val assetService: AssetService,
) {

    @Schema(description = "Format for Asset read")
    data class AssetTableDTO(
        val id: UUID,
        val name: String,
        val branchId: UUID,
        val assetCategoryId: UUID,
        val manufacturedDate: Instant?,
        val endOfLife: Instant?,
        val remarks: String?,
        val objectIds: List<UUID> = emptyList(),
        val createdById: UUID?,
        val updatedById: UUID?,
        val createdAt: Instant,
        val updatedAt: Instant
    )

    @Schema(description = "Format for Asset create")
    data class AssetPostDTO(
        @field: NotBlank(message = "Name must not be empty")
        val name: String,
        val branchId: UUID,
        val assetCategoryId: UUID,
        @field:PastOrPresent(message = "Manufactured date cannot be in the future")
        val manufacturedDate: Instant?,
        val endOfLife: Instant?,
        val remarks: String?,
        val objectIds: List<UUID> = emptyList(),
        val createdById: UUID
    ){
        @get:AssertTrue(message = "End of Life date must be after the Manufactured date")
        val isDateRangeValid: Boolean
            get() = if (manufacturedDate == null || endOfLife == null) true
            else endOfLife.isAfter(manufacturedDate)
    }

    @Schema(description = "Format for Asset update")
    data class AssetPutDTO(
        @field: NotBlank(message = "Name must not be empty")
        val name: String,
        val branchId: UUID,
        val assetCategoryId: UUID,
        @field:PastOrPresent(message = "Manufactured date cannot be in the future")
        val manufacturedDate: Instant?,
        val endOfLife: Instant?,
        val remarks: String?,
        val objectIds: List<UUID> = emptyList(),
        val updatedById: UUID,
    ){
        @get:AssertTrue(message = "End of Life date must be after the Manufactured date")
        val isDateRangeValid: Boolean
            get() = if (manufacturedDate == null || endOfLife == null) true
            else endOfLife.isAfter(manufacturedDate)
    }

    // Basic CRUD
    @PostMapping
    @Operation(summary = "Create an Asset")
    fun createAsset(@Valid @RequestBody body: AssetPostDTO) =
        assetService.createAsset(body).toCreatedResponse("Asset Created")

    @PutMapping("/{id}")
    @Operation(summary = "Update an Asset by ID")
    fun updateAsset(@PathVariable id: UUID, @Valid @RequestBody body: AssetPutDTO) =
        assetService.updateAsset(id, body).toOkResponse("Asset Updated")

    @GetMapping
    @Operation(summary = "Get all Assets")
    fun getAllAssets(pageable: Pageable) =
        assetService.getAssets(pageable).toPaginatedResponse()

    @GetMapping("/{id}")
    @Operation(summary = "Get an Asset by ID")
    fun getAsset(@PathVariable id: UUID) =
        assetService.getAssetById(id).toOkResponse()

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an Asset by ID")
    fun deleteAsset(@PathVariable id: UUID) =
        assetService.deleteAsset(id).toOkResponse("Asset Deleted")

    // Extended endpoints
    @GetMapping("/{id}/maintenance")
    @Operation(summary = "Get Asset Maintenance Logs by Asset ID")
    fun getAssetMaintenance(
        @PathVariable id: UUID,
        pageable: Pageable
    ) = assetService.getAssetMaintenance(id, pageable).toPaginatedResponse()

    @GetMapping("/{id}/maintenance/schedule")
    @Operation(summary = "Get Asset Maintenance Schedules by Asset ID")
    fun getAssetSchedules(
        @PathVariable id: UUID,
        pageable: Pageable
    ) = assetService.getAssetSchedules(id, pageable).toPaginatedResponse()
}
