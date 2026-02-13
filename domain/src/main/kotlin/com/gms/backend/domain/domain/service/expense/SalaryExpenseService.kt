package com.gms.backend.domain.domain.service.expense

import com.gms.backend.domain.application.rest.expense.SalaryExpenseController
import org.springframework.data.domain.Page
import java.util.UUID

interface SalaryExpenseService {
    fun createSalaryExpense(body: SalaryExpenseController.SalaryExpenseCreateDTO): SalaryExpenseController.SalaryExpenseReadDTO
    fun getSalaryExpense(pageable: org.springframework.data.domain.Pageable): Page<SalaryExpenseController.SalaryExpenseReadDTO>
    fun getSalaryExpenseById(id: UUID): SalaryExpenseController.SalaryExpenseReadDTO
    fun updateSalaryExpense(id: UUID, body: SalaryExpenseController.SalaryExpenseUpdateDTO): SalaryExpenseController.SalaryExpenseReadDTO
    fun deleteSalaryExpense(id: UUID)
}