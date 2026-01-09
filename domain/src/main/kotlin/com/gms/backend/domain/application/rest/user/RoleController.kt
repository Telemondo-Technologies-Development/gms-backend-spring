package com.gms.backend.domain.application.rest.user

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.domain.model.user.Permission
import com.gms.backend.domain.impl.domain.service.role.RoleServiceImpl
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/role")
class RoleController(private val roleService: RoleServiceImpl) {

    data class RoleTableDTO(
        val id: UUID,
        val name: String,
        val description: String,
        val createdById: UUID,
        val updatedById: UUID
    )

    @GetMapping
    fun getAllRoles() = roleService.getRoles().toOkResponse()

    data class RolePermissionTableDTO(
        val id: UUID,
        val name: String,
        val description: String,
        val permissions: List<Permission>,
        val createdById: UUID,
        val updatedById: UUID
    )

    @GetMapping("/{id}")
    fun getRole(@PathVariable id: UUID) =
        roleService.getRoleById(id).toOkResponse()

    data class RolePostDTO(val name: String, val description: String, val createdById: UUID)

    @PostMapping
    fun createRole(@RequestBody body: RolePostDTO) =
        roleService.createRole(body).toCreatedResponse()

    data class RolePutDTO(val name: String, val description: String, val updatedById: UUID)

    @PutMapping("/{id}")
    fun updateRole(@PathVariable id: UUID, @RequestBody body: RolePutDTO) =
        roleService.updateRole(id, body).toOkResponse()

    @DeleteMapping("/{id}")
    fun deleteRole(@PathVariable id: UUID) =
        roleService.deleteRole(id).toOkResponse("Deleted Successfully")

    data class RolePermissionDTO(val permissionIds: MutableSet<UUID>)

    @PutMapping("/{id}/permission")
    fun updateRolePermissions(@PathVariable id: UUID, @RequestBody body: RolePermissionDTO) =
        roleService.updateRolePermissions(id, body).toOkResponse()

    @DeleteMapping("/{id}/permission")
    fun deleteRolePermissions(@PathVariable id: UUID, @RequestBody body: RolePermissionDTO) =
        roleService.deleteRolePermissions(id, body).toOkResponse("Deleted Successfully")

}