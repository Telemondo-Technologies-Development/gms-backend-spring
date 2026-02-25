package com.gms.backend.domain.impl.domain.service.member

import com.gms.backend.domain.application.mapper.member.ProgressOptionMapper
import com.gms.backend.domain.application.rest.member.ProgressOptionController
import com.gms.backend.domain.domain.repository.member.ProgressOptionRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.member.ProgressOptionService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@PreAuthorize("denyAll()")
class ProgressOptionServiceImpl(
    private val progressOptionRepository: ProgressOptionRepository,
    private val progressOptionMapper: ProgressOptionMapper,
    private val actorRepository: ActorRepository,
) : ProgressOptionService {
    @Transactional
    @PreAuthorize("hasAuthority('progressOption_create')")
    override fun createProgressOption(body: ProgressOptionController.ProgressOptionPostDTO): ProgressOptionController.ProgressOptionTableDTO {
        val progressOption = progressOptionMapper.progressOptionPostDTOToProgressOption(body).apply {
            createdBy = actorRepository.getReferenceById(body.createdById)
            updatedBy = actorRepository.getReferenceById(body.createdById)
        }

        val saved = progressOptionRepository.saveAndFlush(progressOption)
        return progressOptionMapper.progressOptionToProgressOptionTableDTO(saved)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('progressOption_read')")
    override fun getProgressOptions(pageable: Pageable): Page<ProgressOptionController.ProgressOptionTableDTO> {
        return progressOptionRepository.findAllProjectedBy(pageable)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('progressOption_read') and hasAuthority('permission_read')")
    override fun getProgressOptionById(id: UUID): ProgressOptionController.ProgressOptionTableDTO {
        return progressOptionRepository.findById(id).orElseThrow().let(progressOptionMapper::progressOptionToProgressOptionTableDTO)
    }

    @Transactional
    @PreAuthorize("hasAuthority('progressOption_update')")
    override fun updateProgressOption(id: UUID, body: ProgressOptionController.ProgressOptionPutDTO): ProgressOptionController.ProgressOptionTableDTO {
        val progressOption = progressOptionRepository.findById(id).orElseThrow().apply {
            progressOptionMapper.progressOptionPutDTOToProgressOption(body, this)
            updatedBy = actorRepository.getReferenceById(body.updatedById)
        }

        progressOptionRepository.saveAndFlush(progressOption)
        return progressOptionMapper.progressOptionToProgressOptionTableDTO(progressOption)
    }

    @Transactional
    @PreAuthorize("hasAuthority('progressOption_delete')")
    override fun deleteProgressOption(id: UUID) {
        val progressOption = progressOptionRepository.findById(id).orElseThrow()
        return progressOptionRepository.delete(progressOption)
    }

}