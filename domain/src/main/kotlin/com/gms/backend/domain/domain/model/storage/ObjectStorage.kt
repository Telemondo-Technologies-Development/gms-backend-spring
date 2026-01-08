package com.gms.backend.domain.domain.model.storage

import com.gms.backend.domain.domain.model.asset.Asset
import com.gms.backend.domain.domain.model.asset.AssetMaintenance
import com.gms.backend.domain.domain.model.billing.PaymentMethod
import com.gms.backend.domain.domain.model.branch.Branch
import com.gms.backend.domain.domain.model.expense.*
import com.gms.backend.domain.domain.model.member.Member
import com.gms.backend.domain.domain.model.member.report.Report
import com.gms.backend.domain.domain.model.user.Actor
import com.gms.backend.domain.domain.model.user.Employee
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.*

@Entity
@Table(name = "object_storage")
class ObjectStorage {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "binary(16)")
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    lateinit var id: UUID

    @Column(nullable = false)
    var bucket: String? = null

    @Column(nullable = false, length = 1024)
    var fileKey: String? = null

    @Column(nullable = false)
    var name: String? = null

    @Column(nullable = false)
    var fileSize: String? = null

    @Column(nullable = false)
    var mimeType: String? = null

    @Column(nullable = false)
    var tags: String? = null

    @Column(nullable = false)
    var status: Int? = null

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    lateinit var createdAt: Instant

    @UpdateTimestamp
    @Column(nullable = false)
    lateinit var updatedAt: Instant

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    var createdBy: Actor? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", nullable = false)
    var updatedBy: Actor? = null

    @OneToMany(mappedBy = "profilePicture")
    var profilePictureBranches = mutableSetOf<Branch>()

    @OneToMany(mappedBy = "profilePicture")
    var profilePictureEmployees = mutableSetOf<Employee>()

    @ManyToMany(mappedBy = "employeeObjects")
    var employees = mutableSetOf<Employee>()

    @OneToMany(mappedBy = "profilePicture")
    var profilePictureMembers = mutableSetOf<Member>()

    @ManyToMany(mappedBy = "memberObjects")
    var members = mutableSetOf<Member>()

    @ManyToMany(mappedBy = "assetObjects")
    var assets = mutableSetOf<Asset>()

    @ManyToMany(mappedBy = "assetMaintenanceObjects")
    var assetMaintenance = mutableSetOf<AssetMaintenance>()

    @ManyToMany(mappedBy = "assetMaintenanceExpensesObjects")
    var assetMaintenanceExpenses =
        mutableSetOf<AssetMaintenanceExpense>()

    @ManyToMany(mappedBy = "salaryExpensesObjects")
    var salaryExpenses = mutableSetOf<SalaryExpense>()

    @ManyToMany(mappedBy = "utilityExpensesObjects")
    var utilityExpenses = mutableSetOf<UtilityExpense>()

    @ManyToMany(mappedBy = "otherExpensesObjects")
    var otherExpenses = mutableSetOf<OtherExpense>()

    @ManyToMany(mappedBy = "assetExpensesObjects")
    var assetExpenses = mutableSetOf<AssetExpense>()

    @ManyToMany(mappedBy = "suppliesExpensesObjects")
    var suppliesExpenses = mutableSetOf<SuppliesExpense>()

    @ManyToMany(mappedBy = "reportsObjects")
    var reports = mutableSetOf<Report>()

    @ManyToMany(mappedBy = "paymentMethodObjects")
    var paymentMethods = mutableSetOf<PaymentMethod>()
}