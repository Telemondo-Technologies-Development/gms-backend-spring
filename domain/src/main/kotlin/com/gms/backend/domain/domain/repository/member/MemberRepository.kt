package com.gms.backend.domain.domain.repository.member

import com.gms.backend.domain.application.rest.member.MemberController
import com.gms.backend.domain.domain.model.member.Member
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface MemberRepository : JpaRepository<Member, UUID> {
    fun findAllByMemberObjectsId(id: UUID): List<Member>
    @Query(
        value = $$"""
        SELECT new com.gms.backend.domain.application.rest.member.MemberController$MemberTableDTO(
        m.id,
        m.actorId,
        m.surname,
        m.firstName,
        m.middleName,
        m.suffix,
        m.status,
        m.profilePictureId,
        m.createdById,
        ca.type as createdByType,
        cu.email as createdByEmail,
        ce.surname as createdBySurname,
        ce.firstName as createdByFirstName,
        m.updatedById,
        ua.type as updatedByType,
        uu.email as updatedByEmail,
        ue.surname as updatedBySurname,
        ue.firstName as updatedByFirstName
        )
        FROM Member m 
        JOIN m.createdBy ca
        LEFT JOIN User cu ON ca = cu.actor
        LEFT JOIN cu.userEmployees ce
        JOIN m.updatedBy ua
        LEFT JOIN User uu ON ua = uu.actor
        LEFT JOIN uu.userEmployees ue
        """,
        // WHERE m.id = :id
        countQuery = "SELECT COUNT(m) FROM Member m"
    )
    fun findAllProjectedBy(pageable: Pageable): Page<MemberController.MemberTableDTO>

    @Query(
        value = $$"""
        SELECT new com.gms.backend.domain.application.rest.member.MemberController$MemberTableDTO(
        m.id,
        m.actorId,
        m.surname,
        m.firstName,
        m.middleName,
        m.suffix,
        m.status,
        m.profilePictureId,
        m.createdById,
        ca.type as createdByType,
        cu.email as createdByEmail,
        ce.surname as createdBySurname,
        ce.firstName as createdByFirstName,
        m.updatedById,
        ua.type as updatedByType,
        uu.email as updatedByEmail,
        ue.surname as updatedBySurname,
        ue.firstName as updatedByFirstName
        )
        FROM Member m 
        JOIN m.createdBy ca
        LEFT JOIN User cu ON ca = cu.actor
        LEFT JOIN cu.userEmployees ce
        JOIN m.updatedBy ua
        LEFT JOIN User uu ON ua = uu.actor
        LEFT JOIN uu.userEmployees ue
        WHERE m.id = :id
        """
    )
    fun findProjectedBy(@Param("id") id: UUID): Optional<MemberController.MemberTableDTO>
}
