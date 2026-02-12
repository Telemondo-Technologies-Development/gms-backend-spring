package com.gms.backend.domain.domain.repository.asset

import com.gms.backend.domain.domain.model.asset.AssetMaintenance
import com.gms.backend.domain.domain.model.user.Actor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.Instant
import java.util.*

interface AssetMaintenanceRepository : JpaRepository<AssetMaintenance, UUID> {

    fun existsByMaintenanceScheduleIdAndMaintenanceDate(
        scheduleId: UUID,
        maintenanceDate: Instant
    ): Boolean

    @Modifying
    @Query("""
        UPDATE AssetMaintenance am 
        SET am.status = com.gms.backend.domain.domain.model.asset.AssetMaintenance.AssetMaintenanceStatus.OVERDUE, 
            am.updatedBy = :systemActor, 
            am.updatedAt = :now
        WHERE am.maintenanceSchedule.id = :scheduleId 
          AND am.maintenanceDate = :targetDate 
          AND am.status = com.gms.backend.domain.domain.model.asset.AssetMaintenance.AssetMaintenanceStatus.PENDING
    """)
    fun updateStatusToOverdue(
        @Param("scheduleId") scheduleId: UUID,
        @Param("targetDate") targetDate: Instant,
        @Param("systemActor") systemActor: Actor,
        @Param("now") now: Instant
    ): Int
}