package com.gms.backend.domain.domain.model.storage

import com.fasterxml.jackson.annotation.JsonIgnore
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
    lateinit var bucket: String

    @Column(nullable = false, length = 1024)
    lateinit var fileKey: String

    @Column(nullable = false)
    lateinit var name: String

    @Column(nullable = false)
    lateinit var fileSize: String

    @Column(nullable = false)
    lateinit var mimeType: String

    @Column(nullable = false)
    lateinit var tags: String

    @Column(nullable = false)
    var status: Int = 0

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    lateinit var createdAt: Instant

    @UpdateTimestamp
    @Column(nullable = false)
    lateinit var updatedAt: Instant

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    @JsonIgnore
    lateinit var createdBy: Actor

    @Column(name = "created_by", insertable = false, updatable = false)
    var createdById: UUID? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", nullable = false)
    @JsonIgnore
    lateinit var updatedBy: Actor

    @Column(name = "updated_by", insertable = false, updatable = false)
    var updatedById: UUID? = null

    @OneToMany(mappedBy = "profilePicture")
    @JsonIgnore
    var profilePictureBranches = mutableSetOf<Branch>()

    @OneToMany(mappedBy = "profilePicture")
    @JsonIgnore
    var profilePictureEmployees = mutableSetOf<Employee>()

    @ManyToMany(mappedBy = "employeeObjects")
    @JsonIgnore
    var employees = mutableSetOf<Employee>()

    @OneToMany(mappedBy = "profilePicture")
    @JsonIgnore
    var profilePictureMembers = mutableSetOf<Member>()

    @ManyToMany(mappedBy = "memberObjects")
    @JsonIgnore
    var members = mutableSetOf<Member>()

    @ManyToMany(mappedBy = "assetObjects")
    @JsonIgnore
    var assets = mutableSetOf<Asset>()

    @ManyToMany(mappedBy = "assetMaintenanceObjects")
    @JsonIgnore
    var assetMaintenance = mutableSetOf<AssetMaintenance>()

    @ManyToMany(mappedBy = "assetMaintenanceExpensesObjects")
    @JsonIgnore
    var assetMaintenanceExpenses =
        mutableSetOf<AssetMaintenanceExpense>()

    @ManyToMany(mappedBy = "salaryExpensesObjects")
    @JsonIgnore
    var salaryExpenses = mutableSetOf<SalaryExpense>()

    @ManyToMany(mappedBy = "utilityExpensesObjects")
    @JsonIgnore
    var utilityExpenses = mutableSetOf<UtilityExpense>()

    @ManyToMany(mappedBy = "otherExpensesObjects")
    @JsonIgnore
    var otherExpenses = mutableSetOf<OtherExpense>()

    @ManyToMany(mappedBy = "assetExpensesObjects")
    @JsonIgnore
    var assetExpenses = mutableSetOf<AssetExpense>()

    @ManyToMany(mappedBy = "suppliesExpensesObjects")
    @JsonIgnore
    var suppliesExpenses = mutableSetOf<SuppliesExpense>()

    @ManyToMany(mappedBy = "reportsObjects")
    @JsonIgnore
    var reports = mutableSetOf<Report>()

    @ManyToMany(mappedBy = "paymentMethodObjects")
    @JsonIgnore
    var paymentMethods = mutableSetOf<PaymentMethod>()
}