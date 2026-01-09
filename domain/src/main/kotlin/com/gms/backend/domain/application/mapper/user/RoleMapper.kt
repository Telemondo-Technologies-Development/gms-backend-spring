package com.gms.backend.domain.application.mapper.user

import com.gms.backend.domain.application.rest.user.RoleController
import com.gms.backend.domain.domain.model.user.Role
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface RoleMapper {
    fun roleToRolePermissionTableDTO(role: Role): RoleController.RolePermissionTableDTO
    fun rolePostDTOToRole(dto: RoleController.RolePostDTO): Role
    fun rolePutDTOToRole(dto: RoleController.RolePutDTO, @MappingTarget role: Role): Role
}