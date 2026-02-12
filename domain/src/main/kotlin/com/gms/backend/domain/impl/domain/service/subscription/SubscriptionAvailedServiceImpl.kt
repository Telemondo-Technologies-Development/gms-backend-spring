package com.gms.backend.domain.impl.domain.service.subscription

import com.gms.backend.domain.application.mapper.subscription.SubscriptionAvailedMapper
import com.gms.backend.domain.application.rest.subscription.SubscriptionAvailedController
import com.gms.backend.domain.domain.model.subscription.SubscriptionAvailed
import com.gms.backend.domain.domain.repository.subscription.BillingCycleRepository
import com.gms.backend.domain.domain.repository.subscription.SubscriptionAvailedRepository
import com.gms.backend.domain.domain.repository.subscription.SubscriptionRepository
import com.gms.backend.domain.domain.service.subscription.SubscriptionAvailedService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@PreAuthorize("denyAll()")
class SubscriptionAvailedServiceImpl(
    private val subscriptionAvailedRepository: SubscriptionAvailedRepository,
    private val subscriptionAvailedMapper: SubscriptionAvailedMapper,
    private val subscriptionRepository: SubscriptionRepository,
    private val billingCycleRepository: BillingCycleRepository
) : SubscriptionAvailedService {
    @Transactional
    @PreAuthorize("hasAuthority('subscriptionAvailed_create')")
    override fun createSubscriptionAvailed(body: SubscriptionAvailedController.SubscriptionAvailedPostDTO): SubscriptionAvailedController.SubscriptionAvailedTableDTO {
        val subscription = subscriptionRepository.findById(body.subscriptionId).orElseThrow()
        val billingCycle = billingCycleRepository.findBillingCycleById(subscription.billingCycleId!!).orElseThrow()
        val subscriptionAvailed =
            subscriptionAvailedMapper.subscriptionAvailedPostDTOToSubscriptionAvailed(body).apply {
                this.subscription = subscription
                name = subscription.name
                amount = subscription.amount
                intervals = billingCycle.intervals
                intervalCount = billingCycle.intervalCount
                gracePeriodDays = billingCycle.gracePeriodDays
            }

        val saved = subscriptionAvailedRepository.saveAndFlush(subscriptionAvailed)
        return subscriptionAvailedMapper.subscriptionAvailedToSubscriptionAvailedTableDTO(saved)
    }

    @Transactional
    @PreAuthorize("hasAuthority('subscriptionAvailed_create')")
    override fun insertSubscriptionAvailed(id: UUID): UUID {
        val subscription = subscriptionRepository.findById(id).orElseThrow()
        val billingCycle = billingCycleRepository.findBillingCycleById(subscription.billingCycleId!!).orElseThrow()
        // Verify if an existing subscriptionAvailed exist with the exact information
        val existing = subscriptionAvailedRepository.findBySubscriptionCriteria(
            name = subscription.name,
            amount = subscription.amount,
            intervals = billingCycle.intervals,
            intervalCount = billingCycle.intervalCount,
            gracePeriodDays = billingCycle.gracePeriodDays,
            subscriptionId = subscription.id
        )

        if (existing == null) {
            val subscriptionAvailed =
                SubscriptionAvailed().apply {
                    this.subscription = subscription
                    name = subscription.name
                    amount = subscription.amount
                    intervals = billingCycle.intervals
                    intervalCount = billingCycle.intervalCount
                    gracePeriodDays = billingCycle.gracePeriodDays
                }

            val saved = subscriptionAvailedRepository.saveAndFlush(subscriptionAvailed)
            return saved.id
        }
        return existing
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('subscriptionAvailed_read')")
    override fun getSubscriptionAvailed(pageable: Pageable): Page<SubscriptionAvailedController.SubscriptionAvailedTableDTO> {
        return subscriptionAvailedRepository.findAllProjectedBy(pageable)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('subscriptionAvailed_read')")
    override fun getSubscriptionAvailedById(id: UUID): SubscriptionAvailedController.SubscriptionAvailedTableDTO {
        return subscriptionAvailedRepository.findById(id).orElseThrow()
            .let(subscriptionAvailedMapper::subscriptionAvailedToSubscriptionAvailedTableDTO)
    }

//    @Transactional
//    @PreAuthorize("hasAuthority('subscriptionAvailed_update')")
//    override fun updateSubscriptionAvailed(
//        id: UUID,
//        body: SubscriptionAvailedController.SubscriptionAvailedPutDTO
//    ): SubscriptionAvailedController.SubscriptionAvailedTableDTO {
//        val subscription = subscriptionRepository.findById(body.subscriptionId).orElseThrow()
//        val billingCycle = billingCycleRepository.findById(body.billingCycleId).orElseThrow()
//        val subscriptionAvailed = subscriptionAvailedMapper.subscriptionAvailedPostDTOToSubscriptionAvailed(body).apply {
//            this.subscription = subscription
//            name = subscription.name
//            amount = subscription.amount
//            intervals = billingCycle.intervals
//            intervalCount = billingCycle.intervalCount
//            gracePeriodDays = billingCycle.gracePeriodDays
//        }
//
//        subscriptionAvailedRepository.saveAndFlush(subscriptionAvailed)
//        return subscriptionAvailedMapper.subscriptionAvailedToSubscriptionAvailedTableDTO(subscriptionAvailed)
//    }

    @Transactional
    @PreAuthorize("hasAuthority('subscriptionAvailed_delete')")
    override fun deleteSubscriptionAvailed(id: UUID) {
        val subscriptionAvailed = subscriptionAvailedRepository.findById(id).orElseThrow()
        subscriptionAvailedRepository.delete(subscriptionAvailed)
    }

}
