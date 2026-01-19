package com.gms.backend.domain.application.mapper.subscription

import com.gms.backend.domain.application.rest.subscription.SubscriptionAvailedController
import com.gms.backend.domain.domain.model.subscription.SubscriptionAvailed
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface SubscriptionAvailedMapper {
    fun subscriptionAvailedToSubscriptionAvailedTableDTO(subscriptionAvailed: SubscriptionAvailed): SubscriptionAvailedController.SubscriptionAvailedTableDTO
    fun subscriptionAvailedPostDTOToSubscriptionAvailed(dto: SubscriptionAvailedController.SubscriptionAvailedPostDTO): SubscriptionAvailed
//    fun subscriptionAvailedPutDTOToSubscriptionAvailed(dto: SubscriptionAvailedController.SubscriptionAvailedPutDTO, @MappingTarget subscriptionAvailed: SubscriptionAvailed): SubscriptionAvailed
}
