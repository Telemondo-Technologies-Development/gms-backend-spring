package com.gms.backend.domain.impl.domain.service.user

import com.gms.backend.domain.application.rest.security.CustomUserDetails
import com.gms.backend.domain.domain.model.user.Permission
import com.gms.backend.domain.domain.repository.user.UserRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@PreAuthorize("denyAll()")
class CustomUserDetailService(
    private val userRepository: UserRepository
) : UserDetailsService {

    @Transactional
    @PreAuthorize("permitAll()")
    override fun loadUserByUsername(email: String): UserDetails {
        val user =
            userRepository.findByEmail(email)
                ?: throw UsernameNotFoundException("User not found")


        val permission: MutableSet<Permission> = mutableSetOf()
        val roles = user.userRoleRoles
        roles.forEach { permission.addAll(it.permissions) }
        // Role
        val authorities = roles.map { SimpleGrantedAuthority("ROLE_${it.name}") } as MutableList
        // Permissions
        authorities.addAll(permission.map { SimpleGrantedAuthority(it.name) })

        return CustomUserDetails(
            user.email,
            user.actorId!!,
            user.password,
            authorities,
        )
    }
}
