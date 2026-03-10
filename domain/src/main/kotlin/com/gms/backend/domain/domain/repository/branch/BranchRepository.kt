package com.gms.backend.domain.domain.repository.branch

import com.gms.backend.domain.application.rest.asset.BrandController
import com.gms.backend.domain.application.rest.branch.BranchController
import com.gms.backend.domain.application.rest.member.MemberController
import com.gms.backend.domain.domain.model.branch.Branch
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.Instant
import java.util.*

interface BranchRepository : JpaRepository<Branch, UUID> {
    @Query(
        $$"""
        SELECT new com.gms.backend.domain.application.rest.branch.BranchController$BranchListDTO(
        b.id,
        b.name
        )FROM User u
        LEFT JOIN u.userEmployees e
        LEFT JOIN BranchPersonnel bp ON e.actor = bp.actor
        LEFT JOIN bp.branch b
        WHERE u.id = :userId
        AND bp.status = com.gms.backend.domain.domain.model.branch.BranchPersonnel$BranchPersonnelStatus.ACTIVE
        AND b.status = com.gms.backend.domain.domain.model.branch.Branch$BranchStatus.ACTIVE
    """
    )
    fun findBranchByUserId(@Param("userId") userId: UUID): List<BranchController.BranchListDTO>

    @Query(
        value = $$"""
        SELECT new com.gms.backend.domain.application.rest.branch.BranchController$BranchTableDTO(
            b.id,
            b.name,
            b.address,
            b.longitude,
            b.latitude,
            b.status,
            b.createdById,
            b.updatedById,
            b.createdAt,
            b.updatedAt,
            b.profilePictureId
        )
        FROM Branch b 
        """,
        countQuery = "SELECT COUNT(b) FROM Branch b"
    )
    fun findAllProjectedBy(pageable: Pageable): Page<BranchController.BranchTableDTO>

    @Query(
        value = $$"""
        SELECT new com.gms.backend.domain.application.rest.branch.BranchController$BranchTableDTO(
            b.id,
            b.name,
            b.address,
            b.longitude,
            b.latitude,
            b.status,
            b.createdById,
            b.updatedById,
            b.createdAt,
            b.updatedAt,
            b.profilePictureId
        )
        FROM Branch b 
        WHERE b.id = :branchId 
        """
    )
    fun findProjectedBy(@Param("branchId") id: UUID): Optional<BranchController.BranchTableDTO>
}
