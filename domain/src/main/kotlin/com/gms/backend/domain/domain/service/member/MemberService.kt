package com.gms.backend.domain.domain.service.Member

import com.gms.backend.domain.application.rest.member.MemberController
import java.util.*

interface MemberService {
    fun createMember(body: MemberController.MemberPostDTO)
    fun getMembers(): List<MemberController.MemberTableDTO>
    fun getMemberById(id: UUID): MemberController.MemberTableDTO
    fun updateMember(id: UUID, body: MemberController.MemberPutDTO)
    fun deleteMember(id: UUID)
}