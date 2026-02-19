package com.gms.backend.domain.application.rest.user

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.domain.model.user.Permission
import com.gms.backend.domain.impl.domain.service.user.RoleServiceImpl
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/role")
@Tag(name = "Access Control")
class RoleController(private val roleService: RoleServiceImpl) {

    @Schema(description = "Format for Role read")
    data class RoleTableDTO(
        val id: UUID,
        val name: String,
        val description: String,
        val createdById: UUID?,
        val updatedById: UUID?
    )


    @Schema(description = "Format for Role Permission read")
    data class RolePermissionTableDTO(
        val id: UUID,
        val name: String,
        val description: String,
        val permissions: List<Permission>,
        val createdById: UUID?,
        val updatedById: UUID?
    )

    @Schema(description = "Format for Role create")
    data class RolePostDTO(
        @field:NotBlank
        val name: String,
        @field:Size(min = 1, message = "Description cannot be blank if provided")
        val description: String,
        val createdById: UUID
    )

    @Schema(description = "Format for Role update")
    data class RolePutDTO(
        @field:NotBlank
        val name: String,
        @field:Size(min = 1, message = "Description cannot be blank if provided")
        val description: String,
        val updatedById: UUID
    )

    @Schema(description = "Format for Role Permission's update & delete")
    data class RolePermissionDTO(
        val permissionIds: MutableSet<UUID>
    )

    @GetMapping
    @Operation(summary = "Get all Roles")
    fun getAllRoles(pageable: Pageable) = roleService.getRoles(pageable).toPaginatedResponse()


    @GetMapping("/{id}")
    @Operation(summary = "Get a Role with its Permissions by id")
    fun getRole(@PathVariable id: UUID) =
        roleService.getRoleById(id).toOkResponse()

    @PostMapping
    @Operation(summary = "Create a new Role")
    fun createRole(@Valid @RequestBody body: RolePostDTO) =
        roleService.createRole(body).toCreatedResponse()

    @Operation(summary = "Update a Role by id")
    @PutMapping("/{id}")
    fun updateRole(@PathVariable id: UUID, @Valid @RequestBody body: RolePutDTO) =
        roleService.updateRole(id, body).toOkResponse()

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Role by id")
    fun deleteRole(@PathVariable id: UUID) =
        roleService.deleteRole(id).toOkResponse("Deleted Successfully")

    @PutMapping("/{id}/permission")
    @Operation(summary = "Update a Role's Permission by id")
    fun updateRolePermissions(@PathVariable id: UUID, @RequestBody body: RolePermissionDTO) =
        roleService.updateRolePermissions(id, body).toOkResponse()

    @DeleteMapping("/{id}/permission")
    @Operation(summary = "Delete a Role's Permission by id")
    fun deleteRolePermissions(@PathVariable id: UUID, @RequestBody body: RolePermissionDTO) =
        roleService.deleteRolePermissions(id, body).toOkResponse("Deleted Successfully")

}