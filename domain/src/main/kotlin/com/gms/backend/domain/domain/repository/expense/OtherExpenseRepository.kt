package com.gms.backend.domain.domain.repository.expense

import com.gms.backend.domain.domain.model.expense.OtherExpense
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface OtherExpenseRepository : JpaRepository<OtherExpense, UUID> {
    fun findAllByOtherExpensesObjectsId(id: UUID): List<OtherExpense>
}
