package com.gms.backend.domain.domain.repository.analytics

import com.gms.backend.domain.domain.model.analytics.AnalyticsMetadata
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface AnalyticsMetadataRepository : JpaRepository<AnalyticsMetadata, String> {
    fun findByReportName(reportName: String): Optional<AnalyticsMetadata>
}