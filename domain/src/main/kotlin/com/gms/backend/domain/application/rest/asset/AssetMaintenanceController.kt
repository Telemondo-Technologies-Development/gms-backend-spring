package com.gms.backend.domain.application.rest.asset

import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.domain.model.asset.AssetMaintenance
import com.gms.backend.domain.domain.service.asset.AssetMaintenanceService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.*

@RestController
@RequestMapping("/api/asset/maintenance")
@Tag(name = "Asset Maintenance")
class AssetMaintenanceController(
    private val maintenanceService: AssetMaintenanceService,
) {

    @Schema(description = "Format for Asset Maintenance Log read")
    data class AssetMaintenanceTableDTO(
        val id: UUID,
        val assetId: UUID?,
        val maintenanceScheduleId: UUID?,
        val maintenanceDate: Instant,
        val completionDate: Instant?,
        val dueDate: Instant,
        val status: String,
        val description: String?,
        val objectIds: List<UUID> = emptyList(),
        val createdById: UUID?,
        val updatedById: UUID?,
        val createdAt: Instant,
        val updatedAt: Instant
    )

    @Schema(description = "Format for Asset Maintenance Log patch")
    data class AssetMaintenancePatchDTO(
        val status: AssetMaintenance.AssetMaintenanceStatus,
        val description: String?,
        val completionDate: Instant?,
        val objectIds: List<UUID> = emptyList(),
        val updatedById: UUID
    )

    // Basic CRUD
    @GetMapping
    @Operation(summary = "Get all Asset Maintenances")
    fun getAllMaintenance(pageable: Pageable) =
        maintenanceService.getMaintenanceLogs(pageable).toPaginatedResponse()

    @GetMapping("/{id}")
    @Operation(summary = "Get an Asset Maintenance by id")
    fun getMaintenanceById(@PathVariable id: UUID) =
        maintenanceService.getMaintenanceLogById(id).toOkResponse()

    @PatchMapping("/{id}")
    @Operation(summary = "Update an Asset Maintenance status, description, files, and completion date by id")
    fun updateMaintenanceStatus(
        @PathVariable id: UUID,
        @RequestBody request: AssetMaintenancePatchDTO
    ) = maintenanceService.updateMaintenanceStatus(id, request).toOkResponse()
}