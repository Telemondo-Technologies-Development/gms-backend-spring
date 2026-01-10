package com.gms.backend.domain.impl.domain.service.member

import com.gms.backend.domain.application.mapper.MemberMapper
import com.gms.backend.domain.application.rest.member.MemberController
import com.gms.backend.domain.domain.model.user.Actor
import com.gms.backend.domain.domain.repository.member.MemberRepository
import com.gms.backend.domain.domain.repository.storage.ObjectStorageRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.member.MemberService
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

        val saved = memberRepository.save(member)
        return memberMapper.memberToMemberTableDTO(saved)
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
    override fun updateMember(id: UUID, body: MemberController.MemberPutDTO): MemberController.MemberTableDTO {
        val member = memberRepository.findById(id).orElseThrow().apply {
            memberMapper.memberPutDTOToMember(body, this)
            this.id = id
            updatedBy = actorRepository.getReferenceById(body.updatedById)
            profilePicture = body.profilePictureId?.let { objectRepository.getReferenceById(it) }
        }

        memberRepository.save(member)
        return memberMapper.memberToMemberTableDTO(member)
    }

    @Transactional
    override fun deleteMember(id: UUID) {
        val member = memberRepository.findById(id).orElseThrow().apply {
            actor.status = Actor.ActorStatus.DELETED
            actor.deactivatedAt = Instant.now()
        }

        memberRepository.delete(member)
    }

}