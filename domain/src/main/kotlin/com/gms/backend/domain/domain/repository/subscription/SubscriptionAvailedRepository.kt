package com.gms.backend.domain.domain.repository.subscription

import com.gms.backend.domain.application.rest.subscription.SubscriptionAvailedController
import com.gms.backend.domain.domain.model.subscription.BillingCycle
import com.gms.backend.domain.domain.model.subscription.SubscriptionAvailed
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.math.BigDecimal
import java.util.*

interface SubscriptionAvailedRepository : JpaRepository<SubscriptionAvailed, UUID> {
    fun findAllProjectedBy(pageable: Pageable): Page<SubscriptionAvailedController.SubscriptionAvailedTableDTO>

//    @Query(
//        """
//        SELECT new com.gms.backend.domain.application.rest.subscription.SubscriptionAvailedController${'$'}SubscriptionAvailedTableDTO(
//        s.id,
//        s.name,
//        s.amount,
//        s.intervals,
//        s.intervalCount,
//        s.gracePeriodDays
//    )FROM SubscriptionAvailed s
//        WHERE s.name = :name
//        AND s.amount = :amount
//        AND s.intervals = :intervals
//        AND s.intervalCount = :intervalCount
//        AND s.gracePeriodDays = :gracePeriodDays
//        AND s.subscriptionId = :subscriptionId
//    """
//    )

    @Query(
        """
        SELECT s.id FROM SubscriptionAvailed s 
        WHERE s.name = :name 
        AND s.amount = :amount 
        AND s.intervals = :intervals 
        AND s.intervalCount = :intervalCount 
        AND s.gracePeriodDays = :gracePeriodDays
        AND s.subscriptionId = :subscriptionId
    """
    )
    fun findBySubscriptionCriteria(
        @Param("name") name: String,
        @Param("amount") amount: BigDecimal,
        @Param("intervals") intervals: BillingCycle.Interval,
        @Param("intervalCount") intervalCount: Int,
        @Param("gracePeriodDays") gracePeriodDays: Int,
        @Param("subscriptionId") subscriptionId: UUID
    ): UUID?
}
