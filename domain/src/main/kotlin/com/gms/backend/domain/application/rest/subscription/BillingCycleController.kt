package com.gms.backend.domain.application.rest.subscription

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.domain.model.subscription.BillingCycle
import com.gms.backend.domain.impl.domain.service.subscription.BillingCycleServiceImpl
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.*

@RestController
@RequestMapping("/api/billingCycle")
@Tag(name = "Billing Cycle")
class BillingCycleController(private val billingCycleService: BillingCycleServiceImpl) {

    @Schema(description = "Format for Billing Cycle read")
    data class BillingCycleTableDTO(
        val id: UUID,
        val name: String,
        val intervals: BillingCycle.Interval,
        val intervalCount: Int,
        val gracePeriodDays: Int,
        val createdAt: Instant,
        val updatedAt: Instant,
        val createdById: UUID?,
        val updatedById: UUID?
    )

    @Schema(description = "Format for Billing Cycle create")
    data class BillingCyclePostDTO(
        @field:NotBlank(message = "Name must not be empty")
        val name: String,
        val intervals: BillingCycle.Interval,
        @field:Positive
        val intervalCount: Int,
        @field:PositiveOrZero
        val gracePeriodDays: Int,
        val createdById: UUID,
    )

    @Schema(description = "Format for Billing Cycle update")
    data class BillingCyclePutDTO(
        @field:NotBlank(message = "Name must not be empty")
        val name: String,
        val intervals: BillingCycle.Interval,
        @field:Positive
        val intervalCount: Int,
        @field:PositiveOrZero
        val gracePeriodDays: Int,
        val updatedById: UUID
    )

    @GetMapping
    @Operation(summary = "Get all Billing Cycles")
    fun getAllBillingCycles(pageable: Pageable) = billingCycleService.getBillingCycles(pageable).toPaginatedResponse()

    @GetMapping("/{id}")
    @Operation(summary = "Get a Billing Cycle by id")
    fun getBillingCycle(@PathVariable id: UUID) = billingCycleService.getBillingCycleById(id).toOkResponse()

    @PostMapping
    @Operation(summary = "Create a new Billing Cycle")
    fun createBillingCycle(@Valid @RequestBody body: BillingCyclePostDTO) =
        billingCycleService.createBillingCycle(body).toCreatedResponse("Billing Cycle Successfully Created!")

    @PutMapping("/{id}")
    @Operation(summary = "Update a Billing Cycle by id")
    fun updateBillingCycle(@PathVariable id: UUID, @Valid @RequestBody body: BillingCyclePutDTO) =
        billingCycleService.updateBillingCycle(id, body).toOkResponse("Billing Cycle updated")

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Billing Cycle by id")
    fun deleteBillingCycle(@PathVariable id: UUID) =
        billingCycleService.deleteBillingCycle(id).toOkResponse("Billing Cycle Deleted")
}