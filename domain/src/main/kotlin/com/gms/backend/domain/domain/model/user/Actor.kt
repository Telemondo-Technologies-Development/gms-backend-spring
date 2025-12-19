package com.gms.backend.domain.domain.model.user

import com.gms.backend.domain.domain.model.asset.Asset
import com.gms.backend.domain.domain.model.asset.AssetMaintenance
import com.gms.backend.domain.domain.model.asset.SuppliesLog
import com.gms.backend.domain.domain.model.asset.Supply
import com.gms.backend.domain.domain.model.billing.Invoice
import com.gms.backend.domain.domain.model.billing.Ledger
import com.gms.backend.domain.domain.model.billing.Payment
import com.gms.backend.domain.domain.model.branch.Branch
import com.gms.backend.domain.domain.model.branch.BranchPersonnel
import com.gms.backend.domain.domain.model.expense.*
import com.gms.backend.domain.domain.model.member.Attendance
import com.gms.backend.domain.domain.model.member.Member
import com.gms.backend.domain.domain.model.member.MemberProgress
import com.gms.backend.domain.domain.model.member.MemberSubscription
import com.gms.backend.domain.domain.model.member.report.Report
import com.gms.backend.domain.domain.model.storage.ObjectStorage
import com.gms.backend.domain.domain.model.subscription.BillingCycle
import com.gms.backend.domain.domain.model.subscription.Subscription
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.*

@Entity
@Table(name = "actors")
class Actor {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "binary(16)")
    @GeneratedValue
    @UuidGenerator
    var id: UUID? = null

    @Column(nullable = false)
    var type: String? = null

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    var createdAt: Instant? = null

    @Column
    var deactivatedAt: Instant? = null

    @OneToMany(mappedBy = "createdBy")
    var createdByObjectStorages = mutableSetOf<ObjectStorage>()

    @OneToMany(mappedBy = "updatedBy")
    var updatedByObjectStorages = mutableSetOf<ObjectStorage>()

    @OneToMany(mappedBy = "createdBy")
    var createdByBranches = mutableSetOf<Branch>()

    @OneToMany(mappedBy = "updatedBy")
    var updatedByBranches = mutableSetOf<Branch>()

    @OneToMany(mappedBy = "actor")
    var actorUsers = mutableSetOf<User>()

    @OneToMany(mappedBy = "actor")
    var actorEmployees = mutableSetOf<Employee>()

    @OneToMany(mappedBy = "createdBy")
    var createdByRoles = mutableSetOf<Role>()

    @OneToMany(mappedBy = "updatedBy")
    var updatedByRoles = mutableSetOf<Role>()

    @OneToMany(mappedBy = "createdBy")
    var createdByMembers = mutableSetOf<Member>()

    @OneToMany(mappedBy = "updatedBy")
    var updatedByMembers = mutableSetOf<Member>()

    @OneToMany(mappedBy = "actor")
    var actorMembers = mutableSetOf<Member>()

    @OneToMany(mappedBy = "createdBy")
    var createdByMemberProgresses = mutableSetOf<MemberProgress>()

    @OneToMany(mappedBy = "updatedBy")
    var updatedByMemberProgresses = mutableSetOf<MemberProgress>()

    @OneToMany(mappedBy = "actor")
    var actorMemberProgresses = mutableSetOf<MemberProgress>()

    @OneToMany(mappedBy = "actor")
    var actorAttendances = mutableSetOf<Attendance>()

    @OneToMany(mappedBy = "createdBy")
    var createdByBillingCycles = mutableSetOf<BillingCycle>()

    @OneToMany(mappedBy = "updatedBy")
    var updatedByBillingCycles = mutableSetOf<BillingCycle>()

    @OneToMany(mappedBy = "createdBy")
    var createdBySubscriptions = mutableSetOf<Subscription>()

    @OneToMany(mappedBy = "updatedBy")
    var updatedBySubscriptions = mutableSetOf<Subscription>()

    @OneToMany(mappedBy = "actor")
    var actorMemberSubscriptions = mutableSetOf<MemberSubscription>()

    @OneToMany(mappedBy = "createdBy")
    var createdByMemberSubscriptions = mutableSetOf<MemberSubscription>()

    @OneToMany(mappedBy = "updatedBy")
    var updatedByMemberSubscriptions = mutableSetOf<MemberSubscription>()

    @OneToMany(mappedBy = "actor")
    var actorInvoices = mutableSetOf<Invoice>()

    @OneToMany(mappedBy = "createdBy")
    var createdByInvoices = mutableSetOf<Invoice>()

    @OneToMany(mappedBy = "updatedBy")
    var updatedByInvoices = mutableSetOf<Invoice>()

    @OneToMany(mappedBy = "createdBy")
    var createdByPayments = mutableSetOf<Payment>()

    @OneToMany(mappedBy = "updatedBy")
    var updatedByPayments = mutableSetOf<Payment>()

    @OneToMany(mappedBy = "actor")
    var actorLedgers = mutableSetOf<Ledger>()

    @OneToMany(mappedBy = "createdBy")
    var createdByLedgers = mutableSetOf<Ledger>()

    @OneToMany(mappedBy = "actor")
    var actorBranchPersonnel = mutableSetOf<BranchPersonnel>()

    @OneToMany(mappedBy = "createdBy")
    var createdByBranchPersonnel = mutableSetOf<BranchPersonnel>()

    @OneToMany(mappedBy = "updatedBy")
    var updatedByBranchPersonnel = mutableSetOf<BranchPersonnel>()

    @OneToMany(mappedBy = "createdBy")
    var createdByAssets = mutableSetOf<Asset>()

    @OneToMany(mappedBy = "updatedBy")
    var updatedByAssets = mutableSetOf<Asset>()

    @OneToMany(mappedBy = "createdBy")
    var createdByAssetMaintenances = mutableSetOf<AssetMaintenance>()

    @OneToMany(mappedBy = "updatedBy")
    var updatedByAssetMaintenances = mutableSetOf<AssetMaintenance>()

    @OneToMany(mappedBy = "createdBy")
    var createdBySupplies = mutableSetOf<Supply>()

    @OneToMany(mappedBy = "updatedBy")
    var updatedBySupplies = mutableSetOf<Supply>()

    @OneToMany(mappedBy = "createdBy")
    var createdBySuppliesLogs = mutableSetOf<SuppliesLog>()

    @OneToMany(mappedBy = "updatedBy")
    var updatedBySuppliesLogs = mutableSetOf<SuppliesLog>()

    @OneToMany(mappedBy = "createdBy")
    var createdByAssetMaintenanceExpenses = mutableSetOf<AssetMaintenanceExpense>()

    @OneToMany(mappedBy = "updatedBy")
    var updatedByAssetMaintenanceExpenses = mutableSetOf<AssetMaintenanceExpense>()

    @OneToMany(mappedBy = "actor")
    var actorSalaryExpenses = mutableSetOf<SalaryExpense>()

    @OneToMany(mappedBy = "createdBy")
    var createdBySalaryExpenses = mutableSetOf<SalaryExpense>()

    @OneToMany(mappedBy = "updatedBy")
    var updatedBySalaryExpenses = mutableSetOf<SalaryExpense>()

    @OneToMany(mappedBy = "createdBy")
    var createdByUtilityExpenses = mutableSetOf<UtilityExpense>()

    @OneToMany(mappedBy = "updatedBy")
    var updatedByUtilityExpenses = mutableSetOf<UtilityExpense>()

    @OneToMany(mappedBy = "createdBy")
    var createdByOtherExpenses = mutableSetOf<OtherExpense>()

    @OneToMany(mappedBy = "updatedBy")
    var updatedByOtherExpenses = mutableSetOf<OtherExpense>()

    @OneToMany(mappedBy = "createdBy")
    var createdByAssetExpenses = mutableSetOf<AssetExpense>()

    @OneToMany(mappedBy = "updatedBy")
    var updatedByAssetExpenses = mutableSetOf<AssetExpense>()

    @OneToMany(mappedBy = "createdBy")
    var createdBySuppliesExpenses = mutableSetOf<SuppliesExpense>()

    @OneToMany(mappedBy = "updatedBy")
    var updatedBySuppliesExpenses = mutableSetOf<SuppliesExpense>()

    @OneToMany(mappedBy = "actor")
    var actorReports = mutableSetOf<Report>()

    @OneToMany(mappedBy = "createdBy")
    var createdByReports = mutableSetOf<Report>()

    @OneToMany(mappedBy = "updatedBy")
    var updatedByReports = mutableSetOf<Report>()
}
