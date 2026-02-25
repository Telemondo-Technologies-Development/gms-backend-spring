package com.gms.backend.domain.domain.service.branch

import com.gms.backend.domain.application.rest.branch.BranchPersonnelController
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface BranchPersonnelService {
    fun createBranchPersonnel(body: BranchPersonnelController.BranchPersonnelPostDTO): BranchPersonnelController.BranchPersonnelTableDTO
    fun updateBranchPersonnel(id: UUID, body: BranchPersonnelController.BranchPersonnelPutDTO): BranchPersonnelController.BranchPersonnelTableDTO
    fun getBranchPersonnel(pageable: Pageable): Page<BranchPersonnelController.BranchPersonnelTableDTO>
    fun getBranchPersonnelById(id: UUID): BranchPersonnelController.BranchPersonnelTableDTO
    fun deleteBranchPersonnel(id: UUID)
}
