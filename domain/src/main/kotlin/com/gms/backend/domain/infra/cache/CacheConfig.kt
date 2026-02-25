package com.gms.backend.domain.infra.cache

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.GrantedAuthority
import java.util.concurrent.TimeUnit

@Configuration
class CacheConfig {

    @Bean
    fun permissionCache(): Cache<List<String>, Collection<GrantedAuthority>> {
        return Caffeine.newBuilder()
            .maximumSize(50)
            .expireAfterWrite(8, TimeUnit.HOURS)
            .build()
    }

}