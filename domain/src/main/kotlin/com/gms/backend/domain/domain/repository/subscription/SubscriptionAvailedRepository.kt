package com.gms.backend.domain.domain.repository.subscription

import com.gms.backend.domain.application.rest.subscription.SubscriptionAvailedController
import com.gms.backend.domain.domain.model.subscription.SubscriptionAvailed
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SubscriptionAvailedRepository : JpaRepository<SubscriptionAvailed, UUID> {
    fun findAllProjectedBy(pageable: Pageable): Page<SubscriptionAvailedController.SubscriptionAvailedTableDTO>
}
