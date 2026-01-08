package com.gms.backend.domain.domain.model.member

import com.gms.backend.domain.domain.model.branch.Branch
import com.gms.backend.domain.domain.model.user.Actor
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.*

@Entity
@Table(name = "attendance")
class Attendance {

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

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    lateinit var createdAt: Instant

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id", nullable = false)
    var actor: Actor? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    var branch: Branch? = null
}