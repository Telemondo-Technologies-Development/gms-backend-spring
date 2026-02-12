package com.gms.backend.domain.domain.service.billing

import com.gms.backend.domain.application.rest.billing.PaymentMethodController
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface PaymentMethodService {
    fun createPaymentMethod(body: PaymentMethodController.PaymentMethodPostDTO): PaymentMethodController.PaymentMethodTableDTO
    fun getPaymentMethods(pageable: Pageable): Page<PaymentMethodController.PaymentMethodTableDTO>
    fun getPaymentMethodById(id: UUID): PaymentMethodController.PaymentMethodTableDTO
    fun updatePaymentMethod(id: UUID, body: PaymentMethodController.PaymentMethodPutDTO): PaymentMethodController.PaymentMethodTableDTO
    fun deletePaymentMethod(id: UUID)
}