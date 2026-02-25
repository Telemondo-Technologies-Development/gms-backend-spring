package com.gms.backend.domain.application.rest.subscription

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.domain.model.subscription.BillingCycle
import com.gms.backend.domain.domain.service.subscription.SubscriptionAvailedService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.util.*

@RestController
@RequestMapping("/api/subscription-availed")
@Tag(name = "Subscription Availed")
class SubscriptionAvailedController(private val subscriptionAvailedService: SubscriptionAvailedService) {

    @Schema(description = "Format for Subscription Availed read")
    data class SubscriptionAvailedTableDTO(
        val id: UUID,
        val name: String,
        val amount: BigDecimal,
        val intervals: BillingCycle.Interval,
        val intervalCount: Int,
        val gracePeriodDays: Int
    )

    @Schema(description = "Format for Subscription Availed create")
    data class SubscriptionAvailedPostDTO(
        val subscriptionId: UUID,
    )

    @GetMapping
    @Operation(summary = "Get all Subscription Availed")
    fun getAllSubscriptionAvailed(pageable: Pageable) =
        subscriptionAvailedService.getSubscriptionAvailed(pageable).toPaginatedResponse()

    @GetMapping("/{id}")
    @Operation(summary = "Get a Subscription Availed by id")
    fun getSubscriptionAvailed(@PathVariable id: UUID) =
        subscriptionAvailedService.getSubscriptionAvailedById(id).toOkResponse()

    @PostMapping
    @Operation(summary = "Create a new Subscription Availed")
    fun createSubscriptionAvailed(@RequestBody body: SubscriptionAvailedPostDTO) =
        subscriptionAvailedService.createSubscriptionAvailed(body)
            .toCreatedResponse("Subscription Availed Successfully Created!")

//    @Schema(description = "Format for Subscription Availed update")
//    data class SubscriptionAvailedPutDTO(
//        val subscriptionId: UUID,
//        val billingCycleId: UUID
//    )
//
//    @PutMapping("/{id}")
//    @Operation(summary = "Update a Subscription Availed by id")
//    fun updateSubscriptionAvailed(@PathVariable id: UUID, @RequestBody body: SubscriptionAvailedPutDTO) =
//        subscriptionAvailedService.updateSubscriptionAvailed(id, body).toOkResponse("Subscription Availed updated")


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Subscription Availed by id")
    fun deleteSubscriptionAvailed(@PathVariable id: UUID) =
        subscriptionAvailedService.deleteSubscriptionAvailed(id).toOkResponse("Subscription Availed Deleted")
}