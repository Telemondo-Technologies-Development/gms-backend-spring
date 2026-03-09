package com.gms.backend.domain.domain.repository.billing

import com.gms.backend.domain.application.rest.billing.PaymentController
import com.gms.backend.domain.domain.model.billing.Payment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.math.BigDecimal
import java.time.Instant
import java.util.*

interface PaymentRepository : JpaRepository<Payment, UUID>{
    @Query(
        value = $$"""
    SELECT new com.gms.backend.domain.application.rest.billing.PaymentController$PaymentTableDTO(
        p.id,
        p.invoice.id,
        p.referenceNum,
        p.status,
        pm.id,
        pm.name, 
        p.amount,
        p.paidAt,
        p.failureReason,
        p.createdById,
        p.updatedById
    )
    FROM Payment p
    JOIN p.paymentMethod pm
    WHERE (:status IS NULL OR p.status = :status)
      AND (:paymentMethodName IS NULL OR pm.name = :paymentMethodName)
      AND (:dateFrom IS NULL OR p.paidAt >= :dateFrom)
      AND (:dateTo IS NULL OR p.paidAt <= :dateTo)
    """,
        countQuery = """
    SELECT COUNT(p) FROM Payment p
    JOIN p.paymentMethod pm
    WHERE (:status IS NULL OR p.status = :status)
      AND (:paymentMethodName IS NULL OR pm.name = :paymentMethodName)
      AND (:dateFrom IS NULL OR p.paidAt >= :dateFrom)
      AND (:dateTo IS NULL OR p.paidAt <= :dateTo)
    """
    )
    fun findAllProjectedBy(
        pageable: Pageable,
        @Param("status") status: Payment.PaymentStatus?,
        @Param("paymentMethodName") paymentMethodName: String?,
        @Param("dateFrom") dateFrom: Instant?,
        @Param("dateTo") dateTo: Instant?
    ): Page<PaymentController.PaymentTableDTO>

    @Query(
        value = $$"""
    SELECT new com.gms.backend.domain.application.rest.billing.PaymentController$PaymentTableDTO(
        p.id,
        p.invoice.id,
        p.referenceNum,
        p.status,
        pm.id,
        pm.name, 
        p.amount,
        p.paidAt,
        p.failureReason,
        p.createdById,
        p.updatedById
    )
    FROM Payment p
    JOIN p.paymentMethod pm
    WHERE p.id = :id
      AND (:status IS NULL OR p.status = :status)
      AND (:paymentMethodName IS NULL OR pm.name = :paymentMethodName)
      AND (:dateFrom IS NULL OR p.paidAt >= :dateFrom)
      AND (:dateTo IS NULL OR p.paidAt <= :dateTo)
    """
    )
    fun findByPaymentId(
        @Param("id") id: UUID,
        @Param("status") status: Payment.PaymentStatus?,
        @Param("paymentMethodName") paymentMethodName: String?,
        @Param("dateFrom") dateFrom: Instant?,
        @Param("dateTo") dateTo: Instant?
    ): Optional<PaymentController.PaymentTableDTO>

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
