package com.gms.backend.domain.domain.repository.analytics

import com.gms.backend.domain.application.rest.analytics.BranchSummaryController
import com.gms.backend.domain.domain.model.analytics.BranchSummary
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface BranchSummaryRepository : JpaRepository<BranchSummary, UUID> {
    @Query(
        value = $$"""
        SELECT new com.gms.backend.domain.application.rest.analytics.BranchSummaryController$BranchSummaryTableDTO(
            s.id.branchId,
            s.branch.name, 
            s.reportYear,
            s.reportQuarter,
            s.id.reportMonth,
            s.totalRevenue,
            s.avgInvoice,
            s.totalExpenses,
            s.avgExpense,
            (s.totalRevenue - s.totalExpenses)
        )
        FROM BranchSummary s
        WHERE s.reportYear = :year
        AND (:month IS NULL OR MONTH(s.id.reportMonth) = :month)
        ORDER BY s.id.reportMonth DESC
        """,
        countQuery = """
        SELECT COUNT(s) FROM BranchSummary s 
            WHERE s.reportYear = :year 
            AND (:month IS NULL OR MONTH(s.id.reportMonth) = :month)
        """)
    fun findAllProjectBy(pageable: Pageable, year: Int, month: Int?): Page<BranchSummaryController.BranchSummaryTableDTO>

    @Query(
        value = $$"""
        SELECT new com.gms.backend.domain.application.rest.analytics.BranchSummaryController$BranchSummaryTableDTO(
            s.id.branchId,
            s.branch.name, 
            s.reportYear,
            s.reportQuarter,
            s.id.reportMonth,
            s.totalRevenue,
            s.avgInvoice,
            s.totalExpenses,
            s.avgExpense,
            (s.totalRevenue - s.totalExpenses)
        )
        FROM BranchSummary s
        WHERE s.id.branchId = :branchId
        AND s.reportYear = :year
        AND (:month IS NULL OR MONTH(s.id.reportMonth) = :month)
        ORDER BY s.id.reportMonth DESC
        """)
    fun findByBranchAndYear(pageable: Pageable, branchId: UUID, year: Int, month: Int?): Page<BranchSummaryController.BranchSummaryTableDTO>
}