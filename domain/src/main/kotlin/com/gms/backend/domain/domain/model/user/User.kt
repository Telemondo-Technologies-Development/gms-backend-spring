package com.gms.backend.domain.domain.model.user

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.*

@Entity
@Table(name = "users")
//@DynamicUpdate // Only updates changed field (Seems to be inefficient for small tables with few data)
class User {
    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "binary(16)")
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    var id: UUID? = null

    @Column(nullable = false, unique = true)
    var email: String? = null

    @Column(nullable = false)
    @JsonIgnore
    var password: String? = null

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    var createdAt: Instant? = null

    @UpdateTimestamp
    @Column(nullable = false)
    var updatedAt: Instant? = null

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "actor_id", nullable = false)
    @JsonIgnore
    var actor: Actor? = null

    // This creates a read only column
    @Column(name = "actor_id", insertable = false, updatable = false)
    var actorId: UUID? = null

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    var userEmployees = mutableSetOf<Employee>()

    @ManyToMany(mappedBy = "userRoleUsers")
    @JsonIgnore
    var userRoleRoles = mutableSetOf<Role>()
}
