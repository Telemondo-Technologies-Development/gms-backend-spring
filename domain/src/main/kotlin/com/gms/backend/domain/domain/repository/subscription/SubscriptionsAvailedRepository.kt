package com.gms.backend.domain.domain.repository.subscription

import com.gms.backend.domain.domain.model.subscription.SubscriptionAvailed
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SubscriptionsAvailedRepository : JpaRepository<SubscriptionAvailed, UUID>
