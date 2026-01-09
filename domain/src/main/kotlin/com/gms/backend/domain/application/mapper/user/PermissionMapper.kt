package com.gms.backend.domain.application.mapper.user

import com.gms.backend.domain.domain.model.user.Permission
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface PermissionMapper {
    fun namesToPermission(names: MutableSet<String>): List<Permission>

    @Mapping(target = "name", source = "name")
    fun stringToPermission(name: String): Permission
}