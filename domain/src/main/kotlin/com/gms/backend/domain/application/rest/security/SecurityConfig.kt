package com.gms.backend.domain.application.rest.security

import com.gms.backend.domain.impl.domain.service.user.CustomUserDetailService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.context.*


@Configuration
//@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
@EnableMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val customUserDetailsService: CustomUserDetailService,
    private val permissionHydrationFilter: PermissionHydrationFilter
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider {
        val provider = DaoAuthenticationProvider(customUserDetailsService)
        provider.setPasswordEncoder(passwordEncoder())
        return provider
    }

    @Bean
    fun authenticationManager(
        http: HttpSecurity,
        provider: DaoAuthenticationProvider,
    ): AuthenticationManager {
        return http
            .getSharedObject(AuthenticationManagerBuilder::class.java)
            .authenticationProvider(provider)
            .build()
    }

    val publicEndpoints = arrayOf("/auth/login", "/v3/api-docs")

        @Bean
        fun filterChain(http: HttpSecurity): SecurityFilterChain {
            http
                .addFilterAfter(permissionHydrationFilter, SecurityContextHolderFilter::class.java)
//                .addFilterAfter(sessionUpdateFilter, SecurityContextHolderFilter::class.java)
                .csrf { csrf -> csrf.disable() } // TODO: Enable for protection
    //            .csrf { csrf ->
    //                csrf
    //                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
    //                    .csrfTokenRequestHandler(CsrfTokenRequestAttributeHandler())
    //            }
                .sessionManagement { session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                }
                .authorizeHttpRequests { auth ->
                    auth
                        .requestMatchers(*publicEndpoints)
                        .permitAll()
                        // Any other request
                        .anyRequest()
                        .authenticated()
                }
                .formLogin(Customizer.withDefaults())
                .logout { logout ->
                    logout.logoutUrl("/auth/logout")
                        .deleteCookies("SESSION")
                        .invalidateHttpSession(true)
                        .logoutSuccessUrl("/login")
                }

            return http.build()
        }

    @Bean
    fun securityContextRepository(): SecurityContextRepository {
        return DelegatingSecurityContextRepository(
            HttpSessionSecurityContextRepository(),
            RequestAttributeSecurityContextRepository()
        )
    }
}