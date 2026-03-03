package com.gms.backend.domain.domain.repository.billing

import com.gms.backend.domain.application.rest.billing.PaymentController
import com.gms.backend.domain.domain.model.billing.Payment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.math.BigDecimal
import java.time.Instant
import java.util.*

interface PaymentRepository : JpaRepository<Payment, UUID>{
    fun findAllProjectedBy(pageable: Pageable): Page<PaymentController.PaymentTableDTO>

    // Sample AVG query
    @Query("""
        SELECT AVG(p.amount) 
        FROM Payment p
        JOIN p.invoice i
        WHERE i.branchId = :branchId 
        AND i.status != 'FAILED'
        AND i.createdAt BETWEEN :start AND :end
    """)
    fun findAverageTotalByBranchAndDateRange(
        branchId: UUID,
        start: Instant,
        end: Instant
    ): BigDecimal?
}
