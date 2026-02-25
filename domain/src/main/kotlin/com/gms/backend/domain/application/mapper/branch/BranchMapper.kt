package com.gms.backend.domain.application.mapper.branch

import com.gms.backend.domain.application.rest.branch.BranchController
import com.gms.backend.domain.domain.model.branch.Branch
import com.gms.backend.domain.domain.model.user.Employee
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface BranchMapper {
    fun branchDTOToBranch(dto: BranchController.BranchPostDTO): Branch
    fun branchPutDTOToBranch(
        dto: BranchController.BranchPutDTO,
        @MappingTarget branch: Branch
    ): Branch

    fun branchToDTO(branch: Branch): BranchController.BranchTableDTO
    fun branchesToDTO(branches: List<Branch>): List<BranchController.BranchTableDTO>

    // For branch's list of employee
    fun branchToSummaryDTO(branch: Branch): BranchController.BranchSummaryDTO
    fun employeeToSummaryDTO(employee: Employee): BranchController.EmployeeSummaryDTO
}


