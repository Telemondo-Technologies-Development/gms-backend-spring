package com.gms.backend.domain.domain.repository.asset

import com.gms.backend.domain.domain.model.asset.Supply
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SupplyRepository : JpaRepository<Supply, UUID>