package com.gms.backend.domain.application.rest.member

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.domain.model.member.MemberProgress
import com.gms.backend.domain.domain.service.member.MemberProgressService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Size
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.*

@RestController
@RequestMapping("/api/member/progress")
@Tag(name = "Member Progress")
class MemberProgressController(private val memberProgressService: MemberProgressService) {

    @Schema(description = "Format for Member Progress read")
    data class MemberProgressTableDTO(
        val id: UUID,
        val actorId: UUID?,
        val progressOptionId: UUID?,
        val progressId: UUID?,
        val branchId: UUID?,
        val remarks: String?,
        val status: MemberProgress.MemberProgressStatus,
        val createdById: UUID?,
        val updatedById: UUID?
    ) {
        var progressHistory: List<MemberProgressHistoryBrief> = emptyList()
    }

    data class MemberProgressHistory(
        val id: UUID,
        val memberProgressId: UUID,
        val progressId: UUID,
        val progressName: String,
        val changedAt: Instant
    )

    data class MemberProgressHistoryBrief(
        val id: UUID,
        val progressId: UUID,
        val progressName: String,
        val changedAt: Instant
    )


    @Schema(description = "Format for Member Progress create")
    data class MemberProgressPostDTO(
        val actorId: UUID,
        val progressOptionId: UUID,
        val progressId: UUID,
        val branchId: UUID,
        @field:Size(min = 1, message = "Remarks cannot be blank if provided")
        val remarks: String?,
        val status: MemberProgress.MemberProgressStatus,
        val createdById: UUID
    )

    @Schema(description = "Format for Member Progress update")
    data class MemberProgressPutDTO(
        val progressId: UUID,
        @field:Size(min = 1, message = "Remarks cannot be blank if provided")
        val remarks: String?,
        val status: MemberProgress.MemberProgressStatus,
        val updatedById: UUID
    )

    @GetMapping
    @Operation(summary = "Get all Member Progress")
    fun getAllMemberProgress(pageable: Pageable) =
        memberProgressService.getMemberProgress(pageable).toPaginatedResponse()

    @GetMapping("/{id}")
    @Operation(summary = "Get a Member Progress by id")
    fun getMemberProgress(@PathVariable id: UUID) =
        memberProgressService.getMemberProgressById(id).toOkResponse()

    @PostMapping
    @Operation(summary = "Create a new Member Progress")
    fun createMemberProgress(@Valid @RequestBody body: MemberProgressPostDTO) =
        memberProgressService.createMemberProgress(body).toCreatedResponse()

    @Operation(summary = "Update a Member Progress by id")
    @PutMapping("/{id}")
    fun updateMemberProgress(@PathVariable id: UUID, @Valid @RequestBody body: MemberProgressPutDTO) =
        memberProgressService.updateMemberProgress(id, body).toOkResponse()

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Member Progress by id")
    fun deleteMemberProgress(@PathVariable id: UUID) =
        memberProgressService.deleteMemberProgress(id).toOkResponse("Deleted Successfully")

}