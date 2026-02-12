package com.gms.backend.domain.domain.model.branch

import com.gms.backend.domain.domain.model.asset.Asset
import com.gms.backend.domain.domain.model.asset.Supply
import com.gms.backend.domain.domain.model.billing.Invoice
import com.gms.backend.domain.domain.model.billing.Ledger
import com.gms.backend.domain.domain.model.expense.*
import com.gms.backend.domain.domain.model.member.Attendance
import com.gms.backend.domain.domain.model.member.MemberProgress
import com.gms.backend.domain.domain.model.member.MemberSubscription
import com.gms.backend.domain.domain.model.member.report.Report
import com.gms.backend.domain.domain.model.storage.ObjectStorage
import com.gms.backend.domain.domain.model.user.Actor
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.*

@Entity
@Table(name = "branch")
class Branch {

    enum class BranchStatus {
        ACTIVE,
        CLOSED,
        UNDECIDED,
    }

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "binary(16)")
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    lateinit var id: UUID

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Name must not be empty")
    lateinit var name: String

    @Column(nullable = false, columnDefinition = "longtext")
    @NotBlank(message = "Address must not be empty")
    lateinit var address: String

    @Column(nullable = false)
    @Pattern(
        // Validates: Optional negative sign, 1-3 digits, a dot, and 6 to 15 decimal places
        regexp = "^-?\\d{1,3}\\.\\d{6,15}$",
        message = "Longitude must be in decimal format with at least 6 decimal places (e.g., 125.123456)"
    )
    lateinit var longitude: String

    @Column(nullable = false)
    @Pattern(
        // Validates: Optional negative sign, 1-3 digits, a dot, and 6 to 15 decimal places
        regexp = "^-?\\d{1,3}\\.\\d{6,15}$",
        message = "Latitude must be in decimal format with at least 6 decimal places (e.g., 125.123456)"
    )
    lateinit var latitude: String

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    lateinit var status: BranchStatus

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    lateinit var createdAt: Instant

    @UpdateTimestamp
    @Column(nullable = false)
    lateinit var updatedAt: Instant

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_picture", nullable = true)
    var profilePicture: ObjectStorage? = null

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

    @OneToMany(mappedBy = "branch")
    var branchMemberProgresses = mutableSetOf<MemberProgress>()

    @OneToMany(mappedBy = "branch")
    var branchAttendances = mutableSetOf<Attendance>()

    @OneToMany(mappedBy = "branch")
    var branchMemberSubscriptions = mutableSetOf<MemberSubscription>()

    @OneToMany(mappedBy = "branch")
    var branchInvoices = mutableSetOf<Invoice>()

    @OneToMany(mappedBy = "branch")
    var branchLedgers = mutableSetOf<Ledger>()

    @OneToMany(mappedBy = "branch")
    var branchBranchPersonnel = mutableSetOf<BranchPersonnel>()

    @OneToMany(mappedBy = "branch")
    var branchAssets = mutableSetOf<Asset>()

    @OneToMany(mappedBy = "branch")
    var branchSupplies = mutableSetOf<Supply>()

    @OneToMany(mappedBy = "branch")
    var branchAssetMaintenanceExpenses = mutableSetOf<AssetMaintenanceExpense>()

    @OneToMany(mappedBy = "branch")
    var branchSalaryExpenses = mutableSetOf<SalaryExpense>()

    @OneToMany(mappedBy = "branch")
    var branchUtilityExpenses = mutableSetOf<UtilityExpense>()

    @OneToMany(mappedBy = "branch")
    var branchOtherExpenses = mutableSetOf<OtherExpense>()

    @OneToMany(mappedBy = "branch")
    var branchAssetExpenses = mutableSetOf<AssetExpense>()

    @OneToMany(mappedBy = "branch")
    var branchSuppliesExpenses = mutableSetOf<SuppliesExpense>()

    @OneToMany(mappedBy = "branch")
    var branchReports = mutableSetOf<Report>()
}
