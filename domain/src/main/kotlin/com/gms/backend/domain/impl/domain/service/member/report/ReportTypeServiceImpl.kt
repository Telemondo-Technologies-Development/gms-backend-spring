package com.gms.backend.domain.impl.domain.service.member.report

import com.gms.backend.domain.application.mapper.member.report.ReportTypeMapper
import com.gms.backend.domain.application.rest.member.report.ReportTypeController
import com.gms.backend.domain.domain.repository.member.report.ReportTypeRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.member.report.ReportTypeService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@PreAuthorize("denyAll()")
class ReportTypeServiceImpl(
    private val reportTypeRepository: ReportTypeRepository,
    private val actorRepository: ActorRepository,
    private val reportTypeMapper: ReportTypeMapper
) : ReportTypeService {

    @Transactional
    @PreAuthorize("hasAuthority('reportType_create')")
    override fun createReportType(body: ReportTypeController.ReportTypePostDTO): ReportTypeController.ReportTypeTableDTO {
        val reportType = reportTypeMapper.reportTypePostDTOToReportType(body).apply {
            createdBy = actorRepository.getReferenceById(body.createdById)
            updatedBy = actorRepository.getReferenceById(body.createdById)
        }
        return reportTypeMapper.reportTypeToDTO(reportTypeRepository.saveAndFlush(reportType))
    }

    @Transactional
    @PreAuthorize("hasAuthority('reportType_update')")
    override fun updateReportType(id: UUID, body: ReportTypeController.ReportTypePutDTO): ReportTypeController.ReportTypeTableDTO {
        val reportType = reportTypeRepository.findById(id).orElseThrow {
            NoSuchElementException("Report Type not found with ID: $id")
        }
        reportTypeMapper.reportTypePutDTOToReportType(body, reportType)

        reportType.apply {
            updatedBy = actorRepository.getReferenceById(body.updatedById)
        }

        return reportTypeMapper.reportTypeToDTO(reportTypeRepository.saveAndFlush(reportType))
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('reportType_read')")
    override fun getReportTypes(pageable: Pageable): Page<ReportTypeController.ReportTypeTableDTO> =
        reportTypeRepository.findAll(pageable).map { reportTypeMapper.reportTypeToDTO(it) }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('reportType_read')")
    override fun getReportTypeById(id: UUID): ReportTypeController.ReportTypeTableDTO {
        val reportType = reportTypeRepository.findById(id).orElseThrow {
            NoSuchElementException("Report Type not found with ID: $id")
        }
        return reportTypeMapper.reportTypeToDTO(reportType)
    }

    @Transactional
    @PreAuthorize("hasAuthority('reportType_delete')")
    override fun deleteReportType(id: UUID) {
        val reportType = reportTypeRepository.findById(id).orElseThrow {
            NoSuchElementException("Report Type not found with ID: $id")
        }
        reportTypeRepository.delete(reportType)
    }
}