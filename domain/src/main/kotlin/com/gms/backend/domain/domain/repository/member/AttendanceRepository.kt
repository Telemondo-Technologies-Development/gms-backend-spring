package com.gms.backend.domain.domain.repository.member

import com.gms.backend.domain.application.rest.member.AttendanceController
import com.gms.backend.domain.domain.model.member.Attendance
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface AttendanceRepository : JpaRepository<Attendance, UUID> {
    @Query("SELECT a FROM Attendance a ORDER BY a.createdAt DESC")
    fun findAllProjectedBy(pageable: Pageable): Page<AttendanceController.AttendanceTableDTO>
}
