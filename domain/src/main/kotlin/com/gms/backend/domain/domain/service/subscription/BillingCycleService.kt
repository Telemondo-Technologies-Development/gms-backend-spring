package com.gms.backend.domain.domain.service.subscription

import com.gms.backend.domain.application.rest.subscription.BillingCycleController
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface BillingCycleService {
    fun createBillingCycle(body: BillingCycleController.BillingCyclePostDTO): BillingCycleController.BillingCycleTableDTO
    fun getBillingCycles(pageable: Pageable): Page<BillingCycleController.BillingCycleTableDTO>
    fun getBillingCycleById(id: UUID): BillingCycleController.BillingCycleTableDTO
    fun updateBillingCycle(id: UUID, body: BillingCycleController.BillingCyclePutDTO): BillingCycleController.BillingCycleTableDTO
    fun deleteBillingCycle(id: UUID)
}