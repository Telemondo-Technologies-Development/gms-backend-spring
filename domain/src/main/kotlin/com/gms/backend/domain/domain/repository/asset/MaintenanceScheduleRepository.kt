package com.gms.backend.domain.domain.repository.asset

import com.gms.backend.domain.domain.model.asset.MaintenanceSchedule
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MaintenanceScheduleRepository : JpaRepository<MaintenanceSchedule, UUID>
