package com.gms.backend.domain.domain.repository.asset

import com.gms.backend.domain.domain.model.asset.Asset
import com.gms.backend.domain.domain.model.asset.MaintenanceSchedule
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.Instant
import java.util.*

data class ScheduleWithLatestMaintenanceDTO(
    val scheduleId: UUID,
    val asset: Asset,
    val startDate: Instant,
    val intervalValue: Int,
    val intervalUnit: java.time.temporal.ChronoUnit,
    val leadTimeHours: Int,
    val timeToCompleteHours: Int,
    val dayOfWeek: Int?,
    val weekRank: Int?,
    val monthOfYear: Int?,
    val latestMaintenanceDate: Instant?
)

interface MaintenanceScheduleRepository : JpaRepository<MaintenanceSchedule, UUID> {
    @Query("""
        SELECT new com.gms.backend.domain.domain.repository.asset.ScheduleWithLatestMaintenanceDTO(
            ms.id, 
            ms.asset, 
            ms.startDate, 
            ms.intervalValue, 
            ms.intervalUnit, 
            ms.leadTimeHours, 
            ms.timeToCompleteHours, 
            ms.dayOfWeek, 
            ms.weekRank, 
            ms.monthOfYear,
            MAX(am.maintenanceDate)
        )
        FROM MaintenanceSchedule ms 
        LEFT JOIN AssetMaintenance am ON am.maintenanceSchedule = ms 
        WHERE ms.isActive = true 
        GROUP BY 
            ms.id, 
            ms.asset, 
            ms.startDate, 
            ms.intervalValue, 
            ms.intervalUnit, 
            ms.leadTimeHours, 
            ms.timeToCompleteHours,
            ms.dayOfWeek,
            ms.weekRank,
            ms.monthOfYear
    """)
    fun findAllWithLatestMaintenance(): List<ScheduleWithLatestMaintenanceDTO>
}