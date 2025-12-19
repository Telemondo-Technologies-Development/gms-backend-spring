package com.gms.backend.domain.domain.model.billing

import com.gms.backend.domain.domain.model.storage.ObjectStorage
import com.gms.backend.domain.domain.model.user.Actor
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.*

@Entity
@Table(name = "payment_methods")
class PaymentMethod {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "binary(16)")
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    var id: UUID? = null

    @Column(nullable = false, unique = true)
    var name: String? = null

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    var createdAt: Instant? = null

    @UpdateTimestamp
    @Column(nullable = false)
    var updatedAt: Instant? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    var createdBy: Actor? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", nullable = false)
    var updatedBy: Actor? = null

    @OneToMany(mappedBy = "paymentMethod")
    var paymentMethodPayments = mutableSetOf<Payment>()

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "payment_method_objects",
        joinColumns = [JoinColumn(name = "payment_method_id")],
        inverseJoinColumns = [JoinColumn(name = "object_id")],
    )
    var paymentMethodObjects = mutableSetOf<ObjectStorage>()
}
