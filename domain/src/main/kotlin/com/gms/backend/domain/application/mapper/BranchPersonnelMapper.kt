package com.gms.backend.domain.application.mapper

import com.gms.backend.domain.application.rest.BranchPersonnelController
import com.gms.backend.domain.domain.model.branch.BranchPersonnel
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget

@Mapper(componentModel = "spring")
interface BranchPersonnelMapper {
    fun branchPersonnelDTOToBranchPersonnel(dto: BranchPersonnelController.BranchPersonnelPostDTO): BranchPersonnel
    fun branchPersonnelPutDTOToBranchPersonnel(
        dto: BranchPersonnelController.BranchPersonnelPutDTO,
        @MappingTarget branchPersonnel: BranchPersonnel
    ): BranchPersonnel

    fun branchPersonnelToDTO(branchPersonnel: BranchPersonnel): BranchPersonnelController.BranchPersonnelTableDTO
    fun branchPersonnelsToDTO(branchPersonnels: List<BranchPersonnel>): List<BranchPersonnelController.BranchPersonnelTableDTO>
}
