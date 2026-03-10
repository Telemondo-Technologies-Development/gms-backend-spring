package com.gms.backend.domain.infra.quartz.billing

import com.gms.backend.domain.domain.model.billing.Invoice
import com.gms.backend.domain.domain.model.member.Member
import com.gms.backend.domain.domain.model.subscription.BillingCycle
import com.gms.backend.domain.domain.repository.billing.InvoiceRepository
import org.quartz.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*

@Service
@PreAuthorize("denyAll()")
class InvoiceScheduleService(
    private val invoiceRepository: InvoiceRepository,
    private val scheduler: Scheduler
) {
    @Transactional
    @PreAuthorize("hasAuthority('invoiceSchedule_update')")
    fun checkPending() {
        val startTime = Instant.now().truncatedTo(ChronoUnit.DAYS)
        // Check tomorrow too just to be sure
        val endTime = startTime.plus(2, ChronoUnit.DAYS) //.minusNanos(1)

        // Will search for all pending with due date up to tomorrow
        val entries = invoiceRepository.findAllByStatusAndDueDate(
            Member.MemberStatus.ACTIVE,
            Invoice.InvoiceStatus.PENDING,
        //    startTime,
            endTime
        )

        entries.forEach { invoice ->
            val jobDataMap = JobDataMap().apply { put("invoiceId", invoice.id.toString()) }

            val jobDetail = JobBuilder.newJob(SingleInvoiceStatusJob::class.java)
                .withIdentity("invoiceUpdateJob-${invoice.id}", "invoiceGroup")
                .usingJobData(jobDataMap)
                .build()

            val trigger = TriggerBuilder.newTrigger()
                .withIdentity("invoiceUpdateTrigger-${invoice.id}", "invoiceGroup")
                .startAt(invoice.dueDate)
                // Explicitly configured to fire as soon as possible
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build()

            // Schedule the job dynamically
            if (!scheduler.checkExists(jobDetail.key)) {
                scheduler.scheduleJob(jobDetail, trigger)
            }
        }

    }

    @Transactional
    @PreAuthorize("hasAuthority('invoiceSchedule_update')")
    fun overdue(invoice: Invoice) {
        val jobDataMap = JobDataMap().apply { put("invoiceId", invoice.id.toString()) }

        val jobDetail = JobBuilder.newJob(InvoiceOverdueJob::class.java)
            .withIdentity("invoiceOverdueJob-${invoice.id}", "invoiceGroup")
            .usingJobData(jobDataMap)
            .build()

        val trigger = TriggerBuilder.newTrigger()
            .withIdentity("invoiceOverdueTrigger-${invoice.id}", "invoiceGroup")
            .startAt(invoice.gracePeriodDate)
            //.startAt(Instant.now())
            // Explicitly configured to fire as soon as possible
            .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
            .build()

        // Schedule the job dynamically
        if (!scheduler.checkExists(jobDetail.key)) {
            scheduler.scheduleJob(jobDetail, trigger)
        }
    }

    @Transactional
    @PreAuthorize("hasAuthority('invoice_create')")
    fun createPendingInvoice(id: UUID) {
        val invoice = invoiceRepository.findById(id).orElseThrow()

        val currentZonedDate = invoice.dueDate.atZone(ZoneId.systemDefault())

        val nextZonedDate = when (invoice.subscriptionAvailed.intervals) {
            BillingCycle.Interval.ONCE -> null
            BillingCycle.Interval.MINUTES -> currentZonedDate.plusMinutes(invoice.subscriptionAvailed.intervalCount.toLong())
            BillingCycle.Interval.DAILY -> currentZonedDate.plusDays(invoice.subscriptionAvailed.intervalCount.toLong())
            BillingCycle.Interval.WEEKLY -> currentZonedDate.plusWeeks(invoice.subscriptionAvailed.intervalCount.toLong())
            BillingCycle.Interval.MONTHLY -> currentZonedDate.plusMonths(invoice.subscriptionAvailed.intervalCount.toLong())
            BillingCycle.Interval.YEARLY -> currentZonedDate.plusYears(invoice.subscriptionAvailed.intervalCount.toLong())
        }

        if (nextZonedDate == null) return

        val nextDueDate = nextZonedDate.toInstant()
        // If this date is already in the past, the job will instantly create the invoice
        val creationDate = nextDueDate.minus(7, ChronoUnit.DAYS)

        val jobDataMap = JobDataMap().apply {
            put("invoiceId", invoice.id.toString())
            put("nextDueDate", nextDueDate.toString())
        }

        val jobDetail = JobBuilder.newJob(InvoiceRenewalJob::class.java)
            .withIdentity("invoiceCreateJob-${invoice.id}", "invoiceGroup")
            .usingJobData(jobDataMap)
            .build()

        val trigger = TriggerBuilder.newTrigger()
            .withIdentity("invoiceCreateTrigger-${invoice.id}", "invoiceGroup")
            .startAt(creationDate)
            // Explicitly configured to fire as soon as possible
            .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
            .build()

        // Schedule the job dynamically
        if (!scheduler.checkExists(jobDetail.key)) {
            scheduler.scheduleJob(jobDetail, trigger)
        }

    }
}