package com.gms.backend.domain.domain.repository.member

import com.gms.backend.domain.application.rest.member.MemberSubscriptionController
import com.gms.backend.domain.domain.model.member.MemberSubscription
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface MemberSubscriptionRepository : JpaRepository<MemberSubscription, UUID>{
    fun findAllProjectedBy(pageable: Pageable): Page<MemberSubscriptionController.MemberSubscriptionTableDTO>
    fun findAllByActorIdAndStatus(
        memberId: UUID,
        status: MemberSubscription.MemberSubscriptionStatus
    ): List<MemberSubscriptionController.MemberSubscriptionTableDTO>

    @Query("""
        SELECT m.id 
        FROM MemberSubscription m 
        WHERE m.actorId = :actorId 
        AND m.status = :status
    """)
    fun findIdsByActorIdAndStatus(
        @Param("actorId") actorId: UUID,
        @Param("status") status: MemberSubscription.MemberSubscriptionStatus
    ): List<UUID>

    @Query(
        $$"""
        SELECT new com.gms.backend.domain.application.rest.member.MemberSubscriptionController$MemberSubscriptionIdsDTO(
        m.id,
        s.subscriptionId
        )
        FROM MemberSubscription m 
        JOIN m.subscriptionAvailed s
        WHERE m.id = :id 
    """
    )
    fun findMemberSubscriptionById(
        @Param("id") id: UUID,
    ): Optional<MemberSubscriptionController.MemberSubscriptionIdsDTO>
}
