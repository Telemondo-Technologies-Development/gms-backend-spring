package com.gms.backend.domain.application.mapper.member

import com.gms.backend.domain.application.rest.member.AttendanceController
import com.gms.backend.domain.domain.model.member.Attendance
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface AttendanceMapper {
    fun attendanceToAttendanceTableDTO(attendance: Attendance): AttendanceController.AttendanceTableDTO
    fun attendancePostDTOToAttendance(dto: AttendanceController.AttendancePostDTO): Attendance
    fun attendancePutDTOToAttendance(dto: AttendanceController.AttendancePutDTO, @MappingTarget attendance: Attendance): Attendance
}