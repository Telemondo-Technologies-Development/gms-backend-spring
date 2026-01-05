package com.gms.backend.domain.domain.model.member

import com.gms.backend.domain.domain.model.branch.Branch
import com.gms.backend.domain.domain.model.subscription.SubscriptionAvailed
import com.gms.backend.domain.domain.model.user.Actor
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.*

@Entity
@Table(name = "member_subscriptions")
class MemberSubscription {

    enum class MemberSubscriptionStatus {
        IN,
        OUT,
        UNDECIDED,
    }

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "binary(16)")
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    var id: UUID? = null

    @Column(nullable = false)
    var startDate: Instant? = null

    @Column
    var endDate: Instant? = null

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    lateinit var status: MemberSubscriptionStatus

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    var createdAt: Instant? = null

    @UpdateTimestamp
    @Column(nullable = false)
    var updatedAt: Instant? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id", nullable = false)
    var actor: Actor? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_availed_id", nullable = false)
    var subscriptionAvailed: SubscriptionAvailed? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    var branch: Branch? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    var createdBy: Actor? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", nullable = false)
    var updatedBy: Actor? = null
}
