package com.gms.backend.domain.domain.repository.member

import com.gms.backend.domain.domain.model.member.Attendance
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AttendanceRepository : JpaRepository<Attendance, UUID>
