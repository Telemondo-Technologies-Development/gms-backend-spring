package com.gms.backend.domain.impl.domain.service.user

import com.gms.backend.domain.application.mapper.user.PermissionMapper
import com.gms.backend.domain.domain.model.user.Permission
import com.gms.backend.domain.domain.repository.user.PermissionRepository
import com.gms.backend.domain.domain.service.user.PermissionService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class PermissionServiceImpl(
    private val permissionRepository: PermissionRepository,
    private val permissionMapper: PermissionMapper,
) : PermissionService {
    // TODO: Remove Create for Production
    @Transactional
    override fun createPermission(body: MutableSet<String>) {
        val permissions = permissionMapper.namesToPermission(body)
        permissionRepository.saveAll(permissions)
    }

    @Transactional(readOnly = true)
    override fun getPermissions(): List<Permission> {
        return permissionRepository.findAll()
    }

    @Transactional(readOnly = true)
    override fun getPermissionById(id: UUID): Optional<Permission> {
        return permissionRepository.findById(id)
    }
}