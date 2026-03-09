package com.gms.backend.domain.domain.service.billing

import com.gms.backend.domain.application.rest.billing.PaymentController
import com.gms.backend.domain.domain.model.billing.Payment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.Instant
import java.util.*

interface PaymentService {
    fun createPayment(body: PaymentController.PaymentPostDTO): PaymentController.PaymentTableDTO
    fun getPayments(
        pageable: Pageable,
        status: Payment.PaymentStatus?,
        paymentMethodName: String?,
        dateFrom: Instant?,
        dateTo: Instant?
    ): Page<PaymentController.PaymentTableDTO>
    fun getPaymentById(
        id: UUID,
        status: Payment.PaymentStatus?,
        paymentMethodName: String?,
        dateFrom: Instant?,
        dateTo: Instant?
    ): PaymentController.PaymentTableDTO
    fun updatePayment(id: UUID, body: PaymentController.PaymentPutDTO): PaymentController.PaymentTableDTO
    fun deletePayment(id: UUID)
}