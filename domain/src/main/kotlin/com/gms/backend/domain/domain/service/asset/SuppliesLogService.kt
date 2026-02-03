package com.gms.backend.domain.domain.service.asset

import com.gms.backend.domain.application.rest.asset.SuppliesLogController
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface SuppliesLogService {
    fun createSuppliesLog(body: SuppliesLogController.SuppliesLogPostDTO): SuppliesLogController.SuppliesLogTableDTO
    fun updateSuppliesLog(id: UUID, body: SuppliesLogController.SuppliesLogPutDTO): SuppliesLogController.SuppliesLogTableDTO
    fun getSuppliesLogs(pageable: Pageable): Page<SuppliesLogController.SuppliesLogTableDTO>
    fun getSuppliesLogById(id: UUID): SuppliesLogController.SuppliesLogTableDTO
    fun deleteSuppliesLog(id: UUID)
}