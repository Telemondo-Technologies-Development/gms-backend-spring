package com.gms.backend.domain.domain.repository.member

import com.gms.backend.domain.application.rest.member.MemberProgressController
import com.gms.backend.domain.domain.model.member.MemberProgress
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface MemberProgressRepository : JpaRepository<MemberProgress, UUID> {
    fun findAllProjectedBy(pageable: Pageable): Page<MemberProgressController.MemberProgressTableDTO>

    @Query(
        value = $$"""
        SELECT new com.gms.backend.domain.application.rest.member.MemberProgressController$MemberProgressHistory(
        h.id,
        h.memberProgressId,
        h.progressId,
        p.name as progressName,
        h.changedAt
        )
        FROM MemberProgressHistory h
        JOIN h.progress p
        WHERE h.memberProgressId IN :memberProgressIds
        ORDER BY h.memberProgressId DESC, h.changedAt DESC
        """,
        countQuery = "SELECT COUNT(m) FROM MemberProgressHistory m"
    )
    fun findAllHistory(@Param("memberProgressIds") memberProgressIds: List<UUID>): List<MemberProgressController.MemberProgressHistory>
}
