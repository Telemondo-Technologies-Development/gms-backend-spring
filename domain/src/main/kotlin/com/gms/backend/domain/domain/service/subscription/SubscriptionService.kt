package com.gms.backend.domain.domain.service.subscription

import com.gms.backend.domain.application.rest.subscription.SubscriptionController
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface SubscriptionService {
    fun createSubscription(body: SubscriptionController.SubscriptionPostDTO): SubscriptionController.SubscriptionTableDTO
    fun getSubscriptions(pageable: Pageable): Page<SubscriptionController.SubscriptionTableDTO>
    fun getSubscriptionById(id: UUID): SubscriptionController.SubscriptionTableDTO
    fun updateSubscription(id: UUID, body: SubscriptionController.SubscriptionPutDTO): SubscriptionController.SubscriptionTableDTO
    fun deleteSubscription(id: UUID)
}