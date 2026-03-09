package com.gms.backend.domain.domain.service.member

import com.gms.backend.domain.application.rest.member.AttendanceController
import com.gms.backend.domain.domain.model.member.Attendance
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.Instant
import java.util.*

interface AttendanceService {
    fun createAttendance(body: AttendanceController.AttendancePostDTO): AttendanceController.AttendanceTableDTO
    fun getAttendances(
        pageable: Pageable,
        source: Attendance.AttendanceSource?,
        type: Attendance.AttendanceType?,
        dateFrom: Instant?,
        dateTo: Instant?
    ): Page<AttendanceController.AttendanceTableDTO>
    fun getAttendanceById(
        id: UUID,
        source: Attendance.AttendanceSource?,
        type: Attendance.AttendanceType?,
        dateFrom: Instant?,
        dateTo: Instant?
    ): AttendanceController.AttendanceTableDTO
    fun updateAttendance(id: UUID, body: AttendanceController.AttendancePutDTO): AttendanceController.AttendanceTableDTO
    fun deleteAttendance(id: UUID)
}