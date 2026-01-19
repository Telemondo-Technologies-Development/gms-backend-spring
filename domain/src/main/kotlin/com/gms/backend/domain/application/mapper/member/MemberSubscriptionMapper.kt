package com.gms.backend.domain.application.mapper.member

import com.gms.backend.domain.application.rest.member.MemberSubscriptionController
import com.gms.backend.domain.domain.model.member.MemberSubscription
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface MemberSubscriptionMapper {
    fun memberSubscriptionToMemberSubscriptionTableDTO(memberSubscription: MemberSubscription): MemberSubscriptionController.MemberSubscriptionTableDTO
    fun memberSubscriptionPostDTOToMemberSubscription(dto: MemberSubscriptionController.MemberSubscriptionPostDTO): MemberSubscription
    fun memberSubscriptionPutDTOToMemberSubscription(dto: MemberSubscriptionController.MemberSubscriptionPutDTO, @MappingTarget memberSubscription: MemberSubscription): MemberSubscription
}