package com.gms.backend.domain.domain.repository.user

import com.gms.backend.domain.domain.model.user.Permission
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface PermissionRepository : JpaRepository<Permission, UUID> {
    fun findAllByRolePermissionRolesId(id: UUID): List<Permission>
}
