package com.gms.backend.domain.domain.model.user

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.util.*

@Entity
@Table(name = "permissions")
class Permission {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "binary(16)")
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    lateinit var id: UUID

    @Column(nullable = false, unique = true)
    lateinit var name: String

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "role_permissions",
        joinColumns = [JoinColumn(name = "permission_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")],
    )
    var rolePermissionRoles = mutableSetOf<Role>()
}
