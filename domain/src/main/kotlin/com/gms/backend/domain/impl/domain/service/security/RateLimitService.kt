package com.gms.backend.domain.impl.domain.service.security

import com.github.benmanes.caffeine.cache.Caffeine
import io.github.bucket4j.Bucket
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import java.time.Duration.ofMinutes
import java.util.concurrent.TimeUnit

sealed class RequestIdentity {
    data class LoginPath(val ip: String, val userAgent: String) : RequestIdentity()
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
            is RequestIdentity.LoginPath -> "LIMIT_LOGIN_${identity.ip}_${identity.userAgent.hashCode()}" // Might be susceptible to multiple user agents
            is RequestIdentity.Authenticated -> "LIMIT_AUTH_${identity.userId}"
            is RequestIdentity.Guest -> "LIMIT_GUEST_${identity.ip}"
        }

        return cache.get(key) { createBucketForIdentity(identity) }
    }

    @PreAuthorize("permitAll()")
    private fun createBucketForIdentity(identity: RequestIdentity): Bucket {
        return when (identity) {
            // TODO: Create multiple buckets for login (ip, ip+hash)
            is RequestIdentity.LoginPath ->
                Bucket.builder().addLimit { limit -> limit.capacity(10).refillIntervally(10, ofMinutes(5)) }.build()

            is RequestIdentity.Authenticated ->
                Bucket.builder().addLimit { limit -> limit.capacity(100).refillIntervally(100, ofMinutes(1)) }.build()

            is RequestIdentity.Guest ->
                Bucket.builder().addLimit { limit -> limit.capacity(40).refillIntervally(40, ofMinutes(3)) }.build()
        }
    }
}