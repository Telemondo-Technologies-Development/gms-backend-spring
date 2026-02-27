package com.gms.backend.domain.domain.service.security

import org.springframework.security.core.GrantedAuthority

interface PermissionCacheService {
    fun invalidateRolePermissions(roleName: String)
    fun getRolePermissions(roles: List<String>): Collection<GrantedAuthority>
    fun clearAll()
}