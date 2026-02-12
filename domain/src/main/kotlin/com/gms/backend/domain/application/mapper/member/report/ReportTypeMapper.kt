package com.gms.backend.domain.application.mapper.member.report

import com.gms.backend.domain.application.rest.member.report.ReportTypeController
import com.gms.backend.domain.domain.model.member.report.ReportType
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface ReportTypeMapper {
    fun reportTypePostDTOToReportType(dto: ReportTypeController.ReportTypePostDTO): ReportType
    fun reportTypePutDTOToReportType(dto: ReportTypeController.ReportTypePutDTO, @MappingTarget reportType: ReportType): ReportType
    fun reportTypeToDTO(reportType: ReportType): ReportTypeController.ReportTypeTableDTO
}