package com.gms.backend.domain.application.mapper.member.report

import com.gms.backend.domain.application.rest.member.report.ReportController
import com.gms.backend.domain.domain.model.member.report.Report
import com.gms.backend.domain.domain.model.storage.ObjectStorage
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy
import java.util.*

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface ReportMapper {
    fun reportPostDTOToReport(dto: ReportController.ReportPostDTO): Report
    fun reportPutDTOToReport(dto: ReportController.ReportPutDTO, @MappingTarget report: Report): Report
    fun reportToDTO(report: Report): ReportController.ReportTableDTO
}