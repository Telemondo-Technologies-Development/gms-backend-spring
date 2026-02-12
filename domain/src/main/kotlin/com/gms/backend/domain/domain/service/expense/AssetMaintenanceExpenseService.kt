package com.gms.backend.domain.domain.service.expense

import com.gms.backend.domain.application.rest.expense.AssetMaintenanceExpenseController
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface AssetMaintenanceExpenseService {
    fun createAssetMaintenanceExpense(body: AssetMaintenanceExpenseController.AssetMaintenanceExpenseCreateDTO): AssetMaintenanceExpenseController.AssetMaintenanceExpenseReadDTO
    fun getAssetMaintenanceExpense(pageable: Pageable): Page<AssetMaintenanceExpenseController.AssetMaintenanceExpenseReadDTO>
    fun getAssetMaintenanceExpenseById(id: UUID): AssetMaintenanceExpenseController.AssetMaintenanceExpenseReadDTO
    fun updateAssetMaintenanceExpense(id: UUID, body: AssetMaintenanceExpenseController.AssetMaintenanceExpenseUpdateDTO): AssetMaintenanceExpenseController.AssetMaintenanceExpenseReadDTO
    fun deleteAssetMaintenanceExpense(id: UUID)
}