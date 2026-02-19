package com.gms.backend.domain.impl.domain.service.branch

import com.gms.backend.domain.application.mapper.branch.PersonnelRoleMapper
import com.gms.backend.domain.application.rest.branch.PersonnelRoleController
import com.gms.backend.domain.domain.repository.branch.PersonnelRoleRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.branch.PersonnelRoleService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@PreAuthorize("denyAll()")
class PersonnelRoleServiceImpl(
    private val personnelRoleRepository: PersonnelRoleRepository,
    private val personnelRoleMapper: PersonnelRoleMapper,
    private val actorRepository: ActorRepository,
) : PersonnelRoleService {
    @Transactional
    @PreAuthorize("hasAuthority('personnelRole_create')")
    override fun createPersonnelRole(body: PersonnelRoleController.PersonnelRolePostDTO): PersonnelRoleController.PersonnelRoleTableDTO {
        val personnelRole = personnelRoleMapper.personnelRolePostDTOToPersonnelRole(body).apply {
            createdBy = actorRepository.getReferenceById(body.createdById)
            updatedBy = actorRepository.getReferenceById(body.createdById)
        }

        val saved = personnelRoleRepository.saveAndFlush(personnelRole)
        return personnelRoleMapper.personnelRoleToPersonnelRoleTableDTO(saved)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('personnelRole_read')")
    override fun getPersonnelRoles(pageable: Pageable): Page<PersonnelRoleController.PersonnelRoleTableDTO> {
        return personnelRoleRepository.findAllProjectedBy(pageable)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('personnelRole_read') and hasAuthority('permission_read')")
    override fun getPersonnelRoleById(id: UUID): PersonnelRoleController.PersonnelRoleTableDTO {
        return personnelRoleRepository.findById(id).orElseThrow().let(personnelRoleMapper::personnelRoleToPersonnelRoleTableDTO)
    }

    @Transactional
    @PreAuthorize("hasAuthority('personnelRole_update')")
    override fun updatePersonnelRole(id: UUID, body: PersonnelRoleController.PersonnelRolePutDTO): PersonnelRoleController.PersonnelRoleTableDTO {
        val personnelRole = personnelRoleRepository.findById(id).orElseThrow().apply {
            personnelRoleMapper.personnelRolePutDTOToPersonnelRole(body, this)
            updatedBy = actorRepository.getReferenceById(body.updatedById)
        }

        personnelRoleRepository.saveAndFlush(personnelRole)
        return personnelRoleMapper.personnelRoleToPersonnelRoleTableDTO(personnelRole)
    }

    @Transactional
    @PreAuthorize("hasAuthority('personnelRole_delete')")
    override fun deletePersonnelRole(id: UUID) {
        val personnelRole = personnelRoleRepository.findById(id).orElseThrow()
        return personnelRoleRepository.delete(personnelRole)
    }

}