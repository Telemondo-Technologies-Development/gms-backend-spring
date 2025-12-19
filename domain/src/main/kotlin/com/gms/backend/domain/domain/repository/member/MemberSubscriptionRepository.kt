package com.gms.backend.domain.domain.repository.member

import com.gms.backend.domain.domain.model.member.MemberSubscription
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MemberSubscriptionRepository : JpaRepository<MemberSubscription, UUID>
