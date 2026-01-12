package com.gms.backend.domain.domain.model.subscription

import com.gms.backend.domain.domain.model.billing.Invoice
import com.gms.backend.domain.domain.model.billing.Ledger
import com.gms.backend.domain.domain.model.member.MemberSubscription
import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.math.BigDecimal
import java.util.*

@Entity
@Table(name = "subscription_availed")
class SubscriptionAvailed {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "binary(16)")
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    lateinit var id: UUID

    @Column(nullable = false)
    lateinit var name: String

    @Column(nullable = false, precision = 10, scale = 2)
    var amount: BigDecimal = BigDecimal.ZERO

    @Column(nullable = false)
    lateinit var intervals: String

    @Column(nullable = false)
    var intervalCount: Int = 0

    @Column(nullable = false)
    var gracePeriodDays: Int = 0

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id", nullable = false)
    var subscription: Subscription? = null

    @OneToMany(mappedBy = "subscriptionAvailed")
    var subscriptionAvailedMemberSubscriptions = mutableSetOf<MemberSubscription>()

    @OneToMany(mappedBy = "subscriptionAvailed")
    var subscriptionAvailedInvoices = mutableSetOf<Invoice>()

    @OneToMany(mappedBy = "subscriptionAvailed")
    var subscriptionAvailedLedgers = mutableSetOf<Ledger>()
}
