package com.gms.backend.domain.application.rest.asset

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.domain.service.asset.AssetService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.NotBlank
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
        val updatedById: UUID?
    )

    data class AssetPostDTO(
        @field:NotBlank(message = "Asset name must not be empty")
        val name: String,
        val branchId: UUID,
        val assetCategoryId: UUID,
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

    data class AssetPutDTO(
        @field:NotBlank(message = "Asset name must not be empty")
        val name: String,
        val branchId: UUID,
        val assetCategoryId: UUID,
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

    @PostMapping
    @Operation(summary = "Create an asset")
    fun createAsset(@Valid @RequestBody body: AssetPostDTO) =
        assetService.createAsset(body).toCreatedResponse("Asset Created")

    @PutMapping("/{id}")
    @Operation(summary = "Update an asset by ID")
    fun updateAsset(@PathVariable id: UUID, @Valid @RequestBody body: AssetPutDTO) =
        assetService.updateAsset(id, body).toOkResponse("Asset Updated")

    @GetMapping
    @Operation(summary = "Get all assets")
    fun getAllAssets(pageable: Pageable) =
        assetService.getAssets(pageable).toPaginatedResponse()

    @GetMapping("/{id}")
    @Operation(summary = "Get an asset by ID")
    fun getAsset(@PathVariable id: UUID) =
        assetService.getAssetById(id).toOkResponse()

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an asset by ID")
    fun deleteAsset(@PathVariable id: UUID) =
        assetService.deleteAsset(id).toOkResponse("Asset Deleted")
}
