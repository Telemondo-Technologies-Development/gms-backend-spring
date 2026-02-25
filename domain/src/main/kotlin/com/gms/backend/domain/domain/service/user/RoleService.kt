package com.gms.backend.domain.domain.service.user

import com.gms.backend.domain.application.rest.user.RoleController
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface RoleService {
    fun createRole(body: RoleController.RolePostDTO): RoleController.RoleTableDTO
    fun getRoles(pageable: Pageable): Page<RoleController.RoleTableDTO>
    fun getRoleById(id: UUID): RoleController.RolePermissionTableDTO
    fun updateRole(id: UUID, body: RoleController.RolePutDTO): RoleController.RoleTableDTO
    fun deleteRole(id: UUID)
    fun updateRolePermissions(id: UUID, body: RoleController.RolePermissionDTO): RoleController.RolePermissionTableDTO
    fun addRolePermissions(id: UUID, body: RoleController.RolePermissionDTO): RoleController.RolePermissionTableDTO
    fun deleteRolePermissions(id: UUID, body: RoleController.RolePermissionDTO)
}