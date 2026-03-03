package com.gms.backend.domain.domain.model.analytics

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "analytics_metadata")
class AnalyticsMetadata(
    @Id
    @Column(name = "report_name")
    val reportName: String,

    @Column(name = "last_refreshed_at", nullable = false)
    val lastRefreshedAt: Instant
)