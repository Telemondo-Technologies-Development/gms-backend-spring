package com.gms.backend.domain.domain.repository.member

import com.gms.backend.domain.domain.model.member.ProgressOption
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ProgressOptionRepository : JpaRepository<ProgressOption, UUID>
