package com.gms.backend.domain.domain.repository.user

import com.gms.backend.domain.application.rest.user.RoleController
import com.gms.backend.domain.domain.model.user.Role
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface RoleRepository : JpaRepository<Role, UUID> {
    fun findAllByUserRolesId(id: UUID): List<Role>
    fun findAllProjectedBy(pageable: Pageable): Page<RoleController.RoleTableDTO>

    @Query("""
        SELECT DISTINCT p.name 
        FROM Role r 
        JOIN r.permissions p 
        WHERE r.name IN :roleNames
        ORDER BY p.name ASC
    """)
    fun findPermissionNamesByRoleNames(roleNames: List<String>): List<String>
}
