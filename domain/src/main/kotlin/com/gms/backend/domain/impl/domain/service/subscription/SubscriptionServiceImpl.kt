package com.gms.backend.domain.impl.domain.service.subscription

import com.gms.backend.domain.application.mapper.subscription.SubscriptionMapper
import com.gms.backend.domain.application.rest.subscription.SubscriptionController
import com.gms.backend.domain.domain.repository.subscription.BillingCycleRepository
import com.gms.backend.domain.domain.repository.subscription.SubscriptionRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.subscription.SubscriptionService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@PreAuthorize("denyAll()")
class SubscriptionServiceImpl(
    private val subscriptionRepository: SubscriptionRepository,
    private val subscriptionMapper: SubscriptionMapper,
    private val billingCycleRepository: BillingCycleRepository,
    private val actorRepository: ActorRepository
) : SubscriptionService {
    @Transactional
    @PreAuthorize("hasAuthority('subscription_create')")
    override fun createSubscription(body: SubscriptionController.SubscriptionPostDTO): SubscriptionController.SubscriptionTableDTO {
        val subscription = subscriptionMapper.subscriptionPostDTOToSubscription(body).apply {
            billingCycle = billingCycleRepository.getReferenceById(body.billingCycleId)
            createdBy = actorRepository.getReferenceById(body.createdById)
            updatedBy = actorRepository.getReferenceById(body.createdById)
        }

        val saved = subscriptionRepository.saveAndFlush(subscription)
        return subscriptionMapper.subscriptionToSubscriptionTableDTO(saved)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('subscription_read')")
    override fun getSubscriptions(pageable: Pageable): Page<SubscriptionController.SubscriptionTableDTO> {
        return subscriptionRepository.findAllProjectedBy(pageable)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('subscription_read')")
    override fun getSubscriptionById(id: UUID): SubscriptionController.SubscriptionTableDTO {
        return subscriptionRepository.findById(id).orElseThrow()
            .let(subscriptionMapper::subscriptionToSubscriptionTableDTO)
    }

    @Transactional
    @PreAuthorize("hasAuthority('subscription_update')")
    override fun updateSubscription(
        id: UUID,
        body: SubscriptionController.SubscriptionPutDTO
    ): SubscriptionController.SubscriptionTableDTO {
        val subscription = subscriptionRepository.findById(id).orElseThrow().apply {
            subscriptionMapper.subscriptionPutDTOToSubscription(body, this)
            this.id = id
            billingCycle = billingCycleRepository.getReferenceById(body.billingCycleId)
            updatedBy = actorRepository.getReferenceById(body.updatedById)
        }

        subscriptionRepository.saveAndFlush(subscription)
        return subscriptionMapper.subscriptionToSubscriptionTableDTO(subscription)
    }

    @Transactional
    @PreAuthorize("hasAuthority('subscription_delete')")
    override fun deleteSubscription(id: UUID) {
        val subscription = subscriptionRepository.findById(id).orElseThrow()
        subscriptionRepository.delete(subscription)
    }

}
