package com.gms.backend.domain.domain.repository.subscription

import com.gms.backend.domain.application.rest.subscription.SubscriptionController
import com.gms.backend.domain.domain.model.subscription.Subscription
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SubscriptionRepository : JpaRepository<Subscription, UUID> {
    fun findAllProjectedBy(pageable: Pageable): Page<SubscriptionController.SubscriptionTableDTO>
}
