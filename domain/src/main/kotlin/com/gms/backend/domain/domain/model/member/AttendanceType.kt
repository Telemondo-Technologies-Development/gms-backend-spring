package com.gms.backend.domain.domain.model.member

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.util.*

@Entity
@Table(name = "attendance_types")
class AttendanceType {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "binary(16)")
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    var id: UUID? = null

    @Column(nullable = false, unique = true)
    var name: String? = null

    @OneToMany(mappedBy = "attendanceType")
    var attendanceTypeAttendances = mutableSetOf<Attendance>()
}