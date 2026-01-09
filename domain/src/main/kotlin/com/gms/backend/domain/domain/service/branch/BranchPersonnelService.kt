package com.gms.backend.domain.domain.service.branch

import com.gms.backend.domain.application.rest.BranchPersonnelController
import com.gms.backend.domain.domain.model.branch.BranchPersonnel
import java.util.UUID

interface BranchPersonnelService {
    fun createBranchPersonnel(body: BranchPersonnelController.BranchPersonnelPostDTO): BranchPersonnelController.BranchPersonnelTableDTO
    fun updateBranchPersonnel(id: UUID, body: BranchPersonnelController.BranchPersonnelPutDTO): BranchPersonnelController.BranchPersonnelTableDTO
    fun getBranchPersonnel(): List<BranchPersonnelController.BranchPersonnelTableDTO>
    fun getBranchPersonnelById(id: UUID): BranchPersonnelController.BranchPersonnelTableDTO
    fun deleteBranchPersonnel(id: UUID)
}
