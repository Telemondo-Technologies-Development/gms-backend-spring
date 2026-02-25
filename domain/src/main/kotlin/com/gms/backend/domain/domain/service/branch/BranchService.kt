package com.gms.backend.domain.domain.service.branch

import com.gms.backend.domain.application.rest.branch.BranchController
import com.gms.backend.domain.domain.model.branch.BranchPersonnel
import com.gms.backend.domain.domain.model.user.Employee
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
        branchPersonnelStatus: BranchPersonnel.BranchPersonnelStatus?,
        employeeStatus: Employee.EmployeeStatus?,
        pageable: Pageable
    ): Page<BranchController.EmployeeInBranchDTO>
}