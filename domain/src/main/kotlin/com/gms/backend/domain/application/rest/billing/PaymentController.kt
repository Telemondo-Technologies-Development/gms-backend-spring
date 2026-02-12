package com.gms.backend.domain.application.rest.billing

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.domain.model.billing.Payment
import com.gms.backend.domain.impl.domain.service.billing.PaymentServiceImpl
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.PositiveOrZero
import jakarta.validation.constraints.Size
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@RestController
@RequestMapping("/api/payment")
@Tag(name = "Payment")
class PaymentController(private val paymentService: PaymentServiceImpl) {

    @Schema(description = "Format for Payment read")
    data class PaymentTableDTO(
        val id: UUID,
        val invoiceId: UUID,
        val referenceNum: String?,
        val status: Payment.PaymentStatus,
        val paymentMethodId: UUID,
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
    fun getAllPayments(pageable: Pageable) = paymentService.getPayments(pageable).toPaginatedResponse()

    @GetMapping("/{id}")
    @Operation(summary = "Get a Payment by id")
    fun getPayment(@PathVariable id: UUID) = paymentService.getPaymentById(id).toOkResponse()

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
