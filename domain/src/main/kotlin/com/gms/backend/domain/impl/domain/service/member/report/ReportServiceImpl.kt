package com.gms.backend.domain.impl.domain.service.member.report

import com.gms.backend.domain.application.mapper.member.report.ReportMapper
import com.gms.backend.domain.application.rest.member.report.ReportController
import com.gms.backend.domain.domain.repository.member.report.ReportRepository
import com.gms.backend.domain.domain.repository.member.report.ReportTypeRepository
import com.gms.backend.domain.domain.repository.branch.BranchRepository
import com.gms.backend.domain.domain.repository.storage.ObjectStorageRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@PreAuthorize("denyAll()")
class ReportServiceImpl(
    private val reportRepository: ReportRepository,
    private val reportTypeRepository: ReportTypeRepository,
    private val branchRepository: BranchRepository,
    private val actorRepository: ActorRepository,
    private val objectStorageRepository: ObjectStorageRepository,
    private val reportMapper: ReportMapper
) : ReportService {

    @Transactional
    @PreAuthorize("hasAuthority('report_create')")
    override fun createReport(body: ReportController.ReportPostDTO): ReportController.ReportTableDTO {
        val report = reportMapper.reportPostDTOToReport(body).apply {
            branch = branchRepository.getReferenceById(body.branchId)
            actor = actorRepository.getReferenceById(body.actorId)
            reportType = reportTypeRepository.getReferenceById(body.reportTypeId)
            createdBy = actorRepository.getReferenceById(body.createdById)
            updatedBy = actorRepository.getReferenceById(body.createdById)

            if (body.objectIds.isNotEmpty()) {
                reportsObjects.addAll(objectStorageRepository.findAllById(body.objectIds))
            }
        }
        return reportMapper.reportToDTO(reportRepository.saveAndFlush(report))
    }

    @Transactional
    @PreAuthorize("hasAuthority('report_update')")
    override fun updateReport(id: UUID, body: ReportController.ReportPutDTO): ReportController.ReportTableDTO {
        val report = reportRepository.findById(id).orElseThrow { NoSuchElementException("Report not found") }
        reportMapper.reportPutDTOToReport(body, report)

        report.apply {
            branch = branchRepository.getReferenceById(body.branchId)
            actor = actorRepository.getReferenceById(body.actorId)
            reportType = reportTypeRepository.getReferenceById(body.reportTypeId)
            updatedBy = actorRepository.getReferenceById(body.updatedById)

            reportsObjects.clear()
            if (body.objectIds.isNotEmpty()) {
                reportsObjects.addAll(objectStorageRepository.findAllById(body.objectIds))
            }
        }
        return reportMapper.reportToDTO(reportRepository.saveAndFlush(report))
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('report_read')")
    override fun getReports(pageable: Pageable): Page<ReportController.ReportTableDTO> =
        reportRepository.findAll(pageable).map { reportMapper.reportToDTO(it) }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('report_read')")
    override fun getReportById(id: UUID) = reportMapper.reportToDTO(reportRepository.findById(id).get())

    @Transactional
    @PreAuthorize("hasAuthority('report_delete')")
    override fun deleteReport(id: UUID) {
        val report = reportRepository.findById(id).orElseThrow {
            NoSuchElementException("Report not found")
        }
        reportRepository.delete(report)
    }
}