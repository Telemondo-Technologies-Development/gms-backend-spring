package com.gms.backend.domain.application.rest.subscription

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.domain.service.subscription.SubscriptionService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@RestController
@RequestMapping("/api/subscription")
@Tag(name = "Subscription")
class SubscriptionController(private val subscriptionService: SubscriptionService) {

    @Schema(description = "Format for Subscription read")
    data class SubscriptionTableDTO(
        val id: UUID,
        val billingCycleId: UUID,
        val name: String,
        val description: String,
        val amount: BigDecimal,
        val createdAt: Instant,
        val updatedAt: Instant,
        val createdById: UUID?,
        val updatedById: UUID?
    )

    @Schema(description = "Format for Subscription create")
    data class SubscriptionPostDTO(
        val billingCycleId: UUID,
        @field:NotBlank(message = "Name must not be empty")
        val name: String,
        @field:NotBlank(message = "Description must not be empty")
        val description: String,
        @field:Positive(message = "Amount must be positive")
        val amount: BigDecimal,
        val createdById: UUID
    )

    @Schema(description = "Format for Subscription update")
    data class SubscriptionPutDTO(
        val billingCycleId: UUID,
        @field:NotBlank("Name must not be empty")
        val name: String,
        @field:NotBlank("Description must not be empty")
        val description: String,
        @field:Positive("Amount must be positive")
        val amount: BigDecimal,
        val updatedById: UUID
    )

    @GetMapping
    @Operation(summary = "Get all Subscriptions")
    fun getAllSubscriptions(pageable: Pageable) = subscriptionService.getSubscriptions(pageable).toPaginatedResponse()

    @GetMapping("/{id}")
    @Operation(summary = "Get a Subscription by id")
    fun getSubscription(@PathVariable id: UUID) = subscriptionService.getSubscriptionById(id).toOkResponse()

    @PostMapping
    @Operation(summary = "Create a new Subscription")
    fun createSubscription(@Valid @RequestBody body: SubscriptionPostDTO) =
        subscriptionService.createSubscription(body).toCreatedResponse("Subscription Successfully Created!")

    @PutMapping("/{id}")
    @Operation(summary = "Update a Subscription by id")
    fun updateSubscription(@PathVariable id: UUID, @Valid @RequestBody body: SubscriptionPutDTO) =
        subscriptionService.updateSubscription(id, body).toOkResponse("Subscription updated")


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Subscription by id")
    fun deleteSubscription(@PathVariable id: UUID) =
        subscriptionService.deleteSubscription(id).toOkResponse("Subscription Deleted")
}