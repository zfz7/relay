package org.zfz7.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.zfz7.domain.Config

@Repository
interface ConfigRepository : JpaRepository<Config, Long> {
  fun findAllByValidUntilIsNull(): List<Config>
}