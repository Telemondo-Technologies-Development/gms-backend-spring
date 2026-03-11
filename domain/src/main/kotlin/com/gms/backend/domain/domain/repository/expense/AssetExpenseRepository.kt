package com.gms.backend.domain.domain.repository.expense

import com.gms.backend.domain.domain.model.expense.AssetExpense
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AssetExpenseRepository : JpaRepository<AssetExpense, UUID> {
    fun findAllByAssetExpensesObjectsId(id: UUID): List<AssetExpense>
    fun findAllByAssetIdIn(assetIds: List<UUID>): List<AssetExpense>
}
