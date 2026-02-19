package com.gms.backend.domain.domain.repository.branch

import com.gms.backend.domain.domain.model.branch.BranchPersonnel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface BranchPersonnelRepository : JpaRepository<BranchPersonnel, UUID> {
    fun findAllByBranchId(branchId: UUID, pageable: Pageable): Page<BranchPersonnel>
    fun findAllByBranchIdAndStatus(
        branchId: UUID,
        status: BranchPersonnel.BranchPersonnelStatus,
        pageable: Pageable
    ): Page<BranchPersonnel>
}