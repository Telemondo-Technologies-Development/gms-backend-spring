package com.gms.backend.domain.domain.repository.expense

import com.gms.backend.domain.domain.model.expense.AssetMaintenanceExpense
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AssetMaintenanceExpenseRepository : JpaRepository<AssetMaintenanceExpense, UUID> {
    fun findAllByAssetMaintenanceExpensesObjectsId(
        id: UUID
    ): List<AssetMaintenanceExpense>
}
