package com.gms.backend.domain.domain.service.user

import com.gms.backend.domain.application.rest.user.RoleController
import java.util.*

interface RoleService {
    fun createRole(body: RoleController.RolePostDTO)
    fun getRoles(): List<RoleController.RoleTableDTO>
    fun getRoleById(id: UUID): RoleController.RolePermissionTableDTO
    fun updateRole(id: UUID, body: RoleController.RolePutDTO)
    fun deleteRole(id: UUID)
    fun updateRolePermissions(id: UUID, body: RoleController.RolePermissionDTO)
    fun deleteRolePermissions(id: UUID, body: RoleController.RolePermissionDTO)
}