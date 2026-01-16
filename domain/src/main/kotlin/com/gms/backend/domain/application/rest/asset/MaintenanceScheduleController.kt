package com.gms.backend.domain.application.rest.asset

import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.domain.model.asset.MaintenanceSchedule
import com.gms.backend.domain.domain.service.asset.MaintenanceScheduleService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import java.time.Instant

@RestController
@RequestMapping("/api/asset")
@Tag(name = "Maintenance Schedule")
class MaintenanceScheduleController(
    private val maintenanceScheduleService: MaintenanceScheduleService
) {

    @Schema(description = "Format for Maintenance Schedule read")
    data class MaintenanceScheduleTableDTO(
        val id: UUID,
        val startDate: Instant,
        val intervals: MaintenanceSchedule.MaintenanceScheduleIntervalUnit,
        val intervalCount: Int
    )

    @Schema(description = "Format for Maintenance Schedule update by asset id")
    data class MaintenanceSchedulePutDTO(
        val startDate: Instant,
        val intervals: MaintenanceSchedule.MaintenanceScheduleIntervalUnit,
        val intervalCount: Int,
    )

    @PutMapping("/{assetId}/maintenance-schedule")
    @Operation(summary = "Update Maintenance Schedule by Asset id")
    fun updateMaintenanceSchedule(
        @PathVariable assetId: UUID,
        @RequestBody body: MaintenanceSchedulePutDTO
    ) = maintenanceScheduleService
        .updateMaintenanceSchedule(assetId, body)
        .toOkResponse("Maintenance Schedule Updated")
}
