package com.gms.backend.domain.domain.repository.expense

import com.gms.backend.domain.domain.model.expense.SalaryExpense
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SalaryExpenseRepository : JpaRepository<SalaryExpense, UUID> {
    fun findAllBySalaryExpensesObjectsId(id: UUID): List<SalaryExpense>
}
