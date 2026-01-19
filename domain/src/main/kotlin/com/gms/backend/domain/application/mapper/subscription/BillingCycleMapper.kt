package com.gms.backend.domain.application.mapper.subscription

import com.gms.backend.domain.application.rest.subscription.BillingCycleController
import com.gms.backend.domain.domain.model.subscription.BillingCycle
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface BillingCycleMapper {
    fun billingCycleToBillingCycleTableDTO(billingCycle: BillingCycle): BillingCycleController.BillingCycleTableDTO
    fun billingCyclePostDTOToBillingCycle(dto: BillingCycleController.BillingCyclePostDTO): BillingCycle
    fun billingCyclePutDTOToBillingCycle(dto: BillingCycleController.BillingCyclePutDTO, @MappingTarget billingCycle: BillingCycle): BillingCycle
}
