package com.gms.backend.domain.domain.service.user

import com.gms.backend.domain.application.rest.UserController
import com.gms.backend.domain.domain.model.user.User
import java.util.Optional
import java.util.UUID

interface UserService {
    fun createUser(body: UserController.UserPostDTO): User
    fun getUsers(): List<UserController.UserTableDTO>
    fun getUserById(id: UUID): Optional<User>
    fun updateUser(id: UUID, body: UserController.UserPutDTO)
    fun deleteUser(id: UUID)
}