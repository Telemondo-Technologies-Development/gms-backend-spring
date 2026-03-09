package com.gms.backend.domain.domain.service.member

import com.gms.backend.domain.application.rest.member.MemberController
import com.gms.backend.domain.domain.model.member.Member
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.Instant
import java.util.*

interface MemberService {
    fun createMember(body: MemberController.MemberPostDTO): MemberController.MemberTableDTO
    fun getMembers(
        pageable: Pageable,
        fullName: String?,
        status: Member.MemberStatus?,
        dateFrom: Instant?,
        dateTo: Instant?
    ): Page<MemberController.MemberTableDTO>
    fun getMemberById(
        id: UUID,
        fullName: String?,
        status: Member.MemberStatus?,
        dateFrom: Instant?,
        dateTo: Instant?
    ): MemberController.MemberTableDTO
    fun updateMember(id: UUID, body: MemberController.MemberPutDTO): MemberController.MemberTableDTO
    fun deleteMember(id: UUID)
}