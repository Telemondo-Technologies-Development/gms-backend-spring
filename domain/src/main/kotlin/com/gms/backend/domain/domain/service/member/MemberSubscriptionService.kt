package com.gms.backend.domain.domain.service.member

import com.gms.backend.domain.application.rest.member.MemberSubscriptionController
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface MemberSubscriptionService {
    fun createMemberSubscription(body: MemberSubscriptionController.MemberSubscriptionPostDTO): MemberSubscriptionController.MemberSubscriptionTableDTO
    fun getMemberSubscriptions(pageable: Pageable): Page<MemberSubscriptionController.MemberSubscriptionTableDTO>
    fun getMemberSubscriptionById(id: UUID): MemberSubscriptionController.MemberSubscriptionTableDTO
    fun updateMemberSubscription(id: UUID, body: MemberSubscriptionController.MemberSubscriptionPutDTO): MemberSubscriptionController.MemberSubscriptionTableDTO
    fun deleteMemberSubscription(id: UUID)
}