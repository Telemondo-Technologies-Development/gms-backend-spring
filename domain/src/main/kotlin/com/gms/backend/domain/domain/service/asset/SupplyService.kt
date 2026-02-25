package com.gms.backend.domain.domain.service.asset

import com.gms.backend.domain.application.rest.asset.SupplyController
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface SupplyService {
    fun createSupply(body: SupplyController.SupplyPostDTO): SupplyController.SupplyTableDTO
    fun updateSupply(id: UUID, body: SupplyController.SupplyPutDTO): SupplyController.SupplyTableDTO
    fun getSupplies(pageable: Pageable): Page<SupplyController.SupplyTableDTO>
    fun getSupplyById(id: UUID): SupplyController.SupplyTableDTO
    fun deleteSupply(id: UUID)
    fun getSupplyLogs(supplyId: UUID, pageable: Pageable): Page<SuppliesLogController.SuppliesLogTableDTO>
}
