package com.gms.backend.domain.domain.repository.branch

import com.gms.backend.domain.domain.model.branch.BranchPersonnel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface BranchPersonnelRepository : JpaRepository<BranchPersonnel, UUID> {
    fun findAllByBranchId(branchId: UUID): List<BranchPersonnel>
    fun findAllByBranchIdAndStatus(
        branchId: UUID,
        status: BranchPersonnel.BranchPersonnelStatus
    ): List<BranchPersonnel>
}