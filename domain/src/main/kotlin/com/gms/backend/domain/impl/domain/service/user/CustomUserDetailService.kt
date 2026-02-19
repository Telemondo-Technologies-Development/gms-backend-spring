package com.gms.backend.domain.impl.domain.service.user

import com.gms.backend.domain.application.rest.security.CustomUserDetails
import com.gms.backend.domain.domain.model.user.Permission
import com.gms.backend.domain.domain.repository.branch.BranchRepository
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
    private val userRepository: UserRepository,
    private val branchRepository: BranchRepository
) : UserDetailsService {

    @Transactional
    @PreAuthorize("permitAll()")
    override fun loadUserByUsername(email: String): UserDetails {
        val user =
            userRepository.findByEmail(email)
                ?: throw UsernameNotFoundException("User not found")

        // Query the branches the user is under
        val branches = branchRepository.findBranchByUserId(user.id)
        val permission: MutableSet<Permission> = mutableSetOf()
        val roles = user.userRoleRoles
        roles.forEach { permission.addAll(it.permissions) }
        // Role
        val authorities = roles.map { SimpleGrantedAuthority("ROLE_${it.name}") }.toMutableList()
        // Permissions
        authorities.addAll(permission.map { SimpleGrantedAuthority(it.name) })
        // Create the compact version for the frontend
        val stringPermissions: List<String> = authorities.map { it.authority }
        val compactPermissions = convertPermissions(stringPermissions)

        return CustomUserDetails(
            email = user.email,
            roles = roles.map { role -> role.name },
            compactPermissions = compactPermissions,
            branches = branches,
            actorId = user.actorId!!,
            password = user.password,
            authorities = authorities,
        )
    }

    fun convertPermissions(rawList: List<String>): Map<String, List<Char>> {
        val mapping = mapOf(
            "create" to 'c',
            "read"   to 'r',
            "update" to 'u',
            "delete" to 'd',
            "upload" to 'c' // Mapping 'upload' to 'create' for object storage
        )

        return rawList
            .filter { it.contains("_" ) && !it.startsWith("ROLE_") } // Removes "ROLE_ADMIN" and malformed strings
            .map { it.split("_") }
            .groupBy(
                keySelector = { it[0] },      // Resource (e.g., "member")
                valueTransform = { it[1] }    // Action (e.g., "create")
            )
            .mapValues { (_, actions) ->
                actions.mapNotNull { mapping[it] }
                    .distinct()
                    .sorted() // Optional: keeps output as [c, d, r, u]
            }
            .toSortedMap()
    }
}
