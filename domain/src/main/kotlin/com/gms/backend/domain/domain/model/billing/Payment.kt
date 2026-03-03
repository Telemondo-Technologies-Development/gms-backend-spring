package com.gms.backend.domain.domain.model.billing

import com.gms.backend.domain.domain.model.user.Actor
import jakarta.persistence.*
import jakarta.validation.constraints.PositiveOrZero
import jakarta.validation.constraints.Size
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
        // Not final
        FULL,
        PARTIAL,
        PENDING,
        MISSED,
        CANCELLED,
        WAITING,
        FAILED
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
    @PositiveOrZero(message = "Amount must not be negative")
    var amount: BigDecimal = BigDecimal.ZERO

    @Column
    @Size(min = 1, message = "Reference Number cannot be blank if provided")
    var referenceNum: String? = null

    @Column(nullable = false)
    var paidAt: Instant? = null

    @Column
    @Size(min = 1, message = "Failure Reason cannot be blank if provided")
    var failureReason: String? = null

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    lateinit var createdAt: Instant

    @UpdateTimestamp
    @Column(nullable = false)
    lateinit var updatedAt: Instant

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    lateinit var invoice: Invoice

    @Column(name = "invoice_id", insertable = false, updatable = false)
    var invoiceId: UUID? = null

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id", nullable = false)
    lateinit var paymentMethod: PaymentMethod

    @Column(name = "payment_method_id", insertable = false, updatable = false)
    var paymentMethodId: UUID? = null

    @OneToMany(mappedBy = "payment")
    var paymentLedgers = mutableSetOf<Ledger>()
}
