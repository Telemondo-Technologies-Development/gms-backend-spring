package com.gms.backend.domain.application.rest.asset

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.domain.model.asset.MaintenanceSchedule
import com.gms.backend.domain.domain.service.asset.MaintenanceScheduleService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.data.domain.Pageable
import java.time.Instant
import java.util.UUID

@RestController
@RequestMapping("/api/asset/maintenance/schedule")
@Tag(name = "Maintenance Schedule")
class MaintenanceScheduleController(private val scheduleService: MaintenanceScheduleService) {

    data class ScheduleTableDTO(
        val id: UUID,
        val assetId: UUID,
        val name: String,
        val startDate: Instant,
        val intervalUnit: MaintenanceSchedule.IntervalUnit,
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
        val intervalUnit: MaintenanceSchedule.IntervalUnit,
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
        @get:AssertTrue(message = "Advanced settings (week rank, day of week, month) can only be used with MONTH or YEAR intervals")
        val isAdvancedSettingsAllowed: Boolean
            get() {
                val isAdvanced = weekRank != null || dayOfWeek != null || monthOfYear != null
                val isAllowedUnit = intervalUnit == MaintenanceSchedule.IntervalUnit.MONTH ||
                        intervalUnit == MaintenanceSchedule.IntervalUnit.YEAR

                // If they provided advanced fields, the unit MUST be MONTH or YEAR
                return if (isAdvanced) isAllowedUnit else true
            }
    }

    data class SchedulePutDTO(
        @field:NotBlank(message = "Schedule name is required")
        val name: String,
        @field:NotNull(message = "Start date is required")
        val startDate: Instant,
        val intervalUnit: MaintenanceSchedule.IntervalUnit,
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
        @get:AssertTrue(message = "Advanced settings (week rank, day of week, month) can only be used with MONTH or YEAR intervals")
        val isAdvancedSettingsAllowed: Boolean
            get() {
                val isAdvanced = weekRank != null || dayOfWeek != null || monthOfYear != null
                val isAllowedUnit = intervalUnit == MaintenanceSchedule.IntervalUnit.MONTH ||
                        intervalUnit == MaintenanceSchedule.IntervalUnit.YEAR

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