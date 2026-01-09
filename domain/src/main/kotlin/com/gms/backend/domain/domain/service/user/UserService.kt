package com.gms.backend.domain.domain.service.user

import com.gms.backend.domain.application.rest.user.UserController
import java.util.*

interface UserService {
    fun createUser(body: UserController.UserPostDTO)
    fun getUsers(): List<UserController.UserTableDTO>
    fun getUserById(id: UUID): UserController.UserTableDTO
    fun updateUser(id: UUID, body: UserController.UserPutDTO)
    fun deleteUser(id: UUID)
}