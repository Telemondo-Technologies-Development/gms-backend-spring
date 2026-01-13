package com.gms.backend.domain.domain.repository.user

import com.gms.backend.domain.application.rest.user.UserController
import com.gms.backend.domain.domain.model.user.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, UUID> {
    // Projection function needs to end with By to work properly (JPA Convention)
    fun findByEmail(email: String): User?
    fun findAllProjectedBy(): List<UserController.UserTableDTO>
    fun findAllByActorIdIn(actorIds: Collection<UUID>): List<User>
}

