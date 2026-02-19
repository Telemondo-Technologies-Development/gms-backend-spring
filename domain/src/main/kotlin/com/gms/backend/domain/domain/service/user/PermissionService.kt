package com.gms.backend.domain.domain.service.user

import com.gms.backend.domain.domain.model.user.Permission
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface PermissionService {
    fun createPermission(body: MutableSet<String>)
    fun getPermissions(pageable: Pageable): Page<Permission>
    fun getPermissionById(id: UUID): Permission
}