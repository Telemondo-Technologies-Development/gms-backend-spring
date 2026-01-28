package com.gms.backend.domain.application.rest.member

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.domain.model.member.MemberSubscription
import com.gms.backend.domain.domain.model.subscription.SubscriptionAvailed
import com.gms.backend.domain.impl.domain.service.member.MemberSubscriptionServiceImpl
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.*

@RestController
@RequestMapping("/api/member/subscription")
@Tag(name = "Member Subscription")
class MemberSubscriptionController(private val memberService: MemberSubscriptionServiceImpl) {

    @Schema(description = "Format for MemberSubscription read")
    data class MemberSubscriptionTableDTO(
        val id: UUID,
        val actorId: UUID,
//         val subscriptionAvailed: SubscriptionAvailed?,
        val subscriptionAvailedId: UUID?,
        val branchId: UUID,
        val startDate: Instant,
        val endDate: Instant?,
        val status: MemberSubscription.MemberSubscriptionStatus,
        val createdById: UUID?,
        val updatedById: UUID?
    )

    data class MemberSubscriptionIdsDTO(
        val id: UUID,
        val subscriptionId: UUID
    )

    @Schema(description = "Format for MemberSubscription create")
    data class MemberSubscriptionPostDTO(
        val actorId: UUID,
        val subscriptionId: UUID,
        val branchId: UUID,
        val startDate: Instant,
        val endDate: Instant?,
        val status: MemberSubscription.MemberSubscriptionStatus,
        val createdById: UUID
    )

    @Schema(description = "Format for MemberSubscription update")
    data class MemberSubscriptionPutDTO(
        val actorId: UUID,
        val updateCurrentSubscription: Boolean = false,
        val subscriptionId: UUID,
        val branchId: UUID,
        // Null = don't change
        val startDate: Instant?,
        val endDate: Instant?,
        val status: MemberSubscription.MemberSubscriptionStatus,
        val updatedById: UUID
    )

    @GetMapping
    @Operation(summary = "Get all MemberSubscriptions")
    fun getAllMemberSubscriptions(pageable: Pageable) = memberService.getMemberSubscriptions(pageable).toPaginatedResponse()

    @GetMapping("/{id}")
    @Operation(summary = "Get a MemberSubscription by id")
    fun getMemberSubscription(@PathVariable id: UUID) = memberService.getMemberSubscriptionById(id).toOkResponse()

    @PostMapping
    @Operation(summary = "Create a new MemberSubscription")
    fun createMemberSubscription(@RequestBody body: MemberSubscriptionPostDTO) =
        memberService.createMemberSubscription(body).toCreatedResponse("MemberSubscription Successfully Created!")

    @PutMapping("/{id}")
    @Operation(summary = "Update a MemberSubscription by id")
    fun updateMemberSubscription(@PathVariable id: UUID, @RequestBody body: MemberSubscriptionPutDTO) =
        memberService.updateMemberSubscription(id, body).toOkResponse("MemberSubscription updated")

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a MemberSubscription by id")
    fun deleteMemberSubscription(@PathVariable id: UUID) =
        memberService.deleteMemberSubscription(id).toOkResponse("MemberSubscription Deleted")
}