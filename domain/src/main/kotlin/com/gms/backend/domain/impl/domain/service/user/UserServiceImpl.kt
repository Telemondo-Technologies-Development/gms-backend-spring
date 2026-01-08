package com.gms.backend.domain.impl.domain.service.user

import com.gms.backend.domain.application.mapper.UserMapper
import com.gms.backend.domain.application.rest.UserController
import com.gms.backend.domain.domain.model.user.Actor
import com.gms.backend.domain.domain.model.user.User
import com.gms.backend.domain.domain.repository.user.UserRepository
import com.gms.backend.domain.domain.service.user.UserService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
) : UserService {

    @Transactional
    override fun createUser(body: UserController.UserPostDTO): User {
        val user = userMapper.userPostDTOToUser(body)
        val actor = Actor().let { it.type = Actor.ActorType.USER; it }
        return userRepository.save(user.apply { this.actor = actor })
    }

    @Transactional
    override fun getUsers(): List<UserController.UserTableDTO> {
        return userRepository.findAllProjectedBy()
    }

    @Transactional
    override fun getUserById(id: UUID): UserController.UserTableDTO? {
        val user = userRepository.findById(id).getOrNull()
        return user?.let {userMapper.userToUserTableDTO(it)}
    }

    @Transactional
    override fun updateUser(id: UUID, body: UserController.UserPutDTO) {
        val user = userRepository.findById(id).orElseThrow()
        user.email = body.email
    }

    @Transactional
    override fun deleteUser(id: UUID) {
        return userRepository.deleteById(id)
    }
}