package com.gms.backend.domain.domain.repository.member

import com.gms.backend.domain.application.rest.member.MemberController
import com.gms.backend.domain.domain.model.member.Member
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.Instant
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
        WHERE (:fullName IS NULL OR LOWER(
            CONCAT(m.firstName, COALESCE(m.middleName, ''), m.surname, COALESCE(m.suffix, ''))
        ) LIKE LOWER(CONCAT('%', REPLACE(:fullName, ' ', ''), '%')))
        AND (:status IS NULL OR m.status = :status)
        AND (:dateFrom IS NULL OR m.createdAt >= :dateFrom)
        AND (:dateTo IS NULL OR m.createdAt <= :dateTo)
        """,
        // WHERE m.id = :id
        countQuery = """SELECT COUNT(m) FROM Member m
        WHERE (:fullName IS NULL OR LOWER(
            CONCAT(m.firstName, COALESCE(m.middleName, ''), m.surname, COALESCE(m.suffix, ''))
        ) LIKE LOWER(CONCAT('%', REPLACE(:fullName, ' ', ''), '%')))
        AND (:status IS NULL OR m.status = :status)
        AND (:dateFrom IS NULL OR m.createdAt >= :dateFrom)
        AND (:dateTo IS NULL OR m.createdAt <= :dateTo)
            """
    )
    fun findAllProjectedBy(
        pageable: Pageable,
        @Param("fullName") fullName: String?,
        @Param("status") status: Member.MemberStatus?,
        @Param("dateFrom") dateFrom: Instant?,
        @Param("dateTo") dateTo: Instant?
    ): Page<MemberController.MemberTableDTO>

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
        AND (:fullName IS NULL OR LOWER(
            CONCAT(m.firstName, COALESCE(m.middleName, ''), m.surname, COALESCE(m.suffix, ''))
        ) LIKE LOWER(CONCAT('%', REPLACE(:fullName, ' ', ''), '%')))
        AND (:status IS NULL OR m.status = :status)
        AND (:dateFrom IS NULL OR m.createdAt >= :dateFrom)
        AND (:dateTo IS NULL OR m.createdAt <= :dateTo)
        """
    )
    fun findProjectedBy(
        @Param("id") id: UUID,
        @Param("fullName") fullName: String?,
        @Param("status") status: Member.MemberStatus?,
        @Param("dateFrom") dateFrom: Instant?,
        @Param("dateTo") dateTo: Instant?
    ): Optional<MemberController.MemberTableDTO>
}
