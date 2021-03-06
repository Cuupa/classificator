package com.cuupa.classificator.monitor.persistence.sqlite

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface EventRepository : JpaRepository<EventEntity, LocalDateTime>