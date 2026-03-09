package com.gms.backend.domain.application.rest.billing

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.domain.model.billing.Payment
import com.gms.backend.domain.domain.service.billing.PaymentService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.PositiveOrZero
import jakarta.validation.constraints.Size
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@RestController
@RequestMapping("/api/payment")
@Tag(name = "Payment")
class PaymentController(private val paymentService: PaymentService) {

    @Schema(description = "Format for Payment read")
    data class PaymentTableDTO(
        val id: UUID,
        val invoiceId: UUID,
        val referenceNum: String?,
        val status: Payment.PaymentStatus,
        val paymentMethodId: UUID,
        val paymentMethodName: String,
        val amount: BigDecimal,
        val paidAt: Instant?,
        val failureReason: String?,
        val createdById: UUID,
        val updatedById: UUID?
    )

    @Schema(description = "Format for Payment create")
    data class PaymentPostDTO(
        val invoiceId: UUID,
        val status: Payment.PaymentStatus,
        val paymentMethodId: UUID,
        @field:PositiveOrZero(message = "Amount must not be negative")
        val amount: BigDecimal,
        @field:Size(min = 1, message = "Reference Number cannot be blank if provided")
        val referenceNum: String?,
        val paidAt: Instant?,
        @field:Size(min = 1, message = "Failure Reason cannot be blank if provided")
        val failureReason: String?,
        val createdById: UUID,
    )

    @Schema(description = "Format for Payment update")
    data class PaymentPutDTO(
        val status: Payment.PaymentStatus,
        val paymentMethodId: UUID,
        @field:PositiveOrZero(message = "Amount must not be negative")
        val amount: BigDecimal,
        val paidAt: Instant?,
        @field:Size(min = 1, message = "Failure Reason cannot be blank if provided")
        val failureReason: String?,
        val updatedById: UUID,
    )

    @GetMapping
    @Operation(summary = "Get all Payments")
    fun getAllPayments(
        pageable: Pageable,
        @RequestParam(required = false) status: Payment.PaymentStatus?,
        @RequestParam(required = false) paymentMethodName: String?,
        @RequestParam(required = false) dateFrom: Instant?,
        @RequestParam(required = false) dateTo: Instant?
    ) = paymentService.getPayments(pageable, status, paymentMethodName, dateFrom, dateTo).toPaginatedResponse()

    @GetMapping("/{id}")
    @Operation(summary = "Get a Payment by id")
    fun getPayment(
        @PathVariable id: UUID,
        @RequestParam(required = false) status: Payment.PaymentStatus?,
        @RequestParam(required = false) paymentMethodName: String?,
        @RequestParam(required = false) dateFrom: Instant?,
        @RequestParam(required = false) dateTo: Instant?
    ) = paymentService.getPaymentById(id, status, paymentMethodName, dateFrom, dateTo).toOkResponse()

    @PostMapping
    @Operation(summary = "Create a new Payment")
    fun createPayment(@Valid @RequestBody body: PaymentPostDTO) =
        paymentService.createPayment(body).toCreatedResponse("Payment Successfully Created!")

    @PutMapping("/{id}")
    @Operation(summary = "Update a Payment by id")
    fun updatePayment(@PathVariable id: UUID, @Valid @RequestBody body: PaymentPutDTO) =
        paymentService.updatePayment(id, body).toOkResponse("Payment updated")

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Payment by id")
    fun deletePayment(@PathVariable id: UUID) =
        paymentService.deletePayment(id).toOkResponse("Payment Deleted")
}
