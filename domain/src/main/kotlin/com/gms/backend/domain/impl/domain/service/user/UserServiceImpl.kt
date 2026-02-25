package com.gms.backend.domain.impl.domain.service.user

import com.gms.backend.domain.application.mapper.user.UserMapper
import com.gms.backend.domain.application.response.ApiErrorType
import com.gms.backend.domain.application.response.DomainException
import com.gms.backend.domain.application.rest.user.UserController
import com.gms.backend.domain.domain.model.user.Actor
import com.gms.backend.domain.domain.repository.user.RoleRepository
import com.gms.backend.domain.domain.repository.user.UserRepository
import com.gms.backend.domain.domain.service.user.UserService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.session.FindByIndexNameSessionRepository
import org.springframework.session.Session
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Service
@PreAuthorize("denyAll()")
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder,
    private val sessionRepository: FindByIndexNameSessionRepository<out Session>,
) : UserService {

    @Transactional
    @PreAuthorize("hasAuthority('user_create')")
    override fun createUser(body: UserController.UserPostDTO): UserController.UserTableDTO {
        if (body.password.length !in 8..64) {
            throw DomainException(
                error = ApiErrorType.INVALID_CASE,
                description = "Password must be between 8 and 64 characters",
                field = "password"
            )
        }
        val roles = body.roles.map { roleRepository.getReferenceById(it) }
        val user = userMapper.userPostDTOToUser(body).apply {
            password = passwordEncoder.encode(body.password)!!
            actor = Actor().apply {
                type = Actor.ActorType.USER
                status = Actor.ActorStatus.ACTIVE
            }
            userRoles = roles.toMutableSet()
        }

        val saved = userRepository.saveAndFlush(user)
        return userMapper.userToUserTableDTO(saved)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('user_read')")
    override fun getUsers(pageable: Pageable): Page<UserController.UserTableDTO> {
        val users = userRepository.findAllProjectedBy(pageable)

        // 1. Collect IDs efficiently from the Page content
        val userIds = users.content.map { it.id }
        if (userIds.isEmpty()) return Page.empty(pageable)

        // 2. Fetch roles and group them by userId: O(M)
        // Result: Map<UUID, List<RoleBriefDTO>>
        val rolesByUserId: Map<UUID, List<UserController.UserRoleBriefDTO>> = userRepository.findAllUserRole(userIds)
            .groupBy({ it.userId }, { userMapper.userRoleToUserRoleBriefDTO(it) })

        // 3. Map to DTO using O(1) lookups
        return users.map { user ->
            user.apply {
                userRoles = rolesByUserId.getOrDefault(user.id, emptyList())
            }
        }
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('user_read')")
    override fun getUserById(id: UUID): UserController.UserTableDTO {
        val user = userRepository.findByUserId(id).orElseThrow()
        val roles = userRepository.findAllUserRole(listOf(user.id))
        return user.apply { userRoles = roles.map { userMapper.userRoleToUserRoleBriefDTO(it) } }
    }

    @Transactional
    @PreAuthorize("hasAuthority('user_update')")
    override fun updateUser(id: UUID, body: UserController.UserPutDTO): UserController.UserTableDTO {
        val user = userRepository.findById(id).orElseThrow().apply {
            userMapper.userPutDTOToUser(body, this)
            val roles = body.roles.map { roleRepository.getReferenceById(it) }
            userRoles.clear()
            userRoles.addAll(roles)
        }

        userRepository.saveAndFlush(user)
        // TODO: Only update when roles are changed
        // Assumes the role has been changed too
        val sessions = sessionRepository.findByPrincipalName(user.email)
        sessions.forEach { (sessionId, _) ->
            sessionRepository.deleteById(sessionId)
        }
        return userMapper.userToUserTableDTO(user)
    }

    @Transactional
    @PreAuthorize("hasAuthority('user_delete')")
    override fun deleteUser(id: UUID) {
        val user = userRepository.findById(id).orElseThrow().apply {
            actor.status = Actor.ActorStatus.DELETED
            actor.deactivatedAt = Instant.now()
        }

        userRepository.delete(user)
    }
}