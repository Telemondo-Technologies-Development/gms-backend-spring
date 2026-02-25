package com.gms.backend.domain.domain.model.member

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.*

@Entity
@Table(name = "member_progress_history")
class MemberProgressHistory {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "binary(16)")
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    lateinit var id: UUID

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "member_progress_id", nullable = false)
    @JsonIgnore
    lateinit var memberProgress: MemberProgress

    @Column(name = "member_progress_id", insertable = false, updatable = false)
    var memberProgressId: UUID? = null

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "progress_id", nullable = false)
    @JsonIgnore
    lateinit var progress: Progress

    @Column(name = "progress_id", insertable = false, updatable = false)
    var progressId: UUID? = null

    @Column(nullable = false, updatable = false)
    lateinit var changedAt: Instant
}
