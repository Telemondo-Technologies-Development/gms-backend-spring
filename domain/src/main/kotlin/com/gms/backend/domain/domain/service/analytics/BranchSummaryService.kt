package com.gms.backend.domain.domain.service.analytics

import com.gms.backend.domain.application.rest.analytics.BranchSummaryController
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.Instant
import java.util.*

interface BranchSummaryService {
    fun getBranchSummary(pageable: Pageable, year: Int?, month: Int?): Page<BranchSummaryController.BranchSummaryTableDTO>
    fun getBranchSummaryById(pageable: Pageable, id: UUID, year: Int?, month: Int?): Page<BranchSummaryController.BranchSummaryTableDTO>
    fun updateBranchSummary()
    fun lastUpdated(): Instant
}