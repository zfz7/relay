package org.zfz7.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.zfz7.domain.Config
import org.zfz7.domain.LogEvent
import org.zfz7.domain.LogType
import org.zfz7.domain.Peer
import org.zfz7.repository.ConfigRepository
import org.zfz7.repository.LogEventRepository
import java.time.Instant

@SpringBootTest()
@AutoConfigureMockMvc
@Sql("classpath:setup.sql")
class LogServiceTest {

  @Autowired
  private lateinit var logService: LogService

  @Autowired
  private lateinit var logEventRepository: LogEventRepository

  @Autowired
  private lateinit var configRepository: ConfigRepository

  @BeforeEach
  fun beforeEach(){
    configRepository.deleteAll()
    configRepository.save(Config(disableLogs = true, clientValidDuration = 10))
  }
  @Test
  fun `returns empty logs when disableLogs is true`() {
    logEventRepository.save(LogEvent(message = "thing", key1 = "thing", logType = LogType.PEER_REMOVED ))
    val logs = logService.getLogs()
    assertThat(logs.invalidAccessCodeEvents).hasSize(0)
    assertThat(logs.invalidAdminAccessEvents).hasSize(0)
    assertThat(logs.peerRemovedEvents).hasSize(0)
  }

  @Test
  fun `logRemovedPeers does not save when disableLogs is true`() {
    logService.logRemovedPeers(listOf(Peer( address="", preSharedKey = "", privateKey = "", publicKey = "", endPoint = "", expiration = Instant.now())))
    assertThat(logEventRepository.findAll()).hasSize(0)
  }

  @Test
  fun `logInvalidAccessCodeEvent does not save when disableLogs is true`() {
    logService.logInvalidAccessCodeEvent("")
    assertThat(logEventRepository.findAll()).hasSize(0)
  }

  @Test
  fun `logInvalidAdminAccessEvent does not save when disableLogs is true`() {
    logService.logInvalidAdminAccessEvent("")
    assertThat(logEventRepository.findAll()).hasSize(0)
  }
}
