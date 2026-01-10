package com.gms.backend.domain.application.mapper.branch

import com.gms.backend.domain.application.rest.branch.BranchPersonnelController
import com.gms.backend.domain.domain.model.branch.BranchPersonnel
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface BranchPersonnelMapper {
    fun branchPersonnelDTOToBranchPersonnel(dto: BranchPersonnelController.BranchPersonnelPostDTO): BranchPersonnel
    fun branchPersonnelPutDTOToBranchPersonnel(
        dto: BranchPersonnelController.BranchPersonnelPutDTO,
        @MappingTarget branchPersonnel: BranchPersonnel
    ): BranchPersonnel

    fun branchPersonnelToDTO(branchPersonnel: BranchPersonnel): BranchPersonnelController.BranchPersonnelTableDTO
    fun branchPersonnelsToDTO(branchPersonnels: List<BranchPersonnel>): List<BranchPersonnelController.BranchPersonnelTableDTO>
}
