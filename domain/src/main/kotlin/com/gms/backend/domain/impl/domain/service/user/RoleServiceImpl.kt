package com.gms.backend.domain.impl.domain.service.user

import com.gms.backend.domain.application.mapper.user.RoleMapper
import com.gms.backend.domain.application.response.ApiErrorType
import com.gms.backend.domain.application.response.DomainException
import com.gms.backend.domain.application.rest.user.RoleController
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.repository.user.PermissionRepository
import com.gms.backend.domain.domain.repository.user.RoleRepository
import com.gms.backend.domain.domain.service.user.RoleService
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@PreAuthorize("denyAll()")
class RoleServiceImpl(
    private val roleRepository: RoleRepository,
    private val roleMapper: RoleMapper,
    private val permissionRepository: PermissionRepository,
    private val actorRepository: ActorRepository,
) : RoleService {
    @Transactional
    @PreAuthorize("hasAuthority('role_create')")
    override fun createRole(body: RoleController.RolePostDTO): RoleController.RoleTableDTO {
        val role = roleMapper.rolePostDTOToRole(body).apply {
            createdBy = actorRepository.getReferenceById(body.createdById)
            updatedBy = actorRepository.getReferenceById(body.createdById)
        }

        val saved = roleRepository.save(role)
        return roleMapper.roleToRoleTableDTO(saved)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('role_read')")
    override fun getRoles(): List<RoleController.RoleTableDTO> {
        return roleRepository.findAllProjectedBy()
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('role_read') and hasAuthority('permission_read')")
    override fun getRoleById(id: UUID): RoleController.RolePermissionTableDTO {
        return roleRepository.findById(id).orElseThrow().let(roleMapper::roleToRolePermissionTableDTO)
    }

    @Transactional
    @PreAuthorize("hasAuthority('role_update')")
    override fun updateRole(id: UUID, body: RoleController.RolePutDTO): RoleController.RoleTableDTO {
        val role = roleRepository.findById(id).orElseThrow().apply {
            roleMapper.rolePutDTOToRole(body, this)
            updatedBy = actorRepository.getReferenceById(body.updatedById)
        }

        roleRepository.save(role)
        return roleMapper.roleToRoleTableDTO(role)
    }

    @Transactional
    @PreAuthorize("hasAuthority('role_delete')")
    override fun deleteRole(id: UUID) {
        return roleRepository.deleteById(id)
    }


    @Transactional
    @PreAuthorize("hasAuthority('rolePermission_update')")
    override fun updateRolePermissions(id: UUID, body: RoleController.RolePermissionDTO): RoleController.RolePermissionTableDTO {
        val role = roleRepository.findById(id).orElseThrow()

        val foundPermissions = permissionRepository.findAllById(body.permissionIds)
        if (foundPermissions.size != body.permissionIds.size) {
            val foundIds = foundPermissions.map { it.id }.toSet()
            val missingIds = body.permissionIds.filter { it !in foundIds }
            throw DomainException(
                ApiErrorType.MISSING_UPDATE,
                "Some permissions do not exist: $missingIds",
                "Role Permission Update Failed",
                HttpStatus.NOT_FOUND
            )
        }

        role.permissions.addAll(foundPermissions)
        return roleMapper.roleToRolePermissionTableDTO(role)
    }

    @Transactional
    @PreAuthorize("hasAuthority('rolePermission_delete')")
    override fun deleteRolePermissions(id: UUID, body: RoleController.RolePermissionDTO) {
        val role = roleRepository.findById(id).orElseThrow()
        if (body.permissionIds.isEmpty()) throw DomainException(ApiErrorType.NO_DELETE)

        // Strict Checking to ensure intent is right
        val foundPermissions = permissionRepository.findAllById(body.permissionIds)
        if (foundPermissions.size != body.permissionIds.size) {
            val foundIds = foundPermissions.map { it.id }.toSet()
            val missingIds = body.permissionIds.filter { it !in foundIds }
            throw DomainException(
                ApiErrorType.MISSING_DELETE,
                "Some permissions do not exist: $missingIds",
                "Role Permission Delete Failed",
                HttpStatus.NOT_FOUND
            )
        }

        role.permissions.removeAll(foundPermissions)
    }
}