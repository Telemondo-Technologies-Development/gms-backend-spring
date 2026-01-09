package com.gms.backend.domain.application.rest.member

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.domain.model.member.Member
import com.gms.backend.domain.impl.domain.service.member.MemberServiceImpl
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/member")
class MemberController(private val memberService: MemberServiceImpl) {

    data class MemberTableDTO(
        val id: UUID,
        val actorId: UUID,
        val surname: String,
        val firstName: String,
        val middleName: String?,
        val suffix: String?,
        val status: Member.MemberStatus,
        val createdById: UUID,
        val updatedById: UUID
    )

    @GetMapping
    fun getAllUsers() = memberService.getMembers().toOkResponse()

    @GetMapping("/{id}")
    fun getMember(@PathVariable id: UUID) = memberService.getMemberById(id).toOkResponse()

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
    fun createMember(@RequestBody body: MemberPostDTO) =
        memberService.createMember(body).toCreatedResponse("Member Successfully Created!")

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
    fun updateMember(@PathVariable id: UUID, @RequestBody body: MemberPutDTO) =
        memberService.updateMember(id, body).toOkResponse("Member updated")


    @DeleteMapping("/{id}")
    fun deleteMember(@PathVariable id: UUID) =
        memberService.deleteMember(id).toOkResponse("Member Deleted")
}