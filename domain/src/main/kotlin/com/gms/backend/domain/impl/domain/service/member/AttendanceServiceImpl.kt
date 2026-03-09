package com.gms.backend.domain.impl.domain.service.member

import com.gms.backend.domain.application.mapper.member.AttendanceMapper
import com.gms.backend.domain.application.rest.member.AttendanceController
import com.gms.backend.domain.domain.model.member.Attendance
import com.gms.backend.domain.domain.repository.branch.BranchRepository
import com.gms.backend.domain.domain.repository.member.AttendanceRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.member.AttendanceService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Service
@PreAuthorize("denyAll()")
class AttendanceServiceImpl(
    private val attendanceRepository: AttendanceRepository,
    private val attendanceMapper: AttendanceMapper,
    private val branchRepository: BranchRepository,
    private val actorRepository: ActorRepository,
) : AttendanceService {
    @Transactional
    @PreAuthorize("hasAuthority('attendance_create')")
    override fun createAttendance(body: AttendanceController.AttendancePostDTO): AttendanceController.AttendanceTableDTO {
        val attendance = attendanceMapper.attendancePostDTOToAttendance(body).apply {
            actor = actorRepository.getReferenceById(body.actorId)
            branch = branchRepository.getReferenceById(body.branchId)
            createdBy = actorRepository.getReferenceById(body.createdById)
            updatedBy = actorRepository.getReferenceById(body.createdById)
        }

        val saved = attendanceRepository.saveAndFlush(attendance)
        return attendanceMapper.attendanceToAttendanceTableDTO(saved)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('attendance_read')")
    override fun getAttendances(
        pageable: Pageable,
        source: Attendance.AttendanceSource?,
        type: Attendance.AttendanceType?,
        dateFrom: Instant?,
        dateTo: Instant?
    ): Page<AttendanceController.AttendanceTableDTO> {
        return attendanceRepository.findAllProjectedBy(pageable, source, type, dateFrom, dateTo)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('attendance_read') and hasAuthority('permission_read')")
    override fun getAttendanceById(
        id: UUID,
        source: Attendance.AttendanceSource?,
        type: Attendance.AttendanceType?,
        dateFrom: Instant?,
        dateTo: Instant?
    ): AttendanceController.AttendanceTableDTO {
        return attendanceRepository.findByAttendanceId(id, source, type, dateFrom, dateTo).orElseThrow()
    }

    @Transactional
    @PreAuthorize("hasAuthority('attendance_update')")
    override fun updateAttendance(
        id: UUID,
        body: AttendanceController.AttendancePutDTO
    ): AttendanceController.AttendanceTableDTO {
        val attendance = attendanceRepository.findById(id).orElseThrow().apply {
            attendanceMapper.attendancePutDTOToAttendance(body, this)
            updatedBy = actorRepository.getReferenceById(body.updatedById)
        }

        attendanceRepository.saveAndFlush(attendance)
        return attendanceMapper.attendanceToAttendanceTableDTO(attendance)
    }

    @Transactional
    @PreAuthorize("hasAuthority('attendance_delete')")
    override fun deleteAttendance(id: UUID) {
        val attendance = attendanceRepository.findById(id).orElseThrow()
        return attendanceRepository.delete(attendance)
    }

}