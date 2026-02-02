package com.gms.backend.domain.application.rest.security

import com.gms.backend.domain.application.rest.branch.BranchController
import org.springframework.security.core.CredentialsContainer
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID

class CustomUserDetails(
    val email: String,
    val branches: List<BranchController.BranchListDTO>,
    val actorId: UUID,
    private var password: String?,
    private val authorities: Collection<GrantedAuthority>
) : UserDetails, CredentialsContainer {

    override fun getAuthorities() = authorities
    override fun getPassword() = password
    override fun getUsername() = email

    // This is the magic method called by Spring to wipe sensitive data
    override fun eraseCredentials() {
        this.password = null
    }

    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true
}