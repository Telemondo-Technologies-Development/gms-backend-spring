package com.gms.backend.domain.domain.repository.expense

import com.gms.backend.domain.domain.model.expense.OtherExpenseType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface OtherExpenseTypeRepository : JpaRepository<OtherExpenseType, UUID>
