package com.gms.backend.domain.impl.domain.service.member

import com.gms.backend.domain.application.mapper.member.MemberSubscriptionMapper
import com.gms.backend.domain.application.response.ApiErrorType
import com.gms.backend.domain.application.response.DomainException
import com.gms.backend.domain.application.rest.member.MemberSubscriptionController
import com.gms.backend.domain.application.rest.subscription.SubscriptionAvailedController
import com.gms.backend.domain.domain.model.member.MemberSubscription
import com.gms.backend.domain.domain.model.user.Actor
import com.gms.backend.domain.domain.repository.branch.BranchRepository
import com.gms.backend.domain.domain.repository.member.MemberSubscriptionRepository
import com.gms.backend.domain.domain.repository.subscription.SubscriptionAvailedRepository
import com.gms.backend.domain.domain.repository.subscription.SubscriptionRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.member.MemberSubscriptionService
import com.gms.backend.domain.impl.domain.service.subscription.SubscriptionAvailedServiceImpl
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Service
@PreAuthorize("denyAll()")
class MemberSubscriptionServiceImpl(
    private val memberSubscriptionRepository: MemberSubscriptionRepository,
    private val memberSubscriptionMapper: MemberSubscriptionMapper,
    private val actorRepository: ActorRepository,
    private val branchRepository: BranchRepository,
    private val subscriptionRepository: SubscriptionRepository,
    private val subscriptionAvailedRepository: SubscriptionAvailedRepository,
    private val subscriptionAvailedServiceImpl: SubscriptionAvailedServiceImpl
) : MemberSubscriptionService {
    @Transactional
    @PreAuthorize("hasAuthority('memberSubscription_create')")
    override fun createMemberSubscription(body: MemberSubscriptionController.MemberSubscriptionPostDTO): MemberSubscriptionController.MemberSubscriptionTableDTO {
        val existingSubscription = memberSubscriptionRepository.findAllByActorIdAndStatus(
            body.actorId,
            MemberSubscription.MemberSubscriptionStatus.ACTIVE
        )
        if (existingSubscription.isNotEmpty()) throw DomainException(
            ApiErrorType.INVALID_CASE,
            "Member has an Active Subscription, you might want to edit/cancel existing subscription"
        )
        val subscriptionAvailed = subscriptionAvailedServiceImpl.createSubscriptionAvailed(
            SubscriptionAvailedController.SubscriptionAvailedPostDTO(body.subscriptionId)
        )
        // not sure if we will create member separately or the same time as this one
        val memberSubscription = memberSubscriptionMapper.memberSubscriptionPostDTOToMemberSubscription(body).apply {
            actor = actorRepository.getReferenceById(body.actorId)
            branch = branchRepository.getReferenceById(body.branchId)
            this.subscriptionAvailed = subscriptionAvailedRepository.getReferenceById(subscriptionAvailed.id)
            createdBy = actorRepository.getReferenceById(body.createdById)
            updatedBy = actorRepository.getReferenceById(body.createdById)
        }

        val saved = memberSubscriptionRepository.saveAndFlush(memberSubscription)
        return memberSubscriptionMapper.memberSubscriptionToMemberSubscriptionTableDTO(saved)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('memberSubscription_read')")
    override fun getMemberSubscriptions(pageable: Pageable): Page<MemberSubscriptionController.MemberSubscriptionTableDTO> {
        return memberSubscriptionRepository.findAllProjectedBy(pageable)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('memberSubscription_read')")
    override fun getMemberSubscriptionById(id: UUID): MemberSubscriptionController.MemberSubscriptionTableDTO {
        val entity = memberSubscriptionRepository.findById(id).orElseThrow()
        return memberSubscriptionMapper.memberSubscriptionToMemberSubscriptionTableDTO(entity)
    }

    @Transactional
    @PreAuthorize("hasAuthority('memberSubscription_update')")
    override fun updateMemberSubscription(
        id: UUID,
        body: MemberSubscriptionController.MemberSubscriptionPutDTO
    ): MemberSubscriptionController.MemberSubscriptionTableDTO {
        val memberSubscription = memberSubscriptionRepository.findById(id).orElseThrow()
        var subscriptionAvailed = memberSubscription.subscriptionAvailed

        // Check whether the subscription is changed or still the same
        if (memberSubscription.subscriptionAvailed.subscriptionId == body.subscriptionId) {
            if (body.updateCurrentSubscription) {
                // updates the current Subscription Availed
                val subscription = subscriptionRepository.findById(body.subscriptionId).orElseThrow()
                val billingCycle = subscription.billingCycle
                subscriptionAvailed.apply {
                    this.subscription = subscription
                    name = subscription.name
                    amount = subscription.amount
                    intervals = billingCycle.intervals
                    intervalCount = billingCycle.intervalCount
                    gracePeriodDays = billingCycle.gracePeriodDays
                }
            }
        } else {
            // Creates a new subscriptionAvailed
            val new = subscriptionAvailedServiceImpl.createSubscriptionAvailed(
                SubscriptionAvailedController.SubscriptionAvailedPostDTO(body.subscriptionId)
            )
            subscriptionAvailed = subscriptionAvailedRepository.getReferenceById(new.id)
        }

        memberSubscription.apply {
            memberSubscriptionMapper.memberSubscriptionPutDTOToMemberSubscription(body, this)
            actor = actorRepository.getReferenceById(body.actorId)
            branch = branchRepository.getReferenceById(body.branchId)
            this.subscriptionAvailed = subscriptionAvailedRepository.getReferenceById(subscriptionAvailed.id)
            updatedBy = actorRepository.getReferenceById(body.updatedById)
        }

        memberSubscriptionRepository.saveAndFlush(memberSubscription)
        return memberSubscriptionMapper.memberSubscriptionToMemberSubscriptionTableDTO(memberSubscription)
    }

    @Transactional
    @PreAuthorize("hasAuthority('memberSubscription_delete')")
    override fun deleteMemberSubscription(id: UUID) {
        val memberSubscription = memberSubscriptionRepository.findById(id).orElseThrow().apply {
            actor.status = Actor.ActorStatus.DELETED
            actor.deactivatedAt = Instant.now()
        }

        memberSubscriptionRepository.delete(memberSubscription)
    }

}