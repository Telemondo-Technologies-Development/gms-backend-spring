package com.gms.backend.domain.domain.repository.asset

import com.gms.backend.domain.domain.model.asset.SuppliesLog
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SuppliesLogRepository : JpaRepository<SuppliesLog, UUID>
