package com.gms.backend.domain.domain.model.user

import com.fasterxml.jackson.annotation.JsonIgnore
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

    enum class ActorType {
        // Human
        USER,
        EMPLOYEE,
        MEMBER,
        ADMIN,
        // Machine
        SYSTEM,
        CRON,
        API,
    }

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "binary(16)")
    @GeneratedValue
    @UuidGenerator
    lateinit var id: UUID

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    lateinit var type: ActorType

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    var createdAt: Instant? = null

    @Column
    var deactivatedAt: Instant? = null

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    var createdByObjectStorages = mutableSetOf<ObjectStorage>()

    @OneToMany(mappedBy = "updatedBy")
    @JsonIgnore
    var updatedByObjectStorages = mutableSetOf<ObjectStorage>()

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    var createdByBranches = mutableSetOf<Branch>()

    @OneToMany(mappedBy = "updatedBy")
    @JsonIgnore
    var updatedByBranches = mutableSetOf<Branch>()

    @OneToMany(mappedBy = "actor")
    @JsonIgnore
    var actorUsers = mutableSetOf<User>()

    @OneToMany(mappedBy = "actor")
    @JsonIgnore
    var actorEmployees = mutableSetOf<Employee>()

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    var createdByRoles = mutableSetOf<Role>()

    @OneToMany(mappedBy = "updatedBy")
    @JsonIgnore
    var updatedByRoles = mutableSetOf<Role>()

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    var createdByMembers = mutableSetOf<Member>()

    @OneToMany(mappedBy = "updatedBy")
    @JsonIgnore
    var updatedByMembers = mutableSetOf<Member>()

    @OneToMany(mappedBy = "actor")
    @JsonIgnore
    var actorMembers = mutableSetOf<Member>()

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    var createdByMemberProgresses = mutableSetOf<MemberProgress>()

    @OneToMany(mappedBy = "updatedBy")
    @JsonIgnore
    var updatedByMemberProgresses = mutableSetOf<MemberProgress>()

    @OneToMany(mappedBy = "actor")
    @JsonIgnore
    var actorMemberProgresses = mutableSetOf<MemberProgress>()

    @OneToMany(mappedBy = "actor")
    @JsonIgnore
    var actorAttendances = mutableSetOf<Attendance>()

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    var createdByBillingCycles = mutableSetOf<BillingCycle>()

    @OneToMany(mappedBy = "updatedBy")
    @JsonIgnore
    var updatedByBillingCycles = mutableSetOf<BillingCycle>()

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    var createdBySubscriptions = mutableSetOf<Subscription>()

    @OneToMany(mappedBy = "updatedBy")
    @JsonIgnore
    var updatedBySubscriptions = mutableSetOf<Subscription>()

    @OneToMany(mappedBy = "actor")
    @JsonIgnore
    var actorMemberSubscriptions = mutableSetOf<MemberSubscription>()

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    var createdByMemberSubscriptions = mutableSetOf<MemberSubscription>()

    @OneToMany(mappedBy = "updatedBy")
    @JsonIgnore
    var updatedByMemberSubscriptions = mutableSetOf<MemberSubscription>()

    @OneToMany(mappedBy = "actor")
    @JsonIgnore
    var actorInvoices = mutableSetOf<Invoice>()

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    var createdByInvoices = mutableSetOf<Invoice>()

    @OneToMany(mappedBy = "updatedBy")
    @JsonIgnore
    var updatedByInvoices = mutableSetOf<Invoice>()

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    var createdByPayments = mutableSetOf<Payment>()

    @OneToMany(mappedBy = "updatedBy")
    @JsonIgnore
    var updatedByPayments = mutableSetOf<Payment>()

    @OneToMany(mappedBy = "actor")
    @JsonIgnore
    var actorLedgers = mutableSetOf<Ledger>()

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    var createdByLedgers = mutableSetOf<Ledger>()

    @OneToMany(mappedBy = "actor")
    @JsonIgnore
    var actorBranchPersonnel = mutableSetOf<BranchPersonnel>()

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    var createdByBranchPersonnel = mutableSetOf<BranchPersonnel>()

    @OneToMany(mappedBy = "updatedBy")
    @JsonIgnore
    var updatedByBranchPersonnel = mutableSetOf<BranchPersonnel>()

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    var createdByAssets = mutableSetOf<Asset>()

    @OneToMany(mappedBy = "updatedBy")
    @JsonIgnore
    var updatedByAssets = mutableSetOf<Asset>()

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    var createdByAssetMaintenances = mutableSetOf<AssetMaintenance>()

    @OneToMany(mappedBy = "updatedBy")
    @JsonIgnore
    var updatedByAssetMaintenances = mutableSetOf<AssetMaintenance>()

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    var createdBySupplies = mutableSetOf<Supply>()

    @OneToMany(mappedBy = "updatedBy")
    @JsonIgnore
    var updatedBySupplies = mutableSetOf<Supply>()

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    var createdBySuppliesLogs = mutableSetOf<SuppliesLog>()

    @OneToMany(mappedBy = "updatedBy")
    @JsonIgnore
    var updatedBySuppliesLogs = mutableSetOf<SuppliesLog>()

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    var createdByAssetMaintenanceExpenses = mutableSetOf<AssetMaintenanceExpense>()

    @OneToMany(mappedBy = "updatedBy")
    @JsonIgnore
    var updatedByAssetMaintenanceExpenses = mutableSetOf<AssetMaintenanceExpense>()

    @OneToMany(mappedBy = "actor")
    @JsonIgnore
    var actorSalaryExpenses = mutableSetOf<SalaryExpense>()

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    var createdBySalaryExpenses = mutableSetOf<SalaryExpense>()

    @OneToMany(mappedBy = "updatedBy")
    @JsonIgnore
    var updatedBySalaryExpenses = mutableSetOf<SalaryExpense>()

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    var createdByUtilityExpenses = mutableSetOf<UtilityExpense>()

    @OneToMany(mappedBy = "updatedBy")
    @JsonIgnore
    var updatedByUtilityExpenses = mutableSetOf<UtilityExpense>()

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    var createdByOtherExpenses = mutableSetOf<OtherExpense>()

    @OneToMany(mappedBy = "updatedBy")
    @JsonIgnore
    var updatedByOtherExpenses = mutableSetOf<OtherExpense>()

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    var createdByAssetExpenses = mutableSetOf<AssetExpense>()

    @OneToMany(mappedBy = "updatedBy")
    @JsonIgnore
    var updatedByAssetExpenses = mutableSetOf<AssetExpense>()

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    var createdBySuppliesExpenses = mutableSetOf<SuppliesExpense>()

    @OneToMany(mappedBy = "updatedBy")
    @JsonIgnore
    var updatedBySuppliesExpenses = mutableSetOf<SuppliesExpense>()

    @OneToMany(mappedBy = "actor")
    @JsonIgnore
    var actorReports = mutableSetOf<Report>()

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    var createdByReports = mutableSetOf<Report>()

    @OneToMany(mappedBy = "updatedBy")
    @JsonIgnore
    var updatedByReports = mutableSetOf<Report>()
}
