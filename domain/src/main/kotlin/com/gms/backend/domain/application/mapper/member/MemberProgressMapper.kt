package com.gms.backend.domain.application.mapper.member

import com.gms.backend.domain.application.rest.member.MemberProgressController
import com.gms.backend.domain.domain.model.member.MemberProgress
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface MemberProgressMapper {
    fun memberProgressToMemberProgressTableDTO(memberMemberProgress: MemberProgress): MemberProgressController.MemberProgressTableDTO
    fun memberHistoryToMemberHistoryBriefDTO(memberHistory: MemberProgressController.MemberProgressHistory): MemberProgressController.MemberProgressHistoryBrief
    fun memberProgressPostDTOToMemberProgress(dto: MemberProgressController.MemberProgressPostDTO): MemberProgress
    fun memberProgressPutDTOToMemberProgress(dto: MemberProgressController.MemberProgressPutDTO, @MappingTarget memberProgress: MemberProgress): MemberProgress
}