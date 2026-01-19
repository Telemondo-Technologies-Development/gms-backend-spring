package com.gms.backend.domain.domain.repository.subscription

import com.gms.backend.domain.application.rest.subscription.BillingCycleController
import com.gms.backend.domain.domain.model.subscription.BillingCycle
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface BillingCycleRepository : JpaRepository<BillingCycle, UUID> {
    fun findAllProjectedBy(pageable: Pageable): Page<BillingCycleController.BillingCycleTableDTO>
}
