package com.gms.backend.domain.impl.domain.service.member

import com.gms.backend.domain.application.mapper.MemberMapper
import com.gms.backend.domain.application.rest.MemberController
import com.gms.backend.domain.domain.model.member.Member
import com.gms.backend.domain.domain.model.user.Actor
import com.gms.backend.domain.domain.repository.member.MemberRepository
import com.gms.backend.domain.domain.repository.storage.ObjectStorageRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.Member.MemberService
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*

@Service
class MemberServiceImpl(
    private val memberRepository: MemberRepository,
    private val memberMapper: MemberMapper,
    private val actorRepository: ActorRepository,
    private val objectRepository: ObjectStorageRepository
) : MemberService {
    @Transactional
    override fun createMember(body: MemberController.MemberPostDTO): Member {
        val member = memberMapper.memberPostDTOToMember(body)
        member.actor = Actor().let { it.type = Actor.ActorType.EMPLOYEE; it }
        member.createdBy = actorRepository.getReferenceById(body.createdById)
        member.updatedBy = actorRepository.getReferenceById(body.createdById)
        body.profilePictureId?.let {
            member.profilePicture = objectRepository.getReferenceById(it)
        }
        return memberRepository.save(member)
    }

    private fun validate(actorId: UUID, profilePictureId: UUID?) {
        if (!actorRepository.existsById(actorId)) {
            throw EntityNotFoundException("Actor ID $actorId not found.")
        }

        profilePictureId?.let { id ->
            println("DEBUG: Validating Profile Picture ID: $id")
            if (!objectRepository.existsById(id)) {
                throw EntityNotFoundException("Validation Failed: Profile Picture ID $id not found.")
            }
        }
    }

    @Transactional
    override fun getMembers(): List<MemberController.MemberTableDTO> {
        return memberRepository.findAllProjectedBy()
    }

    @Transactional
    override fun getMemberById(id: UUID): Optional<Member> {
        return memberRepository.findById(id)
    }

    @Transactional
    override fun updateMember(id: UUID, body: MemberController.MemberPutDTO) {
        val member = memberRepository.findById(id).orElseThrow {
            NoSuchElementException("Member not found with ID: $id")
        }
        memberMapper.memberPutDTOToMember(body, member)
        member.id = id
        member.updatedBy = actorRepository.getReferenceById(body.updatedById)
        body.profilePictureId?.let {
            member.profilePicture = objectRepository.getReferenceById(it)
        }
    }

    @Transactional
    override fun deleteMember(id: UUID) {
        return memberRepository.deleteById(id)
    }

}