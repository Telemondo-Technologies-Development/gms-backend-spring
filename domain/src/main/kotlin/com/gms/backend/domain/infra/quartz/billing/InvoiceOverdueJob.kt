package com.gms.backend.domain.infra.quartz.billing

import com.gms.backend.domain.application.response.ApiErrorType
import com.gms.backend.domain.application.response.DomainException
import com.gms.backend.domain.domain.model.billing.Invoice
import com.gms.backend.domain.domain.model.member.Member
import com.gms.backend.domain.domain.model.member.MemberSubscription
import com.gms.backend.domain.domain.repository.billing.InvoiceRepository
import com.gms.backend.domain.domain.repository.member.MemberRepository
import com.gms.backend.domain.domain.repository.member.MemberSubscriptionRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import org.quartz.JobExecutionContext
import org.springframework.core.env.Environment
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

class InvoiceOverdueJob(
    private val invoiceRepository: InvoiceRepository,
    private val memberSubscriptionRepository: MemberSubscriptionRepository,
    private val memberRepository: MemberRepository,
    private val actorRepository: ActorRepository,
    private val environment: Environment
) : QuartzJobBean() {

    override fun executeInternal(context: JobExecutionContext) {
        val authorities = AuthorityUtils.createAuthorityList()
        val auth = UsernamePasswordAuthenticationToken("system_invoiceSchedule_overdue", null, authorities)
        SecurityContextHolder.getContext().authentication = auth
        try {
            val systemId = UUID.fromString(environment.getProperty("system.id"))
                ?: throw DomainException(ApiErrorType.INTERNAL_SERVER_ERROR, "System ID configuration missing")
            val invoiceId = context.jobDetail.jobDataMap.getString("invoiceId")
            // Invoice
            val invoice = invoiceRepository.findById(UUID.fromString(invoiceId)).orElseThrow().apply {
                status = Invoice.InvoiceStatus.OVERDUE
                updatedBy = actorRepository.getReferenceById(systemId)
            }
            invoiceRepository.save(invoice)
            // Member Subscription
            val memberSubscription =
                memberSubscriptionRepository.findById(invoice.memberSubscriptionId!!).orElseThrow().apply {
                    status = MemberSubscription.MemberSubscriptionStatus.CANCELED
                    updatedBy = actorRepository.getReferenceById(systemId)
                }
            memberSubscriptionRepository.save(memberSubscription)
            // Member
            memberSubscriptionRepository.findMemberByMemberSubscriptionId(memberSubscription.id).ifPresent { member ->
                member.status = Member.MemberStatus.DEACTIVATED
                member.updatedBy = actorRepository.getReferenceById(systemId)
                memberRepository.save(member)
            }
        } finally {
            SecurityContextHolder.clearContext()
        }

    }
}