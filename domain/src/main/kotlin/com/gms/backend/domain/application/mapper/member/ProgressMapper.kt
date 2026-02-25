package com.gms.backend.domain.application.mapper.member

import com.gms.backend.domain.application.rest.member.ProgressController
import com.gms.backend.domain.domain.model.member.Progress
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface ProgressMapper {
    fun progressToProgressTableDTO(progress: Progress): ProgressController.ProgressTableDTO
    fun progressPostDTOToProgress(dto: ProgressController.ProgressPostDTO): Progress
    fun progressPutDTOToProgress(dto: ProgressController.ProgressPutDTO, @MappingTarget progress: Progress): Progress
}