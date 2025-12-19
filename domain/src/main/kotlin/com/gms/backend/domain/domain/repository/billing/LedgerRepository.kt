package com.gms.backend.domain.domain.repository.billing

import com.gms.backend.domain.domain.model.billing.Ledger
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface LedgerRepository : JpaRepository<Ledger, UUID>
