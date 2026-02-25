package com.gms.backend.domain.impl.domain.service.member

import com.gms.backend.domain.application.mapper.member.ProgressMapper
import com.gms.backend.domain.application.rest.member.ProgressController
import com.gms.backend.domain.domain.repository.member.ProgressOptionRepository
import com.gms.backend.domain.domain.repository.member.ProgressRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.member.ProgressService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@PreAuthorize("denyAll()")
class ProgressServiceImpl(
    private val progressRepository: ProgressRepository,
    private val progressMapper: ProgressMapper,
    private val progressOptionRepository: ProgressOptionRepository,
    private val actorRepository: ActorRepository,
) : ProgressService {
    @Transactional
    @PreAuthorize("hasAuthority('progress_create')")
    override fun createProgress(body: ProgressController.ProgressPostDTO): ProgressController.ProgressTableDTO {
        val progress = progressMapper.progressPostDTOToProgress(body).apply {
            progressOption = progressOptionRepository.getReferenceById(body.progressOptionId)
            createdBy = actorRepository.getReferenceById(body.createdById)
            updatedBy = actorRepository.getReferenceById(body.createdById)
        }

        val saved = progressRepository.saveAndFlush(progress)
        return progressMapper.progressToProgressTableDTO(saved)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('progress_read')")
    override fun getProgress(pageable: Pageable): Page<ProgressController.ProgressTableDTO> {
        return progressRepository.findAllProjectedBy(pageable)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('progress_read') and hasAuthority('permission_read')")
    override fun getProgressById(id: UUID): ProgressController.ProgressTableDTO {
        return progressRepository.findById(id).orElseThrow().let(progressMapper::progressToProgressTableDTO)
    }

    @Transactional
    @PreAuthorize("hasAuthority('progress_update')")
    override fun updateProgress(id: UUID, body: ProgressController.ProgressPutDTO): ProgressController.ProgressTableDTO {
        val progress = progressRepository.findById(id).orElseThrow().apply {
            progressMapper.progressPutDTOToProgress(body, this)
            progressOption = progressOptionRepository.getReferenceById(body.progressOptionId)
            updatedBy = actorRepository.getReferenceById(body.updatedById)
        }

        progressRepository.saveAndFlush(progress)
        return progressMapper.progressToProgressTableDTO(progress)
    }

    @Transactional
    @PreAuthorize("hasAuthority('progress_delete')")
    override fun deleteProgress(id: UUID) {
        val progress = progressRepository.findById(id).orElseThrow()
        return progressRepository.delete(progress)
    }

}