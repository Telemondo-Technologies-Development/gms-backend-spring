package com.gms.backend.domain.domain.repository.billing

import com.gms.backend.domain.application.rest.billing.PaymentMethodController
import com.gms.backend.domain.domain.model.billing.PaymentMethod
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface PaymentMethodRepository : JpaRepository<PaymentMethod, UUID>{
    fun findAllProjectedBy(pageable: Pageable): Page<PaymentMethodController.PaymentMethodTableDTO>
}
