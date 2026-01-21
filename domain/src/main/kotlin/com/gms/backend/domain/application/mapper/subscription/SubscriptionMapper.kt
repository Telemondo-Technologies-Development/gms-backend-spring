package com.gms.backend.domain.application.mapper.subscription

import com.gms.backend.domain.application.rest.subscription.SubscriptionController
import com.gms.backend.domain.domain.model.subscription.Subscription
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface SubscriptionMapper {
    fun subscriptionToSubscriptionTableDTO(subscription: Subscription): SubscriptionController.SubscriptionTableDTO
    fun subscriptionPostDTOToSubscription(dto: SubscriptionController.SubscriptionPostDTO): Subscription
    fun subscriptionPutDTOToSubscription(dto: SubscriptionController.SubscriptionPutDTO, @MappingTarget subscription: Subscription): Subscription
}
