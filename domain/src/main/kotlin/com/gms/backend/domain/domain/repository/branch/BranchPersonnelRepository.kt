package com.gms.backend.domain.domain.repository.branch

import com.gms.backend.domain.domain.model.branch.BranchPersonnel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface BranchPersonnelRepository : JpaRepository<BranchPersonnel, UUID> {
    fun findAllByBranch_Id(branchId: UUID): List<BranchPersonnel>
    fun findAllByBranch_IdAndStatus(
        branchId: UUID,
        status: BranchPersonnel.BranchPersonnelStatus
    ): List<BranchPersonnel>
}