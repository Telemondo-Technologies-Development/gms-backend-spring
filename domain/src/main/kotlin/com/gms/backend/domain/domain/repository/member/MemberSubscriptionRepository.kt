package com.gms.backend.domain.domain.repository.member

import com.gms.backend.domain.application.rest.member.MemberSubscriptionController
import com.gms.backend.domain.domain.model.member.MemberSubscription
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MemberSubscriptionRepository : JpaRepository<MemberSubscription, UUID>{
    fun findAllProjectedBy(pageable: Pageable): Page<MemberSubscriptionController.MemberSubscriptionTableDTO>
    fun findAllByActorIdAndStatus(
        memberId: UUID,
        status: MemberSubscription.MemberSubscriptionStatus
    ): List<MemberSubscriptionController.MemberSubscriptionTableDTO>
}
