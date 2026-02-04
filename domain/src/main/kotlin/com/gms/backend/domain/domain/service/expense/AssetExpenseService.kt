package com.gms.backend.domain.domain.service.expense

import com.gms.backend.domain.application.rest.expense.AssetExpenseController
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface AssetExpenseService {
    fun createAssetExpense(body: AssetExpenseController.AssetExpensePostDTO): AssetExpenseController.AssetExpenseResponseDTO
    fun getAssetExpenses(pageable: Pageable): Page<AssetExpenseController.AssetExpenseResponseDTO>
    fun getAssetExpenseById(id: UUID): AssetExpenseController.AssetExpenseResponseDTO
    fun updateAssetExpense(id: UUID, body: AssetExpenseController.AssetExpensePutDTO): AssetExpenseController.AssetExpenseResponseDTO
    fun deleteAssetExpense(id: UUID)
}