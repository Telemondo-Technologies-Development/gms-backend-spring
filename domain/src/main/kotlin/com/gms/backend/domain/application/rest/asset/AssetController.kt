package com.gms.backend.domain.application.rest.asset

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.domain.model.asset.Asset
import com.gms.backend.domain.domain.model.asset.AssetMaintenance
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
import java.math.BigDecimal
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
        val acquisitionDate: Instant?,
        val condition: Asset.AssetCondition,
        val status: Asset.AssetStatus,
        val remarks: String?,
        val createdById: UUID?,
        val updatedById: UUID?,
        val createdAt: Instant,
        val updatedAt: Instant,
    ){
        var expenses: List<BigDecimal> = emptyList()
        var brandIds: List<UUID> = emptyList()
        var objectIds: List<UUID> = emptyList()
    }

    @Schema(description = "Format for summarized Asset read")
    data class AssetSummaryDTO(
        val id: UUID,
        val name: String,
        val branchId: UUID,
        val assetCategoryId: UUID,
        val manufacturedDate: Instant?,
        val endOfLife: Instant?,
        val acquisitionDate: Instant?,
        val condition: Asset.AssetCondition,
        val status: Asset.AssetStatus,
        val remarks: String?,
        val createdById: UUID?,
        val updatedById: UUID?,
        val createdAt: Instant,
        val updatedAt: Instant,
    )

    data class AssetMappingDTO(
        val assetId: UUID,
        val relatedId: UUID,
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
        val acquisitionDate: Instant?,
        val condition: Asset.AssetCondition,
        val status: Asset.AssetStatus,
        val remarks: String?,
        val brandIds: List<UUID> = emptyList(),
        val objectIds: List<UUID> = emptyList(),
        val createdById: UUID
    ){
        @get:AssertTrue(message = "End of Life date must be after the Manufactured date")
        val isEndOfLifeValid: Boolean
            get() {
                if (manufacturedDate == null || endOfLife == null) return true
                return endOfLife!!.isAfter(manufacturedDate)
            }

        @get:AssertTrue(message = "Acquisition date must be on or after the manufactured date and before the end of life")
        val isAcquisitionDateValid: Boolean
            get() {
                val currentAcquisition = acquisitionDate ?: return true
                val isAfterManufactured = manufacturedDate?.let { !currentAcquisition.isBefore(it) } ?: true
                val isBeforeEndOfLife = endOfLife?.let { currentAcquisition.isBefore(it) } ?: true

                return isAfterManufactured && isBeforeEndOfLife
            }
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
        val acquisitionDate: Instant?,
        val condition: Asset.AssetCondition,
        val status: Asset.AssetStatus,
        val remarks: String?,
        val brandIds: List<UUID> = emptyList(),
        val objectIds: List<UUID> = emptyList(),
        val updatedById: UUID,
    ){
        @get:AssertTrue(message = "End of Life date must be after the Manufactured date")
        val isEndOfLifeValid: Boolean
            get() {
                if (manufacturedDate == null || endOfLife == null) return true
                return endOfLife!!.isAfter(manufacturedDate)
            }

        @get:AssertTrue(message = "Acquisition date must be on or after the manufactured date and before the end of life")
        val isAcquisitionDateValid: Boolean
            get() {
                val currentAcquisition = acquisitionDate ?: return true
                val isAfterManufactured = manufacturedDate?.let { !currentAcquisition.isBefore(it) } ?: true
                val isBeforeEndOfLife = endOfLife?.let { currentAcquisition.isBefore(it) } ?: true

                return isAfterManufactured && isBeforeEndOfLife
            }
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
    fun getAllAssets(
        pageable: Pageable,
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) branchId: UUID?,
        @RequestParam(required = false) categoryId: UUID?,
        @RequestParam(required = false) status: Asset.AssetStatus?,
        @RequestParam(required = false) condition: Asset.AssetCondition?,
        @RequestParam(required = false) dateFrom: Instant?,
        @RequestParam(required = false) dateTo: Instant?
    ) = assetService.getAssets(
        pageable, name, branchId, categoryId, condition, status, dateFrom, dateTo
    ).toPaginatedResponse()

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
