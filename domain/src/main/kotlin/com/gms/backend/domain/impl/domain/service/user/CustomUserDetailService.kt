package com.gms.backend.domain.impl.domain.service.user

import com.gms.backend.domain.application.rest.security.CustomUserDetails
import com.gms.backend.domain.domain.repository.branch.BranchRepository
import com.gms.backend.domain.domain.repository.user.UserRepository
import com.gms.backend.domain.domain.service.user.PermissionCacheService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@PreAuthorize("denyAll()")
class CustomUserDetailService(
    private val userRepository: UserRepository,
    private val branchRepository: BranchRepository,
    private val permissionCacheService: PermissionCacheService
) : UserDetailsService {

    @Transactional
    @PreAuthorize("permitAll()")
    override fun loadUserByUsername(email: String): UserDetails {
        val user =
            userRepository.findByEmail(email)
                ?: throw UsernameNotFoundException("User not found")

        // Query the branches the user is under
        val branches = branchRepository.findBranchByUserId(user.id)
        val roles = user.userRoles.map { it.name }
        // Role
        val authorities = permissionCacheService.getRolePermissions(roles)

        return CustomUserDetails(
            email = user.email,
            roles = roles,
            branches = branches,
            actorId = user.actorId!!,
            password = user.password,
            authorities = authorities
        )
    }
}
