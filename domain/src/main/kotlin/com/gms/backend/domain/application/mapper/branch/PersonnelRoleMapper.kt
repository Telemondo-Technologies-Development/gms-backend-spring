package com.gms.backend.domain.application.mapper.branch

import com.gms.backend.domain.application.rest.branch.PersonnelRoleController
import com.gms.backend.domain.domain.model.branch.PersonnelRole
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface PersonnelRoleMapper {
    fun personnelRoleToPersonnelRoleTableDTO(personnelRole: PersonnelRole): PersonnelRoleController.PersonnelRoleTableDTO
    fun personnelRolePostDTOToPersonnelRole(dto: PersonnelRoleController.PersonnelRolePostDTO): PersonnelRole
    fun personnelRolePutDTOToPersonnelRole(dto: PersonnelRoleController.PersonnelRolePutDTO, @MappingTarget personnelRole: PersonnelRole): PersonnelRole
}