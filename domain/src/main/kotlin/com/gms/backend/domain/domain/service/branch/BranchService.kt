package com.gms.backend.domain.domain.service.branch

import com.gms.backend.domain.application.rest.BranchController
import com.gms.backend.domain.domain.model.branch.BranchPersonnel
import java.util.*

interface BranchService {
    fun createBranch(body: BranchController.BranchPostDTO): BranchController.BranchTableDTO
    fun updateBranch(id: UUID, body: BranchController.BranchPutDTO): BranchController.BranchTableDTO
    fun getBranches(): List<BranchController.BranchTableDTO>
    fun getBranchById(id: UUID): BranchController.BranchTableDTO
    fun deleteBranch(id: UUID)
    fun getBranchEmployees(
        branchId: UUID,
        status: BranchPersonnel.BranchPersonnelStatus?
    ): BranchController.BranchEmployeesDTO
}