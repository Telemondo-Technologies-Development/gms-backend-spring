package com.gms.backend.domain.application.rest.billing

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.domain.model.billing.Invoice
import com.gms.backend.domain.domain.service.billing.InvoiceService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.PositiveOrZero
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@RestController
@RequestMapping("/api/invoice")
@Tag(name = "Invoice")
class InvoiceController(private val invoiceService: InvoiceService) {

    @Schema(description = "Format for Invoice read")
    data class InvoiceTableDTO(
        val id: UUID,
        val actorId: UUID?,
        val subscriptionAvailedId: UUID?,
        val branchId: UUID?,
        val memberSubscriptionId: UUID?,
        val subtotal: BigDecimal,
        val total: BigDecimal,
        val issuedAt: Instant,
        val dueDate: Instant,
        val gracePeriodDate: Instant,
        val status: Invoice.InvoiceStatus,
        val systemGenerated: Boolean,
        val createdById: UUID,
        val updatedById: UUID?
    )

    @Schema(description = "Format for Invoice create")
    data class InvoicePostDTO(
        val actorId: UUID,
        val memberSubscriptionId: UUID,
        // TODO: Check whether subtotal will be based subscription amount
        @field:PositiveOrZero(message = "Subtotal must not be negative")
        val subtotal: BigDecimal,
        val dueDate: Instant,
        val gracePeriodDate: Instant,
        val status: Invoice.InvoiceStatus,
        val systemGenerated: Boolean? = false,
        val createdById: UUID
    )

    @Schema(description = "Format for Invoice update")
    data class InvoicePutDTO(
        @field:PositiveOrZero(message = "Amount must not be negative")
        val subtotal: BigDecimal,
        val dueDate: Instant,
        val gracePeriodDate: Instant,
        val status: Invoice.InvoiceStatus,
        val updatedById: UUID
    )

    @GetMapping
    @Operation(summary = "Get all Invoices")
    fun getAllInvoices(pageable: Pageable) = invoiceService.getInvoices(pageable).toPaginatedResponse()

    @GetMapping("/{id}")
    @Operation(summary = "Get an Invoice by id")
    fun getInvoice(@PathVariable id: UUID) = invoiceService.getInvoiceById(id).toOkResponse()

    @PostMapping
    @Operation(summary = "Create a new Invoice")
    fun createInvoice(@Valid @RequestBody body: InvoicePostDTO) =
        invoiceService.createInvoice(body).toCreatedResponse("Invoice Successfully Created!")

    @PutMapping("/{id}")
    @Operation(summary = "Update an Invoice by id")
    fun updateInvoice(@PathVariable id: UUID, @Valid @RequestBody body: InvoicePutDTO) =
        invoiceService.updateInvoice(id, body).toOkResponse("Invoice updated")

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an Invoice by id")
    fun deleteInvoice(@PathVariable id: UUID) =
        invoiceService.deleteInvoice(id).toOkResponse("Invoice Deleted")
}
