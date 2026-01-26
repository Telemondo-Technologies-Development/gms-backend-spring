package com.gms.backend.domain.application.mapper.billing

import com.gms.backend.domain.application.rest.billing.InvoiceController
import com.gms.backend.domain.domain.model.billing.Invoice
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface InvoiceMapper {
    fun invoiceToInvoiceTableDTO(invoice: Invoice): InvoiceController.InvoiceTableDTO
    fun invoicePostDTOToInvoice(dto: InvoiceController.InvoicePostDTO): Invoice
    fun invoicePutDTOToInvoice(dto: InvoiceController.InvoicePutDTO, @MappingTarget invoice: Invoice): Invoice
}