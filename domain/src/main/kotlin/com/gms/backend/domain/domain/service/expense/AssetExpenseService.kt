package com.gms.backend.domain.domain.service.expense

import com.gms.backend.domain.application.rest.expense.AssetExpenseController
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface AssetExpenseService {
    fun createAssetExpense(body: AssetExpenseController.AssetExpenseCreateDTO): AssetExpenseController.AssetExpenseReadDTO
    fun getAssetExpense(pageable: Pageable): Page<AssetExpenseController.AssetExpenseReadDTO>
    fun getAssetExpenseById(id: UUID): AssetExpenseController.AssetExpenseReadDTO
    fun updateAssetExpense(id: UUID, body: AssetExpenseController.AssetExpenseUpdateDTO): AssetExpenseController.AssetExpenseReadDTO
    fun deleteAssetExpense(id: UUID)
}