package com.gms.backend.domain.impl.domain.service.analytics

import com.gms.backend.domain.application.rest.analytics.BranchSummaryController
import com.gms.backend.domain.domain.repository.analytics.AnalyticsMetadataRepository
import com.gms.backend.domain.domain.repository.analytics.BranchSummaryRepository
import com.gms.backend.domain.domain.service.analytics.BranchSummaryService
import org.quartz.JobKey
import org.quartz.Scheduler
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.LocalDate
import java.util.*

@Service
@PreAuthorize("denyAll()")
class BranchSummaryServiceImpl(
    private val branchSummaryRepository: BranchSummaryRepository,
    private val analyticsMetadataRepository: AnalyticsMetadataRepository,
    private val scheduler: Scheduler
): BranchSummaryService {
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('branchSummary_read')")
    override fun getBranchSummary(pageable: Pageable, year: Int?, month: Int?): Page<BranchSummaryController.BranchSummaryTableDTO> {
        val year = year ?: LocalDate.now().year
        return branchSummaryRepository.findAllProjectBy(pageable, year, month)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('branchSummary_read')")
    override fun getBranchSummaryById(pageable: Pageable, id: UUID, year: Int?, month: Int?): Page<BranchSummaryController.BranchSummaryTableDTO> {
        val year = year ?: LocalDate.now().year
        return branchSummaryRepository.findByBranchAndYear(pageable,id, year, month)
    }

    @Transactional
    @PreAuthorize("hasAuthority('branchSummary_update')")
    override fun updateBranchSummary() {
        scheduler.triggerJob(JobKey("refreshBranchSummaryJob"))
    }

    @Transactional
    @PreAuthorize("hasAuthority('branchSummary_read')")
    override fun lastUpdated(): Instant {
        val lastRefreshedAt = analyticsMetadataRepository.findByReportName("FINANCIAL_SUMMARY").orElseThrow()
        return lastRefreshedAt.lastRefreshedAt
    }
}