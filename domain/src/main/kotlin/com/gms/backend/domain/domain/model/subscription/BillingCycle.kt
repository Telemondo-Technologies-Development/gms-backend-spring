package com.gms.backend.domain.domain.model.subscription

import com.gms.backend.domain.domain.model.user.Actor
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.*

@Entity
@Table(name = "billing_cycles")
class BillingCycle {

    enum class Interval {
        DAILY,
        WEEKLY,
        MONTHLY,
        YEARLY,
    }

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "binary(16)")
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    lateinit var id: UUID

    @Column(nullable = false)
    @NotBlank(message = "Name must not be empty")
    lateinit var name: String

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    lateinit var intervals: Interval

    @Column(nullable = false)
    @Positive
    var intervalCount: Int = 0

    @Column(nullable = false)
    @PositiveOrZero
    var gracePeriodDays: Int = 0

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    lateinit var createdAt: Instant

    @UpdateTimestamp
    @Column(nullable = false)
    lateinit var updatedAt: Instant

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    lateinit var createdBy: Actor

    @Column(name = "created_by", insertable = false, updatable = false)
    var createdById: UUID? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", nullable = false)
    lateinit var updatedBy: Actor

    @Column(name = "updated_by", insertable = false, updatable = false)
    var updatedById: UUID? = null

    @OneToMany(mappedBy = "billingCycle")
    var billingCycleSubscriptions = mutableSetOf<Subscription>()
}
