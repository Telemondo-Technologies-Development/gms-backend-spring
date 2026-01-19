package com.gms.backend.domain.impl.domain.service.subscription

import com.gms.backend.domain.application.mapper.subscription.BillingCycleMapper
import com.gms.backend.domain.application.rest.subscription.BillingCycleController
import com.gms.backend.domain.domain.repository.subscription.BillingCycleRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.subscription.BillingCycleService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@PreAuthorize("denyAll()")
class BillingCycleServiceImpl(
    private val billingCycleRepository: BillingCycleRepository,
    private val billingCycleMapper: BillingCycleMapper,
    private val actorRepository: ActorRepository
) : BillingCycleService {
    @Transactional
    @PreAuthorize("hasAuthority('billingCycle_create')")
    override fun createBillingCycle(body: BillingCycleController.BillingCyclePostDTO): BillingCycleController.BillingCycleTableDTO {
        val billingCycle = billingCycleMapper.billingCyclePostDTOToBillingCycle(body).apply {
            createdBy = actorRepository.getReferenceById(body.createdById)
            updatedBy = actorRepository.getReferenceById(body.createdById)
        }

        val saved = billingCycleRepository.saveAndFlush(billingCycle)
        return billingCycleMapper.billingCycleToBillingCycleTableDTO(saved)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('billingCycle_read')")
    override fun getBillingCycles(pageable: Pageable): Page<BillingCycleController.BillingCycleTableDTO> {
        return billingCycleRepository.findAllProjectedBy(pageable)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('billingCycle_read')")
    override fun getBillingCycleById(id: UUID): BillingCycleController.BillingCycleTableDTO {
        return billingCycleRepository.findById(id).orElseThrow()
            .let(billingCycleMapper::billingCycleToBillingCycleTableDTO)
    }

    @Transactional
    @PreAuthorize("hasAuthority('billingCycle_update')")
    override fun updateBillingCycle(
        id: UUID,
        body: BillingCycleController.BillingCyclePutDTO
    ): BillingCycleController.BillingCycleTableDTO {
        val billingCycle = billingCycleRepository.findById(id).orElseThrow().apply {
            billingCycleMapper.billingCyclePutDTOToBillingCycle(body, this)
            this.id = id
            updatedBy = actorRepository.getReferenceById(body.updatedById)
        }

        billingCycleRepository.saveAndFlush(billingCycle)
        return billingCycleMapper.billingCycleToBillingCycleTableDTO(billingCycle)
    }

    @Transactional
    @PreAuthorize("hasAuthority('billingCycle_delete')")
    override fun deleteBillingCycle(id: UUID) {
        val billingCycle = billingCycleRepository.findById(id).orElseThrow()
        billingCycleRepository.delete(billingCycle)
    }

}
