package com.gms.backend.domain.application.mapper.member

import com.gms.backend.domain.application.rest.member.ProgressOptionController
import com.gms.backend.domain.domain.model.member.ProgressOption
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface ProgressOptionMapper {
    fun progressOptionToProgressOptionTableDTO(progressOption: ProgressOption): ProgressOptionController.ProgressOptionTableDTO
    fun progressOptionPostDTOToProgressOption(dto: ProgressOptionController.ProgressOptionPostDTO): ProgressOption
    fun progressOptionPutDTOToProgressOption(dto: ProgressOptionController.ProgressOptionPutDTO, @MappingTarget progressOption: ProgressOption): ProgressOption
}