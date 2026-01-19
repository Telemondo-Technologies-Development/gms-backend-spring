package com.gms.backend.domain.domain.model.user

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size
import org.hibernate.annotations.CreationTimestamp
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
    lateinit var id: UUID

    @Column(nullable = false, unique = true)
    @Email(message = "Email should be valid")
    lateinit var email: String

    @Column(nullable = false)
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
    @JsonIgnore
    lateinit var password: String

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    lateinit var createdAt: Instant

    @UpdateTimestamp
    @Column(nullable = false)
    lateinit var updatedAt: Instant

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "actor_id", nullable = false)
    @JsonIgnore
    lateinit var actor: Actor

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
