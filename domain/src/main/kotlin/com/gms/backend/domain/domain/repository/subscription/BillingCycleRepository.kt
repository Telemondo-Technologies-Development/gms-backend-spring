package com.gms.backend.domain.domain.repository.subscription

import com.gms.backend.domain.domain.model.subscription.BillingCycle
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface BillingCycleRepository : JpaRepository<BillingCycle, UUID>
