package com.gms.backend.domain.domain.repository.billing

import com.gms.backend.domain.domain.model.billing.Invoice
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface InvoiceRepository : JpaRepository<Invoice, UUID>
