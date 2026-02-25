package com.gms.backend.domain.domain.model.member

import com.gms.backend.domain.domain.model.branch.Branch
import com.gms.backend.domain.domain.model.user.Actor
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.*

@Entity
@Table(name = "attendance")
class Attendance {

    enum class AttendanceSource {
        MANUAL,
        QR,
        FACE,
        FINGERPRINT
    }

    enum class AttendanceType {
        IN,
        OUT,
        UNDECIDED,
    }

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "binary(16)")
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    lateinit var id: UUID

    @Column
    var remarks: String? = null

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    lateinit var type: AttendanceType

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    lateinit var source: AttendanceSource

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    lateinit var createdAt: Instant

    @UpdateTimestamp
    @Column(nullable = false, updatable = false)
    lateinit var updatedAt: Instant

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id", nullable = false)
    lateinit var actor: Actor

    @Column(name = "actor_id", insertable = false, updatable = false)
    var actorId: UUID? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    lateinit var branch: Branch

    @Column(name = "branch_id", insertable = false, updatable = false)
    var branchId: UUID? = null

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
}