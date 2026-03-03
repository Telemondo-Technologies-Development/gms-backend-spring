package com.gms.backend.domain.application.rest.member

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.domain.model.member.Attendance
import com.gms.backend.domain.domain.service.member.AttendanceService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.*

@RestController
@RequestMapping("/api/member/attendance")
@Tag(name = "Attendance")
class AttendanceController(private val attendanceService: AttendanceService) {

    @Schema(description = "Format for Attendance read")
    data class AttendanceTableDTO(
        val id: UUID,
        val actorId: UUID?,
        val branchId: UUID?,
        val source: Attendance.AttendanceSource,
        val type: Attendance.AttendanceType,
        val recordedAt: Instant,
        val createdById: UUID?,
        val updatedById: UUID?
    )

    @Schema(description = "Format for Attendance create")
    data class AttendancePostDTO(
        val actorId: UUID,
        val branchId: UUID,
        val source: Attendance.AttendanceSource,
        val type: Attendance.AttendanceType,
        val recordedAt: Instant,
        val createdById: UUID
    )

    @Schema(description = "Format for Attendance update")
    data class AttendancePutDTO(
        val source: Attendance.AttendanceSource,
        val type: Attendance.AttendanceType,
        val recordedAt: Instant,
        val updatedById: UUID
    )

    @GetMapping
    @Operation(summary = "Get all Attendances")
    fun getAllAttendances(pageable: Pageable) =
        attendanceService.getAttendances(pageable).toPaginatedResponse()


    @GetMapping("/{id}")
    @Operation(summary = "Get a Attendance by id")
    fun getAttendance(@PathVariable id: UUID) =
        attendanceService.getAttendanceById(id).toOkResponse()

    @PostMapping
    @Operation(summary = "Create a new Attendance")
    fun createAttendance(@Valid @RequestBody body: AttendancePostDTO) =
        attendanceService.createAttendance(body).toCreatedResponse()

    @Operation(summary = "Update a Attendance by id")
    @PutMapping("/{id}")
    fun updateAttendance(@PathVariable id: UUID, @Valid @RequestBody body: AttendancePutDTO) =
        attendanceService.updateAttendance(id, body).toOkResponse()

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Attendance by id")
    fun deleteAttendance(@PathVariable id: UUID) =
        attendanceService.deleteAttendance(id).toOkResponse("Deleted Successfully")

}