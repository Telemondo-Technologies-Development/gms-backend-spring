package com.gms.backend.domain.domain.service.member

import com.gms.backend.domain.application.rest.member.MemberProgressController
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface MemberProgressService {
    fun createMemberProgress(body: MemberProgressController.MemberProgressPostDTO): MemberProgressController.MemberProgressTableDTO
    fun getMemberProgress(pageable: Pageable): Page<MemberProgressController.MemberProgressTableDTO>
    fun getMemberProgressById(id: UUID): MemberProgressController.MemberProgressTableDTO
    fun updateMemberProgress(id: UUID, body: MemberProgressController.MemberProgressPutDTO): MemberProgressController.MemberProgressTableDTO
    fun deleteMemberProgress(id: UUID)
}