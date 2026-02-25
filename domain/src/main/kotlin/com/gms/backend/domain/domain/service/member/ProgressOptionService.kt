package com.gms.backend.domain.domain.service.member

import com.gms.backend.domain.application.rest.member.ProgressOptionController
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface ProgressOptionService {
    fun createProgressOption(body: ProgressOptionController.ProgressOptionPostDTO): ProgressOptionController.ProgressOptionTableDTO
    fun getProgressOptions(pageable: Pageable): Page<ProgressOptionController.ProgressOptionTableDTO>
    fun getProgressOptionById(id: UUID): ProgressOptionController.ProgressOptionTableDTO
    fun updateProgressOption(id: UUID, body: ProgressOptionController.ProgressOptionPutDTO): ProgressOptionController.ProgressOptionTableDTO
    fun deleteProgressOption(id: UUID)
}