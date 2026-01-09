package com.gms.backend.domain.domain.service.user

import com.gms.backend.domain.domain.model.user.Permission
import java.util.*

interface PermissionService {
    fun createPermission(body: MutableSet<String>)
    fun getPermissions(): List<Permission>
    fun getPermissionById(id: UUID): Optional<Permission>
}