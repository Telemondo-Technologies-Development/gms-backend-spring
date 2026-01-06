package com.gms.backend.domain.domain.service.Member

import com.gms.backend.domain.application.rest.MemberController
import com.gms.backend.domain.domain.model.member.Member
import java.util.Optional
import java.util.UUID

interface MemberService {
    fun createMember(body: MemberController.MemberPostDTO): Member
    fun getMembers(): List<MemberController.MemberTableDTO>
    fun getMemberById(id: UUID): Optional<Member>
    fun updateMember(id: UUID, body: MemberController.MemberPutDTO)
    fun deleteMember(id: UUID)
}