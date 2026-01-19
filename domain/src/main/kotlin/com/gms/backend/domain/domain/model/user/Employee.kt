package com.gms.backend.domain.domain.model.user

import com.fasterxml.jackson.annotation.JsonIgnore
import com.gms.backend.domain.domain.model.storage.ObjectStorage
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
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
    lateinit var id: UUID

    @Column(nullable = false)
    @NotBlank(message = "Surname must not be empty")
    lateinit var surname: String

    @Column(nullable = false)
    @NotBlank(message = "First Name must not be empty")
    lateinit var firstName: String

    @Column
    @Size(min = 1, message = "Middle name cannot be blank if provided")
    var middleName: String? = null

    @Column(length = 10)
    @Size(min = 1, message = "Suffix cannot be blank if provided")
    var suffix: String? = null

    @Column(length = 13)
    @Size(min = 11, max = 25, message = "Must be a valid contact no.")
    var contactNo: String? = null

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    lateinit var status: EmployeeStatus

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    lateinit var createdAt: Instant

    @UpdateTimestamp
    @Column(nullable = false)
    lateinit var updatedAt: Instant

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    var user: User? = null

    @Column(name = "user_id", insertable = false, updatable = false)
    var userId: UUID? = null

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "actor_id", nullable = false)
    @JsonIgnore
    lateinit var actor: Actor

    @Column(name = "actor_id", insertable = false, updatable = false)
    var actorId: UUID? = null

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
