package com.gms.backend.domain.impl.domain.service.member

import com.gms.backend.domain.application.mapper.MemberMapper
import com.gms.backend.domain.application.response.ApiErrorType
import com.gms.backend.domain.application.response.DomainException
import com.gms.backend.domain.application.rest.member.MemberController
import com.gms.backend.domain.domain.model.user.Actor
import com.gms.backend.domain.domain.repository.member.MemberRepository
import com.gms.backend.domain.domain.repository.storage.ObjectStorageRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.Member.MemberService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Service
class MemberServiceImpl(
    private val memberRepository: MemberRepository,
    private val memberMapper: MemberMapper,
    private val actorRepository: ActorRepository,
    private val objectRepository: ObjectStorageRepository
) : MemberService {
    @Transactional
    override fun createMember(body: MemberController.MemberPostDTO) {
        val member = memberMapper.memberPostDTOToMember(body)
        member.actor = Actor().let { it.type = Actor.ActorType.EMPLOYEE; it.status = Actor.ActorStatus.ACTIVE; it }
        member.createdBy = actorRepository.getReferenceById(body.createdById)
        member.updatedBy = actorRepository.getReferenceById(body.createdById)
        body.profilePictureId?.let {
            member.profilePicture = objectRepository.getReferenceById(it)
        }
        memberRepository.save(member)
    }

    @Transactional(readOnly = true)
    override fun getMembers(): List<MemberController.MemberTableDTO> {
        return memberRepository.findAllProjectedBy()
    }

    // Not sure if I should throw instead of a returning null
    @Transactional(readOnly = true)
    override fun getMemberById(id: UUID): MemberController.MemberTableDTO {
        return memberRepository.findById(id).orElseThrow().let(memberMapper::memberToMemberTableDTO)
    }

    @Transactional
    override fun updateMember(id: UUID, body: MemberController.MemberPutDTO) {
        val member = memberRepository.findById(id).orElseThrow()
        memberMapper.memberPutDTOToMember(body, member)
        member.id = id
        member.updatedBy = actorRepository.getReferenceById(body.updatedById)
        body.profilePictureId?.let {
            member.profilePicture = objectRepository.getReferenceById(it)
        }
    }

    @Transactional
    override fun deleteMember(id: UUID) {
        val member = memberRepository.findById(id).orElseThrow()
        member.actor?.status = Actor.ActorStatus.DELETED
        member.actor?.deactivatedAt = Instant.now()
        memberRepository.delete(member)
    }

}