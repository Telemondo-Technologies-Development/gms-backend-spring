package com.gms.backend.domain.domain.repository.user

import com.gms.backend.domain.domain.model.user.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, UUID>
