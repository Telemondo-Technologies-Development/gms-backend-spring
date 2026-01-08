package com.gms.backend.domain.application.mapper

import com.gms.backend.domain.application.rest.UserController
import com.gms.backend.domain.domain.model.user.User
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface UserMapper {
    fun userToUserTableDTO(user: User): UserController.UserTableDTO
    fun userPostDTOToUser(userDTO: UserController.UserPostDTO): User
}