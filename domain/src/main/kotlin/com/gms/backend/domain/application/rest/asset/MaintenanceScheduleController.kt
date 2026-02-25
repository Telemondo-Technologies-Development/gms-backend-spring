package com.gms.backend.domain.application.rest.asset

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.domain.service.asset.MaintenanceScheduleService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.*
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.*

@RestController
@RequestMapping("/api/asset/maintenance/schedule")
@Tag(name = "Maintenance Schedule")
class MaintenanceScheduleController(private val scheduleService: MaintenanceScheduleService) {

    data class ScheduleTableDTO(
        val id: UUID,
        val assetId: UUID,
        val name: String,
        val startDate: Instant,
        val intervalUnit: java.time.temporal.ChronoUnit,
        val intervalValue: Int,
        val leadTimeHours: Int,
        val timeToCompleteHours: Int,
        val weekRank: Int?,
        val dayOfWeek: Int?,
        val monthOfYear: Int?,
        val active: Boolean,
        val createdById: UUID?
    )

    data class SchedulePostDTO(
        @field:NotBlank(message = "Schedule name is required")
        val name: String,
        @field:NotNull(message = "Start date is required")
        @field:FutureOrPresent(message = "Start date cannot be in the past")
        val startDate: Instant,
        val intervalUnit: java.time.temporal.ChronoUnit,
        @field:Positive(message = "Interval value must be greater than zero")
        val intervalValue: Int,
        @field:PositiveOrZero(message = "Lead time cannot be negative")
        val leadTimeHours: Int,
        @field:PositiveOrZero(message = "Time to complete cannot be negative")
        val timeToCompleteHours: Int,
        @field:Min(-1) @field:Max(5)
        val weekRank: Int?,
        @field:Min(1) @field:Max(7)
        val dayOfWeek: Int?,
        @field:Min(1) @field:Max(12)
        val monthOfYear: Int?,
        val assetId: UUID,
        val createdById: UUID
    ){
        @get:AssertTrue(message = "Advanced settings require a MONTHS or YEARS interval unit")
        val isAdvancedSettingsAllowed: Boolean
            get() {
                val isAdvanced = weekRank != null || dayOfWeek != null || monthOfYear != null
                val isAllowedUnit = intervalUnit == java.time.temporal.ChronoUnit.MONTHS ||
                        intervalUnit == java.time.temporal.ChronoUnit.YEARS

                // If they provided advanced fields, the unit MUST be MONTH or YEAR
                return if (isAdvanced) isAllowedUnit else true
            }
    }

    data class SchedulePutDTO(
        @field:NotBlank(message = "Schedule name is required")
        val name: String,
        @field:NotNull(message = "Start date is required")
        val startDate: Instant,
        val intervalUnit: java.time.temporal.ChronoUnit,
        @field:Positive(message = "Interval value must be greater than zero")
        val intervalValue: Int,
        @field:PositiveOrZero(message = "Lead time cannot be negative")
        val leadTimeHours: Int,
        @field:PositiveOrZero(message = "Time to complete cannot be negative")
        val timeToCompleteHours: Int,
        @field:Min(-1) @field:Max(5)
        val weekRank: Int?,
        @field:Min(1) @field:Max(7)
        val dayOfWeek: Int?,
        @field:Min(1) @field:Max(12)
        val monthOfYear: Int?,
        val active: Boolean,
        val updatedById: UUID
    ){
        @get:AssertTrue(message = "Advanced settings require a MONTHS or YEARS interval unit")
        val isAdvancedSettingsAllowed: Boolean
            get() {
                val isAdvanced = weekRank != null || dayOfWeek != null || monthOfYear != null
                val isAllowedUnit = intervalUnit == java.time.temporal.ChronoUnit.MONTHS ||
                        intervalUnit == java.time.temporal.ChronoUnit.YEARS

                // If they provided advanced fields, the unit MUST be MONTH or YEAR
                return if (isAdvanced) isAllowedUnit else true
            }
    }

    @PostMapping
    fun createSchedule(@Valid @RequestBody body: SchedulePostDTO) =
        scheduleService.createSchedule(body).toCreatedResponse("Schedule Created")

    @PutMapping("/{id}")
    fun updateSchedule(@PathVariable id: UUID, @Valid @RequestBody body: SchedulePutDTO) =
        scheduleService.updateSchedule(id, body).toOkResponse("Schedule Updated")

    @GetMapping
    fun getAllSchedules(pageable: Pageable) =
        scheduleService.getSchedules(pageable).toPaginatedResponse()

    @GetMapping("/{id}")
    @Operation(summary = "Get an asset by ID")
    fun getAsset(@PathVariable id: UUID) =
        scheduleService.getScheduleById(id).toOkResponse()

    @DeleteMapping("/{id}")
    fun deleteSchedule(@PathVariable id: UUID) =
        scheduleService.deleteSchedule(id).toOkResponse("Schedule Deleted")
}