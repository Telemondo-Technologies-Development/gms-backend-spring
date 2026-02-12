package com.gms.backend.domain.domain.service.subscription

import com.gms.backend.domain.application.rest.subscription.SubscriptionAvailedController
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface SubscriptionAvailedService {
    fun createSubscriptionAvailed(body: SubscriptionAvailedController.SubscriptionAvailedPostDTO): SubscriptionAvailedController.SubscriptionAvailedTableDTO
    fun insertSubscriptionAvailed(id: UUID): UUID
    fun getSubscriptionAvailed(pageable: Pageable): Page<SubscriptionAvailedController.SubscriptionAvailedTableDTO>
    fun getSubscriptionAvailedById(id: UUID): SubscriptionAvailedController.SubscriptionAvailedTableDTO
//    fun updateSubscriptionAvailed(id: UUID, body: SubscriptionAvailedController.SubscriptionAvailedPutDTO): SubscriptionAvailedController.SubscriptionAvailedTableDTO
    fun deleteSubscriptionAvailed(id: UUID)
}