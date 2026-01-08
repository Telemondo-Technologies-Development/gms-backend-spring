package com.gms.backend.domain.domain.model.billing

import com.gms.backend.domain.domain.model.user.Actor
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.UuidGenerator
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@Entity
@Table(name = "payments")
class Payment {

    enum class PaymentStatus {
        IN,
        OUT,
        UNDECIDED,
    }

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "binary(16)")
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    lateinit var id: UUID

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    lateinit var status: PaymentStatus

    @Column(nullable = false, precision = 10, scale = 2)
    var amount: BigDecimal = BigDecimal.ZERO

    @Column(nullable = false)
    var paidAt: Instant? = null

    @Column
    var failureReason: String? = null

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    lateinit var createdAt: Instant

    @UpdateTimestamp
    @Column(nullable = false)
    lateinit var updatedAt: Instant

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    var invoice: Invoice? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    var createdBy: Actor? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", nullable = false)
    var updatedBy: Actor? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id", nullable = false)
    var paymentMethod: PaymentMethod? = null

    @OneToMany(mappedBy = "payment")
    var paymentLedgers = mutableSetOf<Ledger>()
}
