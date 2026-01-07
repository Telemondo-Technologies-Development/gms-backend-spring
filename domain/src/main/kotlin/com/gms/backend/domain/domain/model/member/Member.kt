package com.gms.backend.domain.domain.model.member

import com.gms.backend.domain.domain.model.storage.ObjectStorage
import com.gms.backend.domain.domain.model.user.Actor
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.*

@Entity
@Table(name = "members")
class Member {

    enum class MemberStatus {
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
    lateinit var surname: String

    @Column(nullable = false)
    lateinit var firstName: String

    @Column
    var middleName: String? = null

    @Column(length = 10)
    var suffix: String? = null

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    lateinit var status: MemberStatus

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    lateinit var createdAt: Instant

    @UpdateTimestamp
    @Column(nullable = false)
    lateinit var updatedAt: Instant

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    var createdBy: Actor? = null

    @Column(name = "created_by", insertable = false, updatable = false)
    var createdById: UUID? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", nullable = false)
    var updatedBy: Actor? = null

    @Column(name = "updated_by", insertable = false, updatable = false)
    var updatedById: UUID? = null

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "actor_id", nullable = false)
    var actor: Actor? = null

    @Column(name = "actor_id", insertable = false, updatable = false)
    var actorId: UUID? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_picture", nullable = true)
    var profilePicture: ObjectStorage? = null

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "member_objects",
        joinColumns = [JoinColumn(name = "member_id")],
        inverseJoinColumns = [JoinColumn(name = "object_id")],
    )
    var memberObjects = mutableSetOf<ObjectStorage>()
}
