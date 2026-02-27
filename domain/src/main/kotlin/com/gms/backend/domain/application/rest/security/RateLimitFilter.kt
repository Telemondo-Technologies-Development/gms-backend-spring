package com.gms.backend.domain.application.rest.security

import com.gms.backend.domain.application.response.RateLimitException
import com.gms.backend.domain.impl.domain.service.security.RateLimitService
import com.gms.backend.domain.impl.domain.service.security.RequestIdentity
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver

@Component
class RateLimitFilter(
    private val rateLimitService: RateLimitService,
    @Qualifier("handlerExceptionResolver")
    private val resolver: HandlerExceptionResolver
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val identity = resolveIdentity(request)
            val bucket = rateLimitService.resolveBucket(identity)
            val probe = bucket.tryConsumeAndReturnRemaining(1)

            if (probe.isConsumed) {
                response.addHeader("X-Rate-Limit-Remaining", probe.remainingTokens.toString())
                filterChain.doFilter(request, response)
            } else {
                resolver.resolveException(
                    request, response, null,
                    RateLimitException(probe.nanosToWaitForRefill / 1_000_000_000)
                )
            }
        } catch (e: Exception) {
            resolver.resolveException(request, response, null, e)
        }
    }

    private fun resolveIdentity(request: HttpServletRequest): RequestIdentity {
        val uri = request.requestURI

        if (uri.contains("/auth/login")) return RequestIdentity.LoginPath

        // 2. Try to find the user in the session
        val session = request.getSession(false)
        val securityContext = session?.getAttribute("SPRING_SECURITY_CONTEXT") as? SecurityContext
        val auth = securityContext?.authentication

        // 3. Determine if authenticated or guest
        return if (auth != null && auth.isAuthenticated && auth !is AnonymousAuthenticationToken) {
            RequestIdentity.Authenticated(auth.name)
        } else {
            RequestIdentity.Guest(getRemoteIp(request))
        }
    }

    private fun getRemoteIp(request: HttpServletRequest): String {
        val xf = request.getHeader("X-Forwarded-For")
        return xf?.split(",")?.firstOrNull()?.trim() ?: request.remoteAddr
    }

    private fun isIpBlacklisted(ip: String): Boolean = false // Future logic placeholder
}