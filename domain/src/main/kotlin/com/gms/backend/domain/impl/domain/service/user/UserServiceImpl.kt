package com.gms.backend.domain.impl.domain.service.user

import com.gms.backend.domain.application.mapper.user.UserMapper
import com.gms.backend.domain.application.rest.user.UserController
import com.gms.backend.domain.domain.model.user.Actor
import com.gms.backend.domain.domain.repository.user.UserRepository
import com.gms.backend.domain.domain.service.user.UserService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Service
@PreAuthorize("denyAll()")
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
    private val passwordEncoder: PasswordEncoder
) : UserService {

    @Transactional
    @PreAuthorize("hasAuthority('user_create')")
    override fun createUser(body: UserController.UserPostDTO): UserController.UserTableDTO {
        val user = userMapper.userPostDTOToUser(body).apply {
            password = passwordEncoder.encode(body.password)!!
            actor = Actor().apply {
                type = Actor.ActorType.USER
                status = Actor.ActorStatus.ACTIVE
            }
        }

        val saved = userRepository.saveAndFlush(user)
        return userMapper.userToUserTableDTO(saved)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('user_read')")
    override fun getUsers(pageable: Pageable): Page<UserController.UserTableDTO> {
        return userRepository.findAllProjectedBy(pageable)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('user_read')")
    override fun getUserById(id: UUID): UserController.UserTableDTO {
        return userRepository.findById(id).orElseThrow().let(userMapper::userToUserTableDTO)
    }

    @Transactional
    @PreAuthorize("hasAuthority('user_update')")
    override fun updateUser(id: UUID, body: UserController.UserPutDTO): UserController.UserTableDTO {
        val user = userRepository.findById(id).orElseThrow().apply {
            userMapper.userPutDTOToUser(body, this)
        }

        userRepository.saveAndFlush(user)
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