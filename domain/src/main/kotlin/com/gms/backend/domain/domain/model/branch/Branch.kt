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
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.*

@Entity
@Table(name = "branch")
class Branch {

    enum class BranchStatus {
        IN,
        OUT,
        UNDECIDED,
    }

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "binary(16)")
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    lateinit var id: UUID

    @Column(nullable = false, unique = true)
    lateinit var name: String

    @Column(nullable = false, columnDefinition = "longtext")
    var address: String? = null

    @Column(nullable = false)
    var longitude: String? = null

    @Column(nullable = false)
    var latitude: String? = null

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
    var createdBy: Actor? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", nullable = false)
    var updatedBy: Actor? = null

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
