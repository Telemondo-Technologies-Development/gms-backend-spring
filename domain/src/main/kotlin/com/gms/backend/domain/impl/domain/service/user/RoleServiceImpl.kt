package com.gms.backend.domain.impl.domain.service.role

import com.gms.backend.domain.application.mapper.user.RoleMapper
import com.gms.backend.domain.application.response.ApiErrorType
import com.gms.backend.domain.application.response.DomainException
import com.gms.backend.domain.application.rest.user.RoleController
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.repository.user.PermissionRepository
import com.gms.backend.domain.domain.repository.user.RoleRepository
import com.gms.backend.domain.domain.service.user.RoleService
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
        val permissions = body.permissionIds.map {
            permissionRepository.getReferenceById(it)
        }.toMutableSet()

        val added = role.permissions.addAll(permissions)
        if (!added) throw DomainException(ApiErrorType.NULL_UPDATE)
    }

    @Transactional
    override fun deleteRolePermissions(id: UUID, body: RoleController.RolePermissionDTO) {
        val role = roleRepository.findById(id).orElseThrow()
        if (body.permissionIds.isEmpty()) throw DomainException(ApiErrorType.NO_DELETE)

        val permissions = body.permissionIds.map {
            permissionRepository.getReferenceById(it)
        }.toMutableSet()

        val removed = role.permissions.removeAll(permissions)
        if (!removed) throw DomainException(ApiErrorType.NULL_DELETE)
    }
}