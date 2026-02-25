package com.gms.backend.domain.domain.repository.member

import com.gms.backend.domain.application.rest.member.ProgressOptionController
import com.gms.backend.domain.domain.model.member.ProgressOption
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ProgressOptionRepository : JpaRepository<ProgressOption, UUID>{
    fun findAllProjectedBy(pageable: Pageable): Page<ProgressOptionController.ProgressOptionTableDTO>
}
