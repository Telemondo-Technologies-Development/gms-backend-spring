package com.gms.backend.domain.domain.service.expense

import com.gms.backend.domain.application.rest.expense.UtilityExpenseController
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface UtilityExpenseService {
    fun createUtilityExpense(body: UtilityExpenseController.UtilityExpenseCreateDTO): UtilityExpenseController.UtilityExpenseReadDTO
    fun getUtilityExpense(pageable: Pageable): Page<UtilityExpenseController.UtilityExpenseReadDTO>
    fun getUtilityExpenseById(id: UUID): UtilityExpenseController.UtilityExpenseReadDTO
    fun updateUtilityExpense(id: UUID, body: UtilityExpenseController.UtilityExpenseUpdateDTO): UtilityExpenseController.UtilityExpenseReadDTO
    fun deleteUtilityExpense(id: UUID)
}