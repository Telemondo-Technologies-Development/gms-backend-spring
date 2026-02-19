package com.gms.backend.domain.domain.service.branch

import com.gms.backend.domain.application.rest.branch.BranchController
import com.gms.backend.domain.domain.model.branch.BranchPersonnel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface BranchService {
    fun createBranch(body: BranchController.BranchPostDTO): BranchController.BranchTableDTO
    fun updateBranch(id: UUID, body: BranchController.BranchPutDTO): BranchController.BranchTableDTO
    fun getBranches(pageable: Pageable): Page<BranchController.BranchTableDTO>
    fun getBranchById(id: UUID): BranchController.BranchTableDTO
    fun deleteBranch(id: UUID)
    fun getBranchEmployees(
        branchId: UUID,
        status: BranchPersonnel.BranchPersonnelStatus?,
        pageable: Pageable
    ): Page<BranchController.EmployeeInBranchDTO>
}