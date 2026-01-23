package com.gms.backend.domain.domain.repository.asset

import com.gms.backend.domain.domain.model.asset.AssetMaintenance
import com.gms.backend.domain.domain.model.asset.MaintenanceSchedule
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant
import java.util.*

interface AssetMaintenanceRepository : JpaRepository<AssetMaintenance, UUID> {

    // Prevents duplicate records for the same schedule during catch-up (when server is down)
    fun existsByMaintenanceScheduleAndMaintenanceDateBetween(
        maintenanceSchedule: MaintenanceSchedule,
        start: Instant,
        end: Instant
    ): Boolean

    // Find tasks by status (to be updated to OVERDUE)
    fun findAllByStatus(status: AssetMaintenance.AssetMaintenanceStatus): List<AssetMaintenance>

    // Gets the most recent log for this schedule (used to calculate next due date)
    fun findFirstByMaintenanceScheduleOrderByMaintenanceDateDesc(schedule: MaintenanceSchedule): AssetMaintenance?

    fun findAllByAssetMaintenanceObjectsId(id: UUID): List<AssetMaintenance>
}