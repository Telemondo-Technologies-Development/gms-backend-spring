package com.gms.backend.domain.domain.service.user

import org.springframework.security.core.GrantedAuthority

interface PermissionCacheService {
    fun invalidateRolePermissions(roleName: String)
    fun getRolePermissions(roles: List<String>): Collection<GrantedAuthority>
    fun clearAll()
}