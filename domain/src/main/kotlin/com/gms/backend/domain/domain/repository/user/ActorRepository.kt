package com.gms.backend.domain.domain.repository.user

import com.gms.backend.domain.domain.model.user.Actor
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ActorRepository : JpaRepository<Actor, UUID>{
    fun findByType(type: Actor.ActorType): Optional<Actor>
}
