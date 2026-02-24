package com.gms.backend.domain.domain.repository.branch

import com.gms.backend.domain.application.rest.branch.PersonnelRoleController
import com.gms.backend.domain.domain.model.branch.PersonnelRole
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface PersonnelRoleRepository : JpaRepository<PersonnelRole, UUID> {
    fun findAllProjectedBy(pageable: Pageable): Page<PersonnelRoleController.PersonnelRoleTableDTO>
}