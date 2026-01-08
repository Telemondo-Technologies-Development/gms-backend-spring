package com.gms.backend.domain.domain.repository.member

import com.gms.backend.domain.application.rest.MemberController
import com.gms.backend.domain.domain.model.member.Member
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MemberRepository : JpaRepository<Member, UUID> {
    fun findAllByMemberObjectsId(id: UUID): List<Member>
    fun findAllProjectedBy(): List<MemberController.MemberTableDTO>
}
