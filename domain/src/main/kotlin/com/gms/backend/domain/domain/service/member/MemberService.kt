package com.gms.backend.domain.domain.service.member

import com.gms.backend.domain.application.rest.member.MemberController
import java.util.*

interface MemberService {
    fun createMember(body: MemberController.MemberPostDTO): MemberController.MemberTableDTO
    fun getMembers(): List<MemberController.MemberTableDTO>
    fun getMemberById(id: UUID): MemberController.MemberTableDTO
    fun updateMember(id: UUID, body: MemberController.MemberPutDTO): MemberController.MemberTableDTO
    fun deleteMember(id: UUID)
}