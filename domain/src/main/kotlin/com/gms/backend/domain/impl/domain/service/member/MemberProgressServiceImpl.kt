package com.gms.backend.domain.impl.domain.service.member

import com.gms.backend.domain.application.mapper.member.MemberProgressMapper
import com.gms.backend.domain.application.rest.member.MemberProgressController
import com.gms.backend.domain.domain.model.member.MemberProgressHistory
import com.gms.backend.domain.domain.repository.branch.BranchRepository
import com.gms.backend.domain.domain.repository.member.MemberProgressHistoryRepository
import com.gms.backend.domain.domain.repository.member.MemberProgressRepository
import com.gms.backend.domain.domain.repository.member.ProgressOptionRepository
import com.gms.backend.domain.domain.repository.member.ProgressRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.member.MemberProgressService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Service
@PreAuthorize("denyAll()")
class MemberProgressServiceImpl(
    private val memberProgressRepository: MemberProgressRepository,
    private val memberProgressMapper: MemberProgressMapper,
    private val progressOptionRepository: ProgressOptionRepository,
    private val progressRepository: ProgressRepository,
    private val memberProgressHistoryRepository: MemberProgressHistoryRepository,
    private val branchRepository: BranchRepository,
    private val actorRepository: ActorRepository,
) : MemberProgressService {
    @Transactional
    @PreAuthorize("hasAuthority('memberProgress_create')")
    override fun createMemberProgress(body: MemberProgressController.MemberProgressPostDTO): MemberProgressController.MemberProgressTableDTO {
        val memberProgress = memberProgressMapper.memberProgressPostDTOToMemberProgress(body).apply {
            actor = actorRepository.getReferenceById(body.actorId)
            branch = branchRepository.getReferenceById(body.branchId)
            progressOption = progressOptionRepository.getReferenceById(body.progressOptionId)
            progress = progressRepository.getReferenceById(body.progressId)
            createdBy = actorRepository.getReferenceById(body.createdById)
            updatedBy = actorRepository.getReferenceById(body.createdById)
        }

        val saved = memberProgressRepository.saveAndFlush(memberProgress)
        return memberProgressMapper.memberProgressToMemberProgressTableDTO(saved)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('memberProgress_read')")
    override fun getMemberProgress(pageable: Pageable): Page<MemberProgressController.MemberProgressTableDTO> {
        val memberProgress =  memberProgressRepository.findAllProjectedBy(pageable)

        val memberProgressIds = memberProgress.content.map { it.id }
        if (memberProgressIds.isEmpty()) return Page.empty(pageable)

        val historyByMemberId: Map<UUID, List<MemberProgressController.MemberProgressHistoryBrief>> = memberProgressRepository.findAllHistory(memberProgressIds)
            .groupBy({ it.memberProgressId }, {memberProgressMapper.memberHistoryToMemberHistoryBriefDTO(it)})

        // 3. Map to DTO using O(1) lookups
        return memberProgress.map { progress ->
            progress.apply {
                progressHistory = historyByMemberId.getOrDefault(progress.id, emptyList())
            }
        }
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('memberProgress_read') and hasAuthority('permission_read')")
    override fun getMemberProgressById(id: UUID): MemberProgressController.MemberProgressTableDTO {
        return memberProgressRepository.findById(id).orElseThrow()
            .let(memberProgressMapper::memberProgressToMemberProgressTableDTO)
    }

    @Transactional
    @PreAuthorize("hasAuthority('memberProgress_update')")
    override fun updateMemberProgress(
        id: UUID,
        body: MemberProgressController.MemberProgressPutDTO
    ): MemberProgressController.MemberProgressTableDTO {
        val memberProgress = memberProgressRepository.findById(id).orElseThrow()

        // Creates an entry for the previous state
        if (memberProgress.progressId != body.progressId){
            val history = MemberProgressHistory().apply {
                this.memberProgress = memberProgress
                progress = progressRepository.getReferenceById(memberProgress.progressId!!)
                changedAt = Instant.now()
            }
            memberProgressHistoryRepository.save(history)
        }

        memberProgress.apply {
            memberProgressMapper.memberProgressPutDTOToMemberProgress(body, this)
            progress = progressRepository.getReferenceById(body.progressId)
            updatedBy = actorRepository.getReferenceById(body.updatedById)
        }

        memberProgressRepository.saveAndFlush(memberProgress)
        return memberProgressMapper.memberProgressToMemberProgressTableDTO(memberProgress)
    }

    @Transactional
    @PreAuthorize("hasAuthority('memberProgress_delete')")
    override fun deleteMemberProgress(id: UUID) {
        val memberProgress = memberProgressRepository.findById(id).orElseThrow()
        return memberProgressRepository.delete(memberProgress)
    }

}