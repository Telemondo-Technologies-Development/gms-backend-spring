package com.gms.backend.domain.domain.repository.expense

import com.gms.backend.domain.domain.model.expense.UtilityExpense
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UtilityExpenseRepository : JpaRepository<UtilityExpense, UUID> {
    fun findAllByUtilityExpensesObjectsId(id: UUID): List<UtilityExpense>
}
