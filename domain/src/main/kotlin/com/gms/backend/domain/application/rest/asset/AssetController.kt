package com.gms.backend.domain.application.rest.asset

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.domain.model.asset.MaintenanceSchedule
import com.gms.backend.domain.domain.service.asset.AssetService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.util.UUID

@RestController
@RequestMapping("/api/asset")
@Tag(name = "Asset")
class AssetController (
    private val assetService: AssetService
){
    @Schema(description = "Format for Asset read")
    data class AssetTableDTO(
        val id: UUID,
        val branchId: UUID,
        val maintenanceSchedule: MaintenanceScheduleTableDTO,
        val assetCategoryId: UUID,
        val name: String,
        val manufacturedDate: Instant?,
        val endOfLife: Instant?,
        val remarks: String?,
        val createdById: UUID?,
        val updatedById: UUID?,
//        val createdAt: Instant,
//        val updatedAt: Instant
    )

    @Schema(description = "Format for Asset create")
    data class AssetPostDTO(
        val branchId: UUID,
        val maintenanceSchedule: MaintenanceSchedulePostDTO,
        val assetCategoryId: UUID,
        val name: String,
        val manufacturedDate: Instant?,
        val endOfLife: Instant?,
        val remarks: String?,
        val objectIds: List<UUID> = emptyList(),
        val createdById: UUID,
    )

    @Schema(description = "Format for Asset update")
    data class AssetPutDTO(
        val branchId: UUID,
        val maintenanceScheduleId: UUID,
        val assetCategoryId: UUID,
        val name: String,
        val manufacturedDate: Instant?,
        val endOfLife: Instant?,
        val remarks: String?,
        val objectIds: List<UUID> = emptyList(),
        val updatedById: UUID
    )

    data class MaintenanceScheduleTableDTO(
        val id: UUID,
        val startDate: Instant,
        val intervals: MaintenanceSchedule.MaintenanceScheduleIntervalUnit,
        val intervalCount: Int
    )

    @Schema(description = "Format for Maintenance Schedule create")
    data class MaintenanceSchedulePostDTO(
        val startDate: Instant,
        val intervals: MaintenanceSchedule.MaintenanceScheduleIntervalUnit,
        val intervalCount: Int
    )

    @PostMapping
    @Operation(summary = "Create a new Asset and its related Maintenance Schedule")
    fun createAsset(@RequestBody body: AssetPostDTO) =
        assetService.createAsset(body).toCreatedResponse("Asset Created")

    @PutMapping("/{id}")
    @Operation(summary = "Update a Asset by id")
    fun updateAsset(@PathVariable id: UUID, @RequestBody body: AssetPutDTO) =
        assetService.updateAsset(id, body).toOkResponse("Asset Updated")

    @GetMapping
    @Operation(summary = "Get all Assets with their related Maintenance Schedule")
    fun getAllAssets() =
        assetService.getAssets().toOkResponse()

    @GetMapping("/{id}")
    @Operation(summary = "Get a Asset with its related Maintenance Schedule by Asset id")
    fun getAsset(@PathVariable id: UUID) =
        assetService.getAssetById(id).toOkResponse()

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Asset and its related Maintenance Schedule by Asset id")
    fun deleteAsset(@PathVariable id: UUID) =
        assetService.deleteAsset(id).toOkResponse("Asset Deleted")


}
