package com.gms.backend.domain.domain.model.user

import com.gms.backend.domain.domain.model.storage.ObjectStorage
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.*

@Entity
@Table(name = "employees")
class Employee {

    enum class EmployeeStatus {
        IN,
        OUT,
        UNDECIDED,
    }

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "binary(16)")
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    var id: UUID? = null

    @Column(nullable = false)
    var surname: String? = null

    @Column(nullable = false)
    var firstName: String? = null

    @Column
    var middleName: String? = null

    @Column(length = 10)
    var suffix: String? = null

    @Column(length = 13)
    var contactNo: String? = null

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    lateinit var status: EmployeeStatus

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    var createdAt: Instant? = null

    @UpdateTimestamp
    @Column(nullable = false)
    var updatedAt: Instant? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User? = null

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "actor_id", nullable = false)
    var actor: Actor? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_picture", nullable = true)
    var profilePicture: ObjectStorage? = null

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "employee_objects",
        joinColumns = [JoinColumn(name = "employee_id")],
        inverseJoinColumns = [JoinColumn(name = "object_id")],
    )
    var employeeObjects = mutableSetOf<ObjectStorage>()
}
