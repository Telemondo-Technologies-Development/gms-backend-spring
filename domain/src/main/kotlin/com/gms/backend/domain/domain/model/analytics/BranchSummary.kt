package com.gms.backend.domain.domain.model.analytics

import com.gms.backend.domain.domain.model.branch.Branch
import jakarta.persistence.*
import org.hibernate.annotations.Immutable
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@Embeddable
data class BranchFinancialId(
    @Column(name = "branch_id", columnDefinition = "BINARY(16)")
    val branchId: UUID,

    @Column(name = "report_month")
    val reportMonth: LocalDate
) : Serializable

@Entity
@Immutable
@Table(name = "branch_financial_summary")
class BranchSummary {

    @EmbeddedId
    lateinit var id: BranchFinancialId

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", insertable = false, updatable = false)
    lateinit var branch: Branch

    @Column(nullable = false)
    val reportYear: Int = -1

    @Column(nullable = false)
    val reportQuarter: Int = -1

    @Column(precision = 10, scale = 2)
    val totalRevenue: BigDecimal = BigDecimal.ZERO

    @Column(precision = 10, scale = 2)
    val avgInvoice: BigDecimal = BigDecimal.ZERO

    @Column(precision = 10, scale = 2)
    val totalExpenses: BigDecimal = BigDecimal.ZERO

    @Column(precision = 10, scale = 2)
    val avgExpense: BigDecimal = BigDecimal.ZERO
}