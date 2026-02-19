package com.gms.backend.domain.domain.repository.member.report

import com.gms.backend.domain.domain.model.member.report.Report
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ReportRepository : JpaRepository<Report, UUID> {
    fun findAllByReportsObjectsId(id: UUID): List<Report>
    fun findAllByActorId(actorId: UUID, pageable: Pageable): Page<Report>
}
