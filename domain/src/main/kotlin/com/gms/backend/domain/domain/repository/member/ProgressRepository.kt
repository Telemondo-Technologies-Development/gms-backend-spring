package com.gms.backend.domain.domain.repository.member

import com.gms.backend.domain.domain.model.member.Progress
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ProgressRepository : JpaRepository<Progress, UUID>
