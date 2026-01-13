package com.gms.backend.domain.application.rest.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


@Component
class JwtFilter(private val jwtUtil: JwtUtil, private val userDetailsService: UserDetailsService) :
    OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
    ) {
        val token = request.cookies?.firstOrNull { it.name == "jwt" }?.value

        if (token == null) return chain.doFilter(request, response)

        val data = jwtUtil.extractToken(token) ?: return chain.doFilter(request, response)
        val roles = data.getClaim("roles").asList(String::class.java) ?: emptyList()
        val authorities = roles.map { SimpleGrantedAuthority("ROLE_$it") }

        // TODO: Implement refresh token (on db) to get the best of both db session and jwt
        val auth = UsernamePasswordAuthenticationToken(data.subject, null, authorities)

        SecurityContextHolder.getContext().authentication = auth

        chain.doFilter(request, response)
    }
}