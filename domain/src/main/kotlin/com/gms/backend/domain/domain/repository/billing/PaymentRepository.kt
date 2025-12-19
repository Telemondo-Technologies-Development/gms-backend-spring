package com.gms.backend.domain.domain.repository.billing

import com.gms.backend.domain.domain.model.billing.Payment
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface PaymentRepository : JpaRepository<Payment, UUID>
