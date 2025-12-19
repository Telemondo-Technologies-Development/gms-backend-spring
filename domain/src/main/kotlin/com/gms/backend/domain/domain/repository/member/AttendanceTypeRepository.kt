package com.gms.backend.domain.domain.repository.member

import com.gms.backend.domain.domain.model.member.AttendanceType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AttendanceTypeRepository : JpaRepository<AttendanceType, UUID>
