package org.zfz7.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.zfz7.domain.Config
import org.zfz7.exchange.ConfigDTO
import org.zfz7.repository.ConfigRepository
import java.time.Instant

@SpringBootTest()
@AutoConfigureMockMvc
@Sql("classpath:setup.sql")
class ConfigServiceTest {

  @Autowired
  private lateinit var configService: ConfigService

  @Autowired
  private lateinit var configRepository: ConfigRepository

  @BeforeEach
  fun beforeEach(){
    configRepository.deleteAll()
  }
  @Test
  fun `return correct config`() {
    configRepository.save(Config(disableLogs = false, clientValidDuration = 20, validUntil = Instant.now()))
    configRepository.save(Config(disableLogs = true, clientValidDuration = 15,validUntil = null))
    assertThat(configService.getConfig().disableLogs).isEqualTo(true)
    assertThat(configService.getConfig().clientValidDuration).isEqualTo(15)
  }

  @Test
  fun `if there are multiple valid configs in database (takes latest)`() {
    configRepository.save(Config(disableLogs = false, clientValidDuration = 20, validUntil = null,  createdDate = Instant.now()))
    configRepository.save(Config(disableLogs = true, clientValidDuration = 15, validUntil = null,  createdDate = Instant.now().plusSeconds(100)))
    assertThat(configService.getConfig().disableLogs).isEqualTo(true)
    assertThat(configService.getConfig().clientValidDuration).isEqualTo(15)
  }

  @Test
  fun `if there are multiple valid configs in database (takes latest) 1`() {
    configRepository.save(Config(disableLogs = true, clientValidDuration = 15, validUntil = null,  createdDate = Instant.now().plusSeconds(100)))
    configRepository.save(Config(disableLogs = false, clientValidDuration = 20, validUntil = null,  createdDate = Instant.now()))
    assertThat(configService.getConfig().disableLogs).isEqualTo(true)
    assertThat(configService.getConfig().clientValidDuration).isEqualTo(15)
  }

  @Test
  fun `updates config`() {
    configRepository.save(Config(disableLogs = false, clientValidDuration = 20, validUntil = null,  createdDate = Instant.now().plusSeconds(-100)))
    configService.updateConfig(ConfigDTO(disableLogs = true, clientValidDuration = 25))
    val configs = configRepository.findAll().sortedBy { it.createdDate }
    assertThat(configs).hasSize(2)
    assertThat(configs[0].disableLogs).isEqualTo(false)
    assertThat(configs[0].clientValidDuration).isEqualTo(20)
    assertThat(configs[0].validUntil).isNotNull
    assertThat(configs[1].disableLogs).isEqualTo(true)
    assertThat(configs[1].clientValidDuration).isEqualTo(25)
    assertThat(configs[1].validUntil).isNull()
  }

  @Test
  fun `can handle multiple valid configs (shouldn't happen)`() {
    configRepository.save(Config(disableLogs = false, clientValidDuration = 20, validUntil = null,  createdDate = Instant.now().plusSeconds(-100)))
    configRepository.save(Config(disableLogs = true, clientValidDuration = 25, validUntil = null,  createdDate = Instant.now().plusSeconds(-50)))
    configService.updateConfig(ConfigDTO(disableLogs = true, clientValidDuration = 30))
    val configs = configRepository.findAll().sortedBy { it.createdDate }
    assertThat(configs).hasSize(3)
    assertThat(configs[0].disableLogs).isEqualTo(false)
    assertThat(configs[0].clientValidDuration).isEqualTo(20)
    assertThat(configs[0].validUntil).isNotNull
    assertThat(configs[1].disableLogs).isEqualTo(true)
    assertThat(configs[1].clientValidDuration).isEqualTo(25)
    assertThat(configs[1].validUntil).isNotNull

    assertThat(configs[2].disableLogs).isEqualTo(true)
    assertThat(configs[2].clientValidDuration).isEqualTo(30)
    assertThat(configs[2].validUntil).isNull()
  }
}
