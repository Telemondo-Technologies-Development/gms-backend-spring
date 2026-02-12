package com.gms.backend.domain.impl.domain.service.member.report

import com.gms.backend.domain.application.rest.member.report.ReportController
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface ReportService{
    fun createReport(body: ReportController.ReportPostDTO): ReportController.ReportTableDTO
    fun updateReport(id: UUID, body: ReportController.ReportPutDTO): ReportController.ReportTableDTO
    fun getReports(pageable: Pageable): Page<ReportController.ReportTableDTO>
    fun getReportById(id: UUID): ReportController.ReportTableDTO
    fun deleteReport(id: UUID)
}