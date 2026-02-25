package com.gms.backend.domain.domain.service.billing

import com.gms.backend.domain.application.rest.billing.PaymentController
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface PaymentService {
    fun createPayment(body: PaymentController.PaymentPostDTO): PaymentController.PaymentTableDTO
    fun getPayments(pageable: Pageable): Page<PaymentController.PaymentTableDTO>
    fun getPaymentById(id: UUID): PaymentController.PaymentTableDTO
    fun updatePayment(id: UUID, body: PaymentController.PaymentPutDTO): PaymentController.PaymentTableDTO
    fun deletePayment(id: UUID)
}