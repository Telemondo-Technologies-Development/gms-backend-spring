package com.gms.backend.domain.application.rest.security

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.context.DelegatingSecurityContextRepository
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository
import org.springframework.security.web.context.SecurityContextRepository
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val securityContextRepository: SecurityContextRepository = DelegatingSecurityContextRepository(
        HttpSessionSecurityContextRepository(),
        RequestAttributeSecurityContextRepository()
    )
) {

    data class LogInDTO(var email: String, var password: String)

    @PostMapping("/login")
    fun login(
        @RequestBody body: LogInDTO,
        request: HttpServletRequest,
        response: HttpServletResponse
    ): ResponseEntity<String> {
        // 1. Create the authentication token from request data
        val authToken = UsernamePasswordAuthenticationToken.unauthenticated(body.email, body.password)

        // 2. Authenticate the user
        val authentication = authenticationManager.authenticate(authToken)

        // 3. Create a new Security Context and set the authentication
        val context = SecurityContextHolder.createEmptyContext()
        context.authentication = authentication
        SecurityContextHolder.setContext(context)

        // 4. PERSIST the context to the session/database
        securityContextRepository.saveContext(context, request, response)

        val csrfToken = request.getAttribute(CsrfToken::class.java.name) as? CsrfToken

        val responseBuilder = ResponseEntity.ok()

        csrfToken?.let {
            responseBuilder.header("X-CSRF-TOKEN", it.token)
        }

        return responseBuilder.body("Logged in successfully")
    }
}