package com.gms.backend.domain.application.mapper.billing

import com.gms.backend.domain.application.rest.billing.PaymentController
import com.gms.backend.domain.domain.model.billing.Payment
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface PaymentMapper {
    fun paymentToPaymentTableDTO(payment: Payment): PaymentController.PaymentTableDTO
    fun paymentPostDTOToPayment(dto: PaymentController.PaymentPostDTO): Payment
    fun paymentPutDTOToPayment(dto: PaymentController.PaymentPutDTO, @MappingTarget payment: Payment): Payment
}