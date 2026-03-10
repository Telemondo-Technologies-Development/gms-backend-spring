package com.gms.backend.domain.impl.domain.service.member

import com.gms.backend.domain.application.mapper.member.MemberMapper
import com.gms.backend.domain.application.rest.member.MemberController
import com.gms.backend.domain.domain.model.member.Member
import com.gms.backend.domain.domain.model.user.Actor
import com.gms.backend.domain.domain.repository.member.MemberRepository
import com.gms.backend.domain.domain.repository.storage.ObjectStorageRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.member.MemberService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Service
@PreAuthorize("denyAll()")
class MemberServiceImpl(
    private val memberRepository: MemberRepository,
    private val memberMapper: MemberMapper,
    private val actorRepository: ActorRepository,
    private val objectRepository: ObjectStorageRepository
) : MemberService {
    @Transactional
    @PreAuthorize("hasAuthority('member_create')")
    override fun createMember(body: MemberController.MemberPostDTO): MemberController.MemberTableDTO {
        val member = memberMapper.memberPostDTOToMember(body).apply {
            actor = Actor().apply {
                type = Actor.ActorType.MEMBER
                status = Actor.ActorStatus.ACTIVE
            }
            createdBy = actorRepository.getReferenceById(body.createdById)
            updatedBy = actorRepository.getReferenceById(body.createdById)
            profilePicture = body.profilePictureId?.let { objectRepository.getReferenceById(it) }
        }

        val saved = memberRepository.saveAndFlush(member)
        return memberMapper.memberToMemberTableDTO(saved)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('member_read')")
    override fun getMembers(
        pageable: Pageable,
        fullName: String?,
        status: Member.MemberStatus?,
        dateFrom: Instant?,
        dateTo: Instant?
    ): Page<MemberController.MemberTableDTO> {
        return memberRepository.findAllProjectedBy(pageable, fullName, status, dateFrom, dateTo)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('member_read')")
    override fun getMemberById(
        id: UUID,
        fullName: String?,
        status: Member.MemberStatus?,
        dateFrom: Instant?,
        dateTo: Instant?
    ): MemberController.MemberTableDTO {
        val member = memberRepository.findProjectedBy(id, fullName, status, dateFrom, dateTo).orElseThrow()
        return member
    }

    @Transactional
    @PreAuthorize("hasAuthority('member_update')")
    override fun updateMember(id: UUID, body: MemberController.MemberPutDTO): MemberController.MemberTableDTO {
        val member = memberRepository.findById(id).orElseThrow().apply {
            memberMapper.memberPutDTOToMember(body, this)
            this.id = id
            updatedBy = actorRepository.getReferenceById(body.updatedById)
            profilePicture = body.profilePictureId?.let { objectRepository.getReferenceById(it) }
        }

        memberRepository.saveAndFlush(member)
        return memberMapper.memberToMemberTableDTO(member)
    }

    @Transactional
    @PreAuthorize("hasAuthority('member_delete')")
    override fun deleteMember(id: UUID) {
        val member = memberRepository.findById(id).orElseThrow().apply {
            actor.status = Actor.ActorStatus.DELETED
            actor.deactivatedAt = Instant.now()
        }

        memberRepository.delete(member)
    }

}