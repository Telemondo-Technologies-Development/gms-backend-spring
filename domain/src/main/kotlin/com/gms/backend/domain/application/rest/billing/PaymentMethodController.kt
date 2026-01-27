package com.gms.backend.domain.application.rest.billing

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.impl.domain.service.billing.PaymentMethodServiceImpl
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/payment/method")
@Tag(name = "Payment")
class PaymentMethodController(private val paymentMethodService: PaymentMethodServiceImpl) {

    @Schema(description = "Format for Payment Method read")
    data class PaymentMethodTableDTO(
        val id: UUID,
        val name: String,
        val createdById: UUID?,
        val updatedById: UUID?
    )

    @Schema(description = "Format for Payment Method create")
    data class PaymentMethodPostDTO(
        @field:NotBlank
        val name: String,
        val createdById: UUID
    )

    @Schema(description = "Format for Payment Method update")
    data class PaymentMethodPutDTO(
        @field:NotBlank
        val name: String,
        val updatedById: UUID
    )

    @GetMapping
    @Operation(summary = "Get all Payment Methods")
    fun getAllPaymentMethods(pageable: Pageable) = paymentMethodService.getPaymentMethods(pageable).toPaginatedResponse()


    @GetMapping("/{id}")
    @Operation(summary = "Get a Payment Method with its Permissions by id")
    fun getPaymentMethod(@PathVariable id: UUID) =
        paymentMethodService.getPaymentMethodById(id).toOkResponse()

    @PostMapping
    @Operation(summary = "Create a new Payment Method")
    fun createPaymentMethod(@Valid @RequestBody body: PaymentMethodPostDTO) =
        paymentMethodService.createPaymentMethod(body).toCreatedResponse()

    @Operation(summary = "Update a Payment Method by id")
    @PutMapping("/{id}")
    fun updatePaymentMethod(@PathVariable id: UUID, @Valid @RequestBody body: PaymentMethodPutDTO) =
        paymentMethodService.updatePaymentMethod(id, body).toOkResponse()

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Payment Method by id")
    fun deletePaymentMethod(@PathVariable id: UUID) =
        paymentMethodService.deletePaymentMethod(id).toOkResponse("Deleted Successfully")
}