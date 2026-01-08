package com.gms.backend.domain.domain.repository.branch

import com.gms.backend.domain.domain.model.branch.Branch
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface BranchRepository : JpaRepository<Branch, UUID>
