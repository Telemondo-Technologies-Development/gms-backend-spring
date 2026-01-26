package com.gms.backend.domain.impl.domain.service.billing

import com.gms.backend.domain.application.mapper.billing.PaymentMethodMapper
import com.gms.backend.domain.application.rest.billing.PaymentMethodController
import com.gms.backend.domain.domain.repository.billing.PaymentMethodRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.billing.PaymentMethodService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@PreAuthorize("denyAll()")
class PaymentMethodServiceImpl(
    private val paymentMethodRepository: PaymentMethodRepository,
    private val paymentMethodMapper: PaymentMethodMapper,
    private val actorRepository: ActorRepository,
) : PaymentMethodService {
    @Transactional
    @PreAuthorize("hasAuthority('paymentMethod_create')")
    override fun createPaymentMethod(body: PaymentMethodController.PaymentMethodPostDTO): PaymentMethodController.PaymentMethodTableDTO {
        val paymentMethod = paymentMethodMapper.paymentMethodPostDTOToPaymentMethod(body).apply {
            createdBy = actorRepository.getReferenceById(body.createdById)
            updatedBy = actorRepository.getReferenceById(body.createdById)
        }

        val saved = paymentMethodRepository.saveAndFlush(paymentMethod)
        return paymentMethodMapper.paymentMethodToPaymentMethodTableDTO(saved)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('paymentMethod_read')")
    override fun getPaymentMethods(pageable: Pageable): Page<PaymentMethodController.PaymentMethodTableDTO> {
        return paymentMethodRepository.findAllProjectedBy(pageable)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('paymentMethod_read')")
    override fun getPaymentMethodById(id: UUID): PaymentMethodController.PaymentMethodTableDTO {
        val entity = paymentMethodRepository.findById(id).orElseThrow()
        return paymentMethodMapper.paymentMethodToPaymentMethodTableDTO(entity)
    }

    @Transactional
    @PreAuthorize("hasAuthority('paymentMethod_update')")
    override fun updatePaymentMethod(
        id: UUID,
        body: PaymentMethodController.PaymentMethodPutDTO
    ): PaymentMethodController.PaymentMethodTableDTO {
        val paymentMethod = paymentMethodRepository.findById(id).orElseThrow().apply {
            paymentMethodMapper.paymentMethodPutDTOToPaymentMethod(body, this)
            updatedBy = actorRepository.getReferenceById(body.updatedById)
        }

        paymentMethodRepository.saveAndFlush(paymentMethod)
        return paymentMethodMapper.paymentMethodToPaymentMethodTableDTO(paymentMethod)
    }

    @Transactional
    @PreAuthorize("hasAuthority('paymentMethod_delete')")
    override fun deletePaymentMethod(id: UUID) {
        return paymentMethodRepository.deleteById(id)
    }
}