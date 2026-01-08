package com.gms.backend.domain.domain.repository.member.report

import com.gms.backend.domain.domain.model.member.report.ReportType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ReportTypeRepository : JpaRepository<ReportType, UUID>
