package com.gms.backend.domain.application.rest.analytics

import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.domain.service.analytics.BranchSummaryService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@RestController
@RequestMapping("/api/branch-summary")
@Tag(name = "Branch Summary")
class BranchSummaryController(private val branchSummaryService: BranchSummaryService) {

    @Schema(description = "Format for Branch Summary read")
    data class BranchSummaryTableDTO(
        val branchId: UUID,
        val branchName: String?,  // Optional: If you join with the Branch entity
        val reportYear: Int,
        val reportQuarter: Int,
        val reportMonth: LocalDate,
        val totalRevenue: BigDecimal,
        val avgInvoice: BigDecimal,
        val totalExpenses: BigDecimal,
        val avgExpense: BigDecimal,
        val netProfit: BigDecimal // Calculated field: Revenue - Expenses
    )

    @GetMapping
    @Operation(summary = "Get all Branch Summary")
    fun getAllBranchSummary(pageable: Pageable, @RequestParam year: Int?, @RequestParam month: Int?) =
        branchSummaryService.getBranchSummary(pageable, year, month).toPaginatedResponse()

    @GetMapping("/{id}")
    @Operation(summary = "Get Branch Summary by Branch id")
    fun getBranchSummary(
        pageable: Pageable,
        @PathVariable id: UUID,
        @RequestParam year: Int?,
        @RequestParam month: Int?
    ) =
        branchSummaryService.getBranchSummaryById(pageable, id, year, month).toPaginatedResponse()

    @GetMapping("/last-update")
    @Operation(summary = "Get the time for the last Branch Summary Update")
    fun getBranchSummary() = branchSummaryService.lastUpdated().toOkResponse()

    @PutMapping("")
    @Operation(summary = "Update Branch Summary")
    fun updateBranchSummary() =
        branchSummaryService.updateBranchSummary().toOkResponse("BranchSummary updated")
}