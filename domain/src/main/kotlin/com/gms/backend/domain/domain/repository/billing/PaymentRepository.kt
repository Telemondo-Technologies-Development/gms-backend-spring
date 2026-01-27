package com.gms.backend.domain.domain.repository.billing

import com.gms.backend.domain.application.rest.billing.PaymentController
import com.gms.backend.domain.domain.model.billing.Payment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface PaymentRepository : JpaRepository<Payment, UUID>{
    fun findAllProjectedBy(pageable: Pageable): Page<PaymentController.PaymentTableDTO>
}
