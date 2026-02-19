package com.gms.backend.domain.domain.service.branch

import com.gms.backend.domain.application.rest.branch.PersonnelRoleController
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface PersonnelRoleService {
    fun createPersonnelRole(body: PersonnelRoleController.PersonnelRolePostDTO): PersonnelRoleController.PersonnelRoleTableDTO
    fun getPersonnelRoles(pageable: Pageable): Page<PersonnelRoleController.PersonnelRoleTableDTO>
    fun getPersonnelRoleById(id: UUID): PersonnelRoleController.PersonnelRoleTableDTO
    fun updatePersonnelRole(id: UUID, body: PersonnelRoleController.PersonnelRolePutDTO): PersonnelRoleController.PersonnelRoleTableDTO
    fun deletePersonnelRole(id: UUID)
}