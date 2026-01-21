package com.gms.backend.domain.domain.repository.member

import com.gms.backend.domain.application.rest.member.MemberController
import com.gms.backend.domain.domain.model.member.Member
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MemberRepository : JpaRepository<Member, UUID> {
    fun findAllByMemberObjectsId(id: UUID): List<Member>
    fun findAllProjectedBy(pageable: Pageable): Page<MemberController.MemberTableDTO>
}
