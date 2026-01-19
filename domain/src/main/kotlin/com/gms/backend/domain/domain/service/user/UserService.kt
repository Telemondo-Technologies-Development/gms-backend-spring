package com.gms.backend.domain.domain.service.user

import com.gms.backend.domain.application.rest.user.UserController
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface UserService {
    fun createUser(body: UserController.UserPostDTO): UserController.UserTableDTO
    fun getUsers(pageable: Pageable): Page<UserController.UserTableDTO>
    fun getUserById(id: UUID): UserController.UserTableDTO
    fun updateUser(id: UUID, body: UserController.UserPutDTO): UserController.UserTableDTO
    fun deleteUser(id: UUID)
}