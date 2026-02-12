package com.gms.backend.domain.domain.repository.asset

import com.gms.backend.domain.domain.model.asset.SuppliesLog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface SuppliesLogRepository : JpaRepository<SuppliesLog, UUID>{
    fun findAllBySuppliesId(suppliesId: UUID, pageable: Pageable): Page<SuppliesLog>
}
