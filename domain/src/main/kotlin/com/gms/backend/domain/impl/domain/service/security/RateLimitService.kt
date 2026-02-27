package com.gms.backend.domain.impl.domain.service.security

import com.github.benmanes.caffeine.cache.Caffeine
import io.github.bucket4j.Bucket
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import java.time.Duration.ofMinutes
import java.util.concurrent.TimeUnit

sealed class RequestIdentity {
    object LoginPath : RequestIdentity()
    data class Authenticated(val userId: String) : RequestIdentity()
    data class Guest(val ip: String) : RequestIdentity()
}

@Service
@PreAuthorize("denyAll()")
class RateLimitService {

    private val cache = Caffeine.newBuilder()
        .expireAfterAccess(2, TimeUnit.HOURS)
        .maximumSize(1_000) // For a small company
        .build<String, Bucket>()

    @PreAuthorize("permitAll()")
    fun resolveBucket(identity: RequestIdentity): Bucket {
        val key = when (identity) {
            is RequestIdentity.LoginPath -> "LIMIT_LOGIN_${identity}" // Global or IP-based key
            is RequestIdentity.Authenticated -> "LIMIT_AUTH_${identity.userId}"
            is RequestIdentity.Guest -> "LIMIT_GUEST_${identity.ip}"
        }

        return cache.get(key) { createBucketForIdentity(identity) }
    }

    @PreAuthorize("permitAll()")
    private fun createBucketForIdentity(identity: RequestIdentity): Bucket {
        return when (identity) {
            is RequestIdentity.LoginPath ->
                Bucket.builder().addLimit { limit -> limit.capacity(5).refillIntervally(5, ofMinutes(15)) }.build()

            is RequestIdentity.Authenticated ->
                Bucket.builder().addLimit { limit -> limit.capacity(100).refillIntervally(100, ofMinutes(1)) }.build()

            is RequestIdentity.Guest ->
                Bucket.builder().addLimit { limit -> limit.capacity(20).refillIntervally(20, ofMinutes(3)) }.build()
        }
    }
}