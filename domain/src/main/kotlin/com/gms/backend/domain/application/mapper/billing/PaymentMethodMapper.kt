package com.gms.backend.domain.application.mapper.billing

import com.gms.backend.domain.application.rest.billing.PaymentMethodController
import com.gms.backend.domain.domain.model.billing.PaymentMethod
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface PaymentMethodMapper {
    fun paymentMethodToPaymentMethodTableDTO(paymentMethod: PaymentMethod): PaymentMethodController.PaymentMethodTableDTO
    fun paymentMethodPostDTOToPaymentMethod(dto: PaymentMethodController.PaymentMethodPostDTO): PaymentMethod
    fun paymentMethodPutDTOToPaymentMethod(dto: PaymentMethodController.PaymentMethodPutDTO, @MappingTarget paymentMethod: PaymentMethod): PaymentMethod
}