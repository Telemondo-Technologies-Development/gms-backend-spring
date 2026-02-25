package com.gms.backend.domain.domain.repository.member

import com.gms.backend.domain.domain.model.member.MemberProgressHistory
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MemberProgressHistoryRepository : JpaRepository<MemberProgressHistory, UUID>{
//    fun findAllProjectedBy(pageable: Pageable): Page<MemberProgressHistoryController.MemberProgressHistoryTableDTO>
}
