package com.gms.backend.domain.impl.domain.service.user

import com.gms.backend.domain.application.mapper.user.UserMapper
import com.gms.backend.domain.application.rest.user.UserController
import com.gms.backend.domain.domain.model.user.Actor
import com.gms.backend.domain.domain.repository.user.UserRepository
import com.gms.backend.domain.domain.service.user.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
) : UserService {

    @Transactional
    override fun createUser(body: UserController.UserPostDTO) {
        val user = userMapper.userPostDTOToUser(body)
        user.actor = Actor().let { it.type = Actor.ActorType.USER; it.status = Actor.ActorStatus.ACTIVE; it }
        userRepository.save(user)
    }

    @Transactional(readOnly = true)
    override fun getUsers(): List<UserController.UserTableDTO> {
        return userRepository.findAllProjectedBy()
    }

    @Transactional(readOnly = true)
    override fun getUserById(id: UUID): UserController.UserTableDTO {
        return userRepository.findById(id).orElseThrow().let(userMapper::userToUserTableDTO)
    }

    @Transactional
    override fun updateUser(id: UUID, body: UserController.UserPutDTO) {
        val user = userRepository.findById(id).orElseThrow()
        user.email = body.email
    }

    @Transactional
    override fun deleteUser(id: UUID) {
        val user = userRepository.findById(id).orElseThrow()
        user.actor?.status = Actor.ActorStatus.DELETED
        user.actor?.deactivatedAt = Instant.now()
        userRepository.delete(user)
    }
}