//package com.gms.backend.domain.application.rest.security
//
//import com.gms.backend.domain.domain.repository.user.UserRepository
//import com.gms.backend.domain.impl.domain.service.security.CustomUserDetailService
//import jakarta.servlet.FilterChain
//import jakarta.servlet.http.HttpServletRequest
//import jakarta.servlet.http.HttpServletResponse
//import org.springframework.security.authentication.AnonymousAuthenticationToken
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
//import org.springframework.security.core.context.SecurityContextHolder
//import org.springframework.security.web.context.HttpSessionSecurityContextRepository
//import org.springframework.stereotype.Component
//import org.springframework.web.filter.OncePerRequestFilter
//import java.time.Duration
//import java.time.Instant
//
//@Component
//class SessionUpdateFilter(
//    private val userDetailsService: CustomUserDetailService,
//    private val userRepository: UserRepository,
//    private val securityContextRepository: HttpSessionSecurityContextRepository =
//        HttpSessionSecurityContextRepository()
//) : OncePerRequestFilter() {
//
//    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
//        val session = request.getSession(false)
//        val auth = SecurityContextHolder.getContext().authentication
//
//        if (session != null && auth != null && auth.isAuthenticated && auth !is AnonymousAuthenticationToken) {
//            val now = Instant.now()
//            val lastChecked = session.getAttribute("LAST_ROLE_CHECK") as? Instant ?: Instant.MIN
//            val principal = auth.principal as? CustomUserDetails
//
//            // 1. Get the latest 'updatedAt' from the roles assigned to this specific user
//            // Only hit the DB if we haven't checked in the last 30 seconds
//            if (Duration.between(lastChecked, now).toSeconds() > 30) {
//                println("\n ROLE CHECK \n")
//                session.setAttribute("LAST_ROLE_CHECK", now)
//                val latestRoleChange = userRepository.findMaxRoleUpdateTimeByRoles(principal?.roles ?: emptyList())
//
//                // 2. Check if any role was modified AFTER the session began
//                if (latestRoleChange != null && latestRoleChange.isAfter(lastChecked)) {
//                    println("\n CHANGED FOUND \n")
//                    // 3. Fully reload user (including new permission set) from DB
//                    val freshUser = userDetailsService.loadUserByUsername(auth.name)
//
//                    val newAuth = UsernamePasswordAuthenticationToken(
//                        freshUser,
//                        auth.credentials,
//                        freshUser.authorities
//                    )
//
//                    val newContext = SecurityContextHolder.createEmptyContext()
//                    newContext.authentication = newAuth
//                    SecurityContextHolder.setContext(newContext)
//                    securityContextRepository.saveContext(newContext, request, response)
//                }
//            }
//        }
//        chain.doFilter(request, response)
//    }
//}