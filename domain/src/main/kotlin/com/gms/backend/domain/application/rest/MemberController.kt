package com.gms.backend.domain.application.rest

import com.gms.backend.domain.domain.model.member.Member
import com.gms.backend.domain.impl.domain.service.member.MemberServiceImpl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/member")
class MemberController(private val memberService: MemberServiceImpl) {

    data class MemberTableDTO(
        val id: UUID,
        val actorId: UUID,
        val surname: String,
        val firstName: String,
        val middleName: String?,
        val suffix: String?, // Might set to enum
        val status: Member.MemberStatus,
        val createdById: UUID,
        val updatedById: UUID
    )

    @GetMapping("")
    fun getAllUsers(): ResponseEntity<List<MemberTableDTO>> {
        val members = memberService.getMembers()
        return ResponseEntity.ok(members)
    }

    @GetMapping("/{id}")
    fun getMember(@PathVariable id: UUID): ResponseEntity<Optional<Member>> {
        val member = memberService.getMemberById(id)
        return ResponseEntity.ok(member)
    }

    data class MemberPostDTO(
        val surname: String,
        val firstName: String,
        val middleName: String?,
        val suffix: String?, // Might set to enum
        val status: Member.MemberStatus,
        val profilePictureId: UUID?,
        val createdById: UUID,
    )

    @PostMapping("")
    fun createMember(@RequestBody body: MemberPostDTO): ResponseEntity<String> {
        val something = memberService.createMember(body)
        return ResponseEntity.ok("Member Successfully Created!")
    }

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
    fun updateMember(@PathVariable id: UUID, @RequestBody body: MemberPutDTO): ResponseEntity<String> {
        return try {
            memberService.updateMember(id, body)
            ResponseEntity.ok("Member updated")
        } catch (e: Exception) {
            // Catch all other exceptions
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error updating member: ${e.message}")
        }
    }

    @DeleteMapping("/{id}")
    fun deleteMember(@PathVariable id: UUID): ResponseEntity<String> {
        return try {
            memberService.deleteMember(id)
            ResponseEntity.ok("Member Deleted")
        } catch (e: Exception) {
            // Catch all other exceptions
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error deleting member: ${e.message}")
        }
    }
}