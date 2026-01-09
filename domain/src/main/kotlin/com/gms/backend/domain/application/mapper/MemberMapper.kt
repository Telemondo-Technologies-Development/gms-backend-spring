package com.gms.backend.domain.application.mapper

import com.gms.backend.domain.application.rest.member.MemberController
import com.gms.backend.domain.domain.model.member.Member
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface MemberMapper {
    fun memberToMemberTableDTO(member: Member): MemberController.MemberTableDTO
    fun memberPostDTOToMember(dto: MemberController.MemberPostDTO): Member
    fun memberPutDTOToMember(dto: MemberController.MemberPutDTO, @MappingTarget member: Member): Member
}
