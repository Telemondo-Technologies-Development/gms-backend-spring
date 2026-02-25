package com.gms.backend.domain.domain.service.expense

import com.gms.backend.domain.application.rest.expense.OtherExpenseController
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface OtherExpenseService {
    fun createOtherExpense(body: OtherExpenseController.OtherExpenseCreateDTO): OtherExpenseController.OtherExpenseReadDTO
    fun getOtherExpense(pageable: Pageable): Page<OtherExpenseController.OtherExpenseReadDTO>
    fun getOtherExpenseById(id: UUID): OtherExpenseController.OtherExpenseReadDTO
    fun updateOtherExpense(id: UUID, body: OtherExpenseController.OtherExpenseUpdateDTO): OtherExpenseController.OtherExpenseReadDTO
    fun deleteOtherExpense(id: UUID)
}