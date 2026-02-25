package com.gms.backend.domain.application.rest.security

import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.rest.branch.BranchController
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.context.DelegatingSecurityContextRepository
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository
import org.springframework.security.web.context.SecurityContextRepository
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val securityContextRepository: SecurityContextRepository = DelegatingSecurityContextRepository(
        HttpSessionSecurityContextRepository(), RequestAttributeSecurityContextRepository()
    )
) {

    data class LogInDTO(var username: String, var password: String)
    data class LogInResponse(
        var email: String,
        var actorId: UUID,
        var branches: List<BranchController.BranchListDTO>,
        var roles: List<String>,
        val permissions: Map<String, List<Char>>
    )

    fun convertPermissions(authorities: Collection<GrantedAuthority>): Map<String, List<Char>> {
        val stringPermissions: List<String> = authorities.map { it.authority.toString() }
        val mapping = mapOf(
            "create" to 'c',
            "read" to 'r',
            "update" to 'u',
            "delete" to 'd'
        )

        return stringPermissions
            .filter { it.contains("_") && !it.startsWith("ROLE_") } // Removes "ROLE_ADMIN" and malformed strings
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

    @GetMapping("/me")
    fun getMyDetails(@AuthenticationPrincipal principal: CustomUserDetails) = LogInResponse(
        principal.email,
        principal.actorId,
        principal.branches,
        principal.roles,
        convertPermissions(principal.authorities)
    ).toOkResponse()

    @PostMapping("/login")
    fun login(
        @RequestBody body: LogInDTO, request: HttpServletRequest, response: HttpServletResponse
    ): ResponseEntity<LogInResponse> {
        // 1. Create the authentication token from request data
        val authToken = UsernamePasswordAuthenticationToken.unauthenticated(body.username, body.password)

        // 2. Authenticate the user
        val authentication = authenticationManager.authenticate(authToken)

        // 3. Create a new Security Context and set the authentication
        val context = SecurityContextHolder.createEmptyContext()
        context.authentication = authentication
        SecurityContextHolder.setContext(context)

        // 4. PERSIST the context to the session/database
        securityContextRepository.saveContext(context, request, response)

//        val session = request.getSession(true)
//        session.setAttribute("LAST_ROLE_CHECK", Instant.now())

        val csrfToken = request.getAttribute(CsrfToken::class.java.name) as? CsrfToken

        val responseBuilder = ResponseEntity.ok()

        csrfToken?.let {
            responseBuilder.header("X-CSRF-TOKEN", it.token)
        }

        val principal = authentication.principal as CustomUserDetails
        val response = LogInResponse(
            principal.email,
            principal.actorId,
            principal.branches,
            principal.roles,
            convertPermissions(principal.authorities)
        )
        return responseBuilder.body(response)
    }
}