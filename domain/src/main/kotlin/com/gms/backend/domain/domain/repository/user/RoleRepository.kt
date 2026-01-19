package com.gms.backend.domain.domain.repository.user

import com.gms.backend.domain.application.rest.user.RoleController
import com.gms.backend.domain.domain.model.user.Role
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RoleRepository : JpaRepository<Role, UUID> {
    fun findAllByUserRoleUsersId(id: UUID): List<Role>
    fun findAllProjectedBy(pageable: Pageable): Page<RoleController.RoleTableDTO>
}
