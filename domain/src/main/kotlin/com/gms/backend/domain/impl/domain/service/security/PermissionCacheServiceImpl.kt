package com.gms.backend.domain.impl.domain.service.security

import com.github.benmanes.caffeine.cache.Cache
import com.gms.backend.domain.domain.repository.user.RoleRepository
import com.gms.backend.domain.domain.service.security.PermissionCacheService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service

@Service
@PreAuthorize("denyAll()")
class PermissionCacheServiceImpl(
    private val roleRepository: RoleRepository,
    private val permissionCache: Cache<List<String>, Collection<GrantedAuthority>>
): PermissionCacheService {
    // Call this when a Role's permissions are updated in the Admin Panel
    @PreAuthorize("permitAll()")
    override fun invalidateRolePermissions(roleName: String) {
        // Since our key is a List of roles, we find all keys containing this role and remove them
        val keysToRemove = permissionCache.asMap().keys.filter { it.contains(roleName) }
        permissionCache.invalidateAll(keysToRemove)
        println("Cache busted for role: $roleName. Impacted keys: ${keysToRemove.size}")
    }

    @PreAuthorize("permitAll()")
    override fun getRolePermissions(roles: List<String>): Collection<GrantedAuthority> {
        return permissionCache.get(roles.sorted()) { roleNames ->
            println("\n CACHE MISS \n")
            val permissionStrings = roleRepository.findPermissionNamesByRoleNames(roleNames)
            val perms = permissionStrings.map { SimpleGrantedAuthority(it) }.toMutableList()
            perms.addAll(roleNames.map { SimpleGrantedAuthority("ROLE_$it") })
            perms
        }
    }
    // Call this for a full system reset
    @PreAuthorize("permitAll()")
    override fun clearAll() {
        permissionCache.invalidateAll()
    }
}