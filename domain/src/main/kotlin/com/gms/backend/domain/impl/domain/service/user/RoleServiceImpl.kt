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
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class RoleServiceImpl(
    private val roleRepository: RoleRepository,
    private val roleMapper: RoleMapper,
    private val permissionRepository: PermissionRepository,
    private val actorRepository: ActorRepository,
) : RoleService {
    @Transactional
    override fun createRole(body: RoleController.RolePostDTO) {
        val role = roleMapper.rolePostDTOToRole(body)
        role.createdBy = actorRepository.getReferenceById(body.createdById)
        role.updatedBy = actorRepository.getReferenceById(body.createdById)
        roleRepository.save(role)
    }

    @Transactional(readOnly = true)
    override fun getRoles(): List<RoleController.RoleTableDTO> {
        return roleRepository.findAllProjectedBy()
    }

    @Transactional(readOnly = true)
    override fun getRoleById(id: UUID): RoleController.RolePermissionTableDTO {
        return roleRepository.findById(id).orElseThrow().let(roleMapper::roleToRolePermissionTableDTO)
    }

    @Transactional
    override fun updateRole(id: UUID, body: RoleController.RolePutDTO) {
        val role = roleRepository.findById(id).orElseThrow()
        roleMapper.rolePutDTOToRole(body, role)
        role.updatedBy = actorRepository.getReferenceById(body.updatedById)
    }

    @Transactional
    override fun deleteRole(id: UUID) {
        return roleRepository.deleteById(id)
    }


    @Transactional
    override fun updateRolePermissions(id: UUID, body: RoleController.RolePermissionDTO) {
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
    }

    @Transactional
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