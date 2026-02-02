package com.gms.backend.domain.domain.repository.branch

import com.gms.backend.domain.application.rest.branch.BranchController
import com.gms.backend.domain.domain.model.branch.Branch
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
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
}
