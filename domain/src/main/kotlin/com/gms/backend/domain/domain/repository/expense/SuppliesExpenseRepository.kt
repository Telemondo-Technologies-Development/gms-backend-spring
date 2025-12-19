package com.gms.backend.domain.domain.repository.expense

import com.gms.backend.domain.domain.model.expense.SuppliesExpense
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SuppliesExpenseRepository : JpaRepository<SuppliesExpense, UUID> {
    fun findAllBySuppliesExpensesObjectsId(id: UUID): List<SuppliesExpense>
}
