package com.gms.backend.domain.domain.service.expense

import com.gms.backend.domain.application.rest.expense.SuppliesExpenseController
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface SuppliesExpenseService {
    fun createSuppliesExpense(body: SuppliesExpenseController.SuppliesExpenseCreateDTO): SuppliesExpenseController.SuppliesExpenseReadDTO
    fun getSuppliesExpense(pageable: Pageable): Page<SuppliesExpenseController.SuppliesExpenseReadDTO>
    fun getSuppliesExpenseById(id: UUID): SuppliesExpenseController.SuppliesExpenseReadDTO
    fun updateSuppliesExpense(id: UUID, body: SuppliesExpenseController.SuppliesExpenseUpdateDTO): SuppliesExpenseController.SuppliesExpenseReadDTO
    fun deleteSuppliesExpense(id: UUID)
}