package com.gms.backend.domain.domain.repository.expense

import com.gms.backend.domain.domain.model.expense.UtilityType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UtilityTypeRepository : JpaRepository<UtilityType, UUID>
