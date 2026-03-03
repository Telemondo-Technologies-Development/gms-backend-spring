package com.gms.backend.domain.domain.repository.billing

import com.gms.backend.domain.application.rest.billing.InvoiceController
import com.gms.backend.domain.domain.model.billing.Invoice
import com.gms.backend.domain.domain.model.member.Member
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.Instant
import java.util.*

interface InvoiceRepository : JpaRepository<Invoice, UUID>{
    fun findAllProjectedBy(pageable: Pageable): Page<InvoiceController.InvoiceTableDTO>

    @Query(
        value = """
        SELECT i
        FROM Invoice i
        JOIN i.memberSubscription ms
        LEFT JOIN Member m on ms.actor = m.actor
        WHERE m.status = :memberStatus
        AND i.status = :invoiceStatus
        AND i.dueDate < :end
        ORDER BY ms.updatedAt DESC
        """,
        // AND i.dueDate BETWEEN :start AND :end
    )
    fun findAllByStatusAndDueDate(
        memberStatus: Member.MemberStatus,
        invoiceStatus: Invoice.InvoiceStatus,
//        start: Instant,
        end: Instant
    ): List<Invoice>
}
