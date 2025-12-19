package com.gms.backend.domain.domain.repository.member

import com.gms.backend.domain.domain.model.member.MemberProgress
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MemberProgressRepository : JpaRepository<MemberProgress, UUID>
