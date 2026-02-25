package com.gms.backend.domain.domain.service.billing

import com.gms.backend.domain.application.rest.billing.InvoiceController
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface InvoiceService {
    fun createInvoice(body: InvoiceController.InvoicePostDTO): InvoiceController.InvoiceTableDTO
    fun getInvoices(pageable: Pageable): Page<InvoiceController.InvoiceTableDTO>
    fun getInvoiceById(id: UUID): InvoiceController.InvoiceTableDTO
    fun updateInvoice(id: UUID, body: InvoiceController.InvoicePutDTO): InvoiceController.InvoiceTableDTO
    fun deleteInvoice(id: UUID)
}