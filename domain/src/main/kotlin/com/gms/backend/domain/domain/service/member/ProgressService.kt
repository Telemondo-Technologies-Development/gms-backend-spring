package com.gms.backend.domain.domain.service.member

import com.gms.backend.domain.application.rest.member.ProgressController
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface ProgressService {
    fun createProgress(body: ProgressController.ProgressPostDTO): ProgressController.ProgressTableDTO
    fun getProgress(pageable: Pageable): Page<ProgressController.ProgressTableDTO>
    fun getProgressById(id: UUID): ProgressController.ProgressTableDTO
    fun updateProgress(id: UUID, body: ProgressController.ProgressPutDTO): ProgressController.ProgressTableDTO
    fun deleteProgress(id: UUID)
}