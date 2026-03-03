package com.gms.backend.domain.application.mapper.user

import com.gms.backend.domain.application.rest.user.UserController
import com.gms.backend.domain.domain.model.user.User
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface UserMapper {
    @Mapping(target = "username", source = "email")
    fun userToUserTableDTO(user: User): UserController.UserTableDTO
    fun userRoleToUserRoleBriefDTO(role: UserController.UserRoleWithUserId): UserController.UserRoleBriefDTO
    @Mapping(target = "email", source = "username")
    fun userPostDTOToUser(userDTO: UserController.UserPostDTO): User
    @Mapping(target = "email", source = "username")
    fun userPutDTOToUser(dto: UserController.UserPutDTO, @MappingTarget user: User): User
}