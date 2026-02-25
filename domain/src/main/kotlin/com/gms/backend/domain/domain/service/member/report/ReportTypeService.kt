package com.gms.backend.domain.domain.service.member.report

import com.gms.backend.domain.application.rest.member.report.ReportTypeController
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface ReportTypeService {
    fun createReportType(body: ReportTypeController.ReportTypePostDTO): ReportTypeController.ReportTypeTableDTO
    fun updateReportType(id: UUID, body: ReportTypeController.ReportTypePutDTO): ReportTypeController.ReportTypeTableDTO
    fun getReportTypes(pageable: Pageable): Page<ReportTypeController.ReportTypeTableDTO>
    fun getReportTypeById(id: UUID): ReportTypeController.ReportTypeTableDTO
    fun deleteReportType(id: UUID)
}