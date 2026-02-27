package com.gms.backend.domain.application.rest.security

import com.gms.backend.domain.domain.service.security.PermissionCacheService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class PermissionHydrationFilter(
    private val permissionCacheService: PermissionCacheService
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val auth = SecurityContextHolder.getContext().authentication

        if (auth != null && auth.principal is CustomUserDetails) {
            val principal = auth.principal as CustomUserDetails

            // Try to get from Cache first; if missing, hit DB
            val authorities = permissionCacheService.getRolePermissions(principal.roles)

            principal.setAuthorities(authorities)
            val newAuth = UsernamePasswordAuthenticationToken(
                principal,
                auth.credentials,
                authorities
            )

            SecurityContextHolder.getContext().authentication = newAuth
        }

        chain.doFilter(request, response)
    }
}