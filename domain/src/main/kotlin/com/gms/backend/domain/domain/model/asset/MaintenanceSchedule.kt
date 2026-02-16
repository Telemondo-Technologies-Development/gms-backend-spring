package com.gms.backend.domain.domain.model.asset

import com.gms.backend.domain.domain.model.branch.BranchPersonnel.BranchPersonnelStatus
import com.gms.backend.domain.domain.model.user.Actor
import jakarta.persistence.*
import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.*

@Entity
@Table(name = "maintenance_schedules")
class MaintenanceSchedule {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "binary(16)")
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    lateinit var id: UUID

    @Column(nullable = false)
    @field: NotBlank(message = "Name must not be empty")
    lateinit var name: String

    @Column(nullable = false)
    @field: NotNull(message = "Start date is required")
    lateinit var startDate: Instant

    @get:AssertTrue(message = "New schedules must start in the present or future")
    val isStartDateValid: Boolean
        get() {
            // only for new records
            if (!this::id.isInitialized) {
                return startDate.isAfter(Instant.now().minusSeconds(60))
            }
            return true
        }

    @Enumerated(EnumType.STRING)
    @Column(name = "interval_unit", nullable = false)
    @field:NotNull(message = "Interval unit must be specified")
    lateinit var intervalUnit: java.time.temporal.ChronoUnit

    @Column(name = "interval_value", nullable = false)
    @field: Positive(message = "Interval value must be greater than zero")
    var intervalValue: Int = 0

    @Column(name = "lead_time_hours", nullable = false)
    @field: PositiveOrZero(message = "Lead time cannot be negative")
    var leadTimeHours: Int = 0

    @Column(name = "time_to_complete_hours", nullable = false)
    @field: PositiveOrZero(message = "Time to complete cannot be negative")
    var timeToCompleteHours: Int = 0

    @Column(name = "week_rank")
    @field:Min(-1, message = "Week rank must be between -1 and 5")
    @field:Max(5, message = "Week rank must be between -1 and 5")
    var weekRank: Int? = null

    @Column(name = "day_of_week")
    @field:Min(1, message = "Day of week must be between 1 and 7")
    @field:Max(7, message = "Day of week must be between 1 and 7")
    var dayOfWeek: Int? = null

    @Column(name = "month_of_year")
    @field:Min(1, message = "Month of year must be between 1 and 12")
    @field:Max(12, message = "Month of year must be between 1 and 12")
    var monthOfYear: Int? = null

    @Column(name = "is_active", nullable = false)
    var isActive: Boolean = true

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", nullable = false)
    lateinit var asset: Asset

    @Column(name = "asset_id", insertable = false, updatable = false)
    var assetId: UUID? = null

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    lateinit var createdAt: Instant

    @UpdateTimestamp
    @Column(nullable = false)
    lateinit var updatedAt: Instant

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

    @PrePersist
    @PreUpdate
    fun normalizeStartDate() {
        this.startDate = this.startDate.truncatedTo(java.time.temporal.ChronoUnit.MINUTES)
    }

    @get:AssertTrue(message = "Advanced settings require a MONTHS or YEARS interval unit")
    val isAdvancedSettingsAllowed: Boolean
        get() {
            val hasAdvancedFields = weekRank != null || dayOfWeek != null || monthOfYear != null
            val isCorrectUnit = intervalUnit == java.time.temporal.ChronoUnit.MONTHS ||
                    intervalUnit == java.time.temporal.ChronoUnit.YEARS

            return if (hasAdvancedFields) isCorrectUnit else true
        }
}
