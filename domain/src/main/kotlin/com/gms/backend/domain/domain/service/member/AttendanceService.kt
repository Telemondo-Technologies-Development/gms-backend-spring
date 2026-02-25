package com.gms.backend.domain.domain.service.member

import com.gms.backend.domain.application.rest.member.AttendanceController
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface AttendanceService {
    fun createAttendance(body: AttendanceController.AttendancePostDTO): AttendanceController.AttendanceTableDTO
    fun getAttendances(pageable: Pageable): Page<AttendanceController.AttendanceTableDTO>
    fun getAttendanceById(id: UUID): AttendanceController.AttendanceTableDTO
    fun updateAttendance(id: UUID, body: AttendanceController.AttendancePutDTO): AttendanceController.AttendanceTableDTO
    fun deleteAttendance(id: UUID)
}