package com.gms.backend.domain.domain.service.branch

import com.gms.backend.domain.domain.model.branch.Branch
import com.gms.backend.domain.application.rest.BranchController
import java.util.*

interface BranchService {
    fun createBranch(body: BranchController.BranchPostDTO): BranchController.BranchTableDTO
    fun updateBranch(id: UUID, body: BranchController.BranchPutDTO): BranchController.BranchTableDTO
    fun getBranches(): List<BranchController.BranchTableDTO>
    fun getBranchById(id: UUID): BranchController.BranchTableDTO
    fun deleteBranch(id: UUID)
}