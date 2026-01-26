package com.gms.backend.domain.domain.repository.billing

import com.gms.backend.domain.application.rest.billing.InvoiceController
import com.gms.backend.domain.domain.model.billing.Invoice
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface InvoiceRepository : JpaRepository<Invoice, UUID>{
    fun findAllProjectedBy(pageable: Pageable): Page<InvoiceController.InvoiceTableDTO>
}
