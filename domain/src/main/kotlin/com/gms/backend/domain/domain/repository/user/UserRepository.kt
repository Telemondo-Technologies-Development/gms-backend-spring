package com.gms.backend.domain.domain.repository.user

import com.gms.backend.domain.application.rest.user.UserController
import com.gms.backend.domain.domain.model.user.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.Instant
import java.util.*

interface UserRepository : JpaRepository<User, UUID> {
    // Projection function needs to end with By to work properly (JPA Convention)
    fun findByEmail(email: String): User?

    @Query(
        value = $$"""
        SELECT new com.gms.backend.domain.application.rest.user.UserController$UserTableDTO(
        u.id,
        u.email as username,
        u.createdAt,
        u.updatedAt,
        u.actorId
        )
        FROM User u
        ORDER BY u.createdAt DESC
        """,
        countQuery = "SELECT COUNT(u) FROM User u"
    )
    fun findAllProjectedBy(pageable: Pageable): Page<UserController.UserTableDTO>

    @Query(
        value = $$"""
        SELECT new com.gms.backend.domain.application.rest.user.UserController$UserTableDTO(
        u.id,
        u.email as username,
        u.createdAt,
        u.updatedAt,
        u.actorId
        )
        FROM User u
        WHERE id = :id
        ORDER BY u.createdAt DESC
        """
    )
    fun findByUserId(@Param("id") id: UUID): Optional<UserController.UserTableDTO>

    @Query(
        value = $$"""
        SELECT new com.gms.backend.domain.application.rest.user.UserController$UserRoleWithUserId(
        u.id as userId,
        r.id,
        r.name
        )
        FROM Role r
        JOIN r.userRoles u
        WHERE u.id IN :userIds
        """
    )
    fun findAllUserRole(@Param("userIds") userIds: List<UUID>): List<UserController.UserRoleWithUserId>

    @Query("""
        SELECT MAX(r.updatedAt) 
        FROM Role r
        WHERE r.name IN :roles
    """)
    fun findMaxRoleUpdateTimeByRoles(@Param("roles") roles: List<String>): Instant?
    fun findAllByActorIdIn(actorIds: Collection<UUID>): List<User>
}

