package com.gms.backend.domain.application.rest.member

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.domain.model.member.Member
import com.gms.backend.domain.impl.domain.service.member.MemberServiceImpl
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/member")
@Tag(name = "Member")
class MemberController(private val memberService: MemberServiceImpl) {

    @Schema(description = "Format for Member read")
    data class MemberTableDTO(
        val id: UUID,
        val actorId: UUID?,
        val surname: String,
        val firstName: String,
        val middleName: String?,
        val suffix: String?,
        val status: Member.MemberStatus,
        val createdById: UUID?,
        val updatedById: UUID?
    )

    @GetMapping
    @Operation(summary = "Get all Members")
    fun getAllUsers() = memberService.getMembers().toOkResponse()

    @GetMapping("/{id}")
    @Operation(summary = "Get a Member by id")
    fun getMember(@PathVariable id: UUID) = memberService.getMemberById(id).toOkResponse()

    @Schema(description = "Format for Member create")
    data class MemberPostDTO(
        val surname: String,
        val firstName: String,
        val middleName: String?,
        val suffix: String?, // Might set to enum
        val status: Member.MemberStatus,
        val profilePictureId: UUID?,
        val createdById: UUID,
    )

    @PostMapping
    @Operation(summary = "Create a new Member")
    fun createMember(@RequestBody body: MemberPostDTO) =
        memberService.createMember(body).toCreatedResponse("Member Successfully Created!")

    @Schema(description = "Format for Member update")
    data class MemberPutDTO(
        val surname: String,
        val firstName: String,
        val middleName: String?,
        val suffix: String?, // Might set to enum
        val status: Member.MemberStatus,
        val profilePictureId: UUID?,
        val updatedById: UUID,
    )

    @PutMapping("/{id}")
    @Operation(summary = "Update a Member by id")
    fun updateMember(@PathVariable id: UUID, @RequestBody body: MemberPutDTO) =
        memberService.updateMember(id, body).toOkResponse("Member updated")


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Member by id")
    fun deleteMember(@PathVariable id: UUID) = memberService.deleteMember(id).toOkResponse("Member Deleted")
}