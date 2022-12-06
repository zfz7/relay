package org.zfz7.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.zfz7.domain.*
import org.zfz7.exchange.CodeDTO
import org.zfz7.exchange.ConfigDTO
import org.zfz7.exchange.Logs
import org.zfz7.exchange.Peers
import org.zfz7.repository.CodeRepository
import org.zfz7.repository.LogEventRepository
import org.zfz7.repository.PeerRepository
import org.zfz7.service.ConfigService
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
@Sql("classpath:setup.sql")
class AdminControllerTest {
  @Autowired
  private lateinit var peerRepository: PeerRepository

  @Autowired
  private lateinit var logEventRepository: LogEventRepository

  @Autowired
  private lateinit var codeRepository: CodeRepository

  @Autowired
  private lateinit var configService: ConfigService

  @Autowired
  private lateinit var mockMvc: MockMvc

  @Autowired
  private lateinit var jackson2ObjectMapperBuilder: Jackson2ObjectMapperBuilder

  private lateinit var objectMapper: ObjectMapper

  val admin1User: OidcUser = DefaultOidcUser(
    AuthorityUtils.createAuthorityList("SCOPE_message:read"),
    OidcIdToken.withTokenValue("id-token").claim("login", "admin1").build(),
    "login"
  )
  val admin2User: OidcUser = DefaultOidcUser(
    AuthorityUtils.createAuthorityList("SCOPE_message:read"),
    OidcIdToken.withTokenValue("id-token").claim("login", "admin2").build(),
    "login"
  )
  val badUser: OidcUser = DefaultOidcUser(
    AuthorityUtils.createAuthorityList("SCOPE_message:read"),
    OidcIdToken.withTokenValue("id-token").claim("login", "bad").build(),
    "login"
  )

  @BeforeEach
  fun setup() {
    objectMapper = jackson2ObjectMapperBuilder.build()
  }

  @Test
  @DisplayName("Returns Admin DTO")
  fun `Should return the correct Admin DTO`() {
    val publicId = UUID.randomUUID()
    val now = Instant.now()
    peerRepository.save(
      Peer(
        publicId = publicId,
        expiration = now,
        address = "Hello",
        privateKey = "",
        preSharedKey = "",
        publicKey = "",
        endPoint = ""
      )
    )

    val result = mockMvc.perform(
      MockMvcRequestBuilders.get("/api/admin/peers")
        .contentType(MediaType.APPLICATION_JSON)
        .with(oidcLogin().oidcUser(admin1User))
    )
      .andExpect(status().isOk)
      .andReturn()

    val response = objectMapper.readValue(result.response.contentAsByteArray, Peers::class.java)
    assertThat(response.peers).hasSize(1)
    assertThat(response.peers[0].expiration).isCloseTo(now,within(1, ChronoUnit.SECONDS) )
    assertThat(response.peers[0].address).isEqualTo("Hello")

    assertThat(peerRepository.findAll().size).isEqualTo(1)
  }

  @Test
  fun `Should return when any user is in relayConfig adminUsers`() {
    mockMvc.perform(
      MockMvcRequestBuilders.get("/api/admin/peers")
        .contentType(MediaType.APPLICATION_JSON)
        .with(oidcLogin().oidcUser(admin1User))
    )
      .andExpect(status().isOk)
      .andReturn()

    mockMvc.perform(
      MockMvcRequestBuilders.get("/api/admin/peers")
        .contentType(MediaType.APPLICATION_JSON)
        .with(oidcLogin().oidcUser(admin2User))
    )
      .andExpect(status().isOk)
      .andReturn()
  }

  @Test
  @DisplayName("Will error if user does not have correct user attributes")
  @WithMockUser
  fun `Will error if user does not have correct user attributes`() {
    mockMvc.perform(
      MockMvcRequestBuilders.get("/api/admin/peers")
        .contentType(MediaType.APPLICATION_JSON)
    )
      .andExpect(status().isForbidden)
      .andReturn()

    val logs = logEventRepository.findAll()
    assertThat(logs.size).isEqualTo(1)
    assertThat(logs[0].key1).isEqualTo("UNKNOWN")
    assertThat(logs[0].message).contains("An invalid github user: UNKNOWN, has tried to access the admin page.")
    assertThat(logs[0].logType).isEqualTo(LogType.INVALID_ADMIN_ACCESS)
  }

  @Test
  @DisplayName("Will error and record failed login attempt")
  @WithMockUser
  fun `Will error and record failed login attempt`() {
    mockMvc.perform(
      MockMvcRequestBuilders.get("/api/admin/peers")
        .contentType(MediaType.APPLICATION_JSON)
        .with(oidcLogin().oidcUser(badUser))
    )
      .andExpect(status().isForbidden)
      .andReturn()

    val logs = logEventRepository.findAll()
    assertThat(logs.size).isEqualTo(1)
    assertThat(logs[0].key1).isEqualTo("bad")
    assertThat(logs[0].message).contains("An invalid github user: bad, has tried to access the admin page.")
    assertThat(logs[0].logType).isEqualTo(LogType.INVALID_ADMIN_ACCESS)
  }

  @Test
  @DisplayName("Will redirect if no user is not logged in")
  fun `Will redirect if no user is not logged in`() {
    mockMvc.perform(
      MockMvcRequestBuilders.get("/api/admin/peers")
        .contentType(MediaType.APPLICATION_JSON)
    )
      .andExpect(status().isFound)
      .andReturn()
  }

  @Test
  @DisplayName("Will error on accessing logs with a unknown user")
  @WithMockUser
  fun `Will error on accessing logs with a unknown user`() {
    mockMvc.perform(
      MockMvcRequestBuilders.get("/api/admin/logs")
        .contentType(MediaType.APPLICATION_JSON)
        .with(oidcLogin().oidcUser(badUser))
    )
      .andExpect(status().isForbidden)
      .andReturn()
  }

  @Test
  @WithMockUser
  fun `Returns correct logs`() {
    logEventRepository.saveAll(
      listOf(
        InvalidAccessCodeEvent(ipAddress = "CHINA").toLogEvent(),
        InvalidAdminAccessEvent(username = "Bad guy").toLogEvent(),
        PeerRemovedEvent(peerAddress = "RUSSIA").toLogEvent(),
        PeerRemovedEvent(peerAddress = "RUSSIA2").toLogEvent()
      )
    )

    val result = mockMvc.perform(
      MockMvcRequestBuilders.get("/api/admin/logs")
        .contentType(MediaType.APPLICATION_JSON)
        .with(oidcLogin().oidcUser(admin1User))
    )
      .andExpect(status().isOk)
      .andReturn()

    val response = objectMapper.readValue(result.response.contentAsByteArray, Logs::class.java)
    assertThat(response.invalidAccessCodeEvents).hasSize(1)
    assertThat(response.invalidAccessCodeEvents[0].ipAddress).isEqualTo("CHINA")

    assertThat(response.invalidAdminAccessEvents).hasSize(1)
    assertThat(response.invalidAdminAccessEvents[0].username).isEqualTo("Bad guy")

    assertThat(response.peerRemovedEvents).hasSize(2)
    assertThat(response.peerRemovedEvents[0].peerAddress).isEqualTo("RUSSIA")
    assertThat(response.peerRemovedEvents[1].peerAddress).isEqualTo("RUSSIA2")
  }

  @Test
  @WithMockUser
  fun `Will update code and set old code to no longer be valid`() {
    mockMvc.perform(
      MockMvcRequestBuilders.post("/api/admin/code")
        .contentType(MediaType.APPLICATION_JSON)
        .content(
          objectMapper.writeValueAsBytes(
            CodeDTO(
              code = "test-code"
            )
          )
        )
        .with(oidcLogin().oidcUser(admin1User))
    )
      .andExpect(status().isOk)
      .andReturn()
    var codes = codeRepository.findAll()
    assertThat(codes).hasSize(1)
    assertThat(codes[0].code).isEqualTo("test-code")
    assertThat(codes[0].validUntil).isNull()

    mockMvc.perform(
      MockMvcRequestBuilders.post("/api/admin/code")
        .contentType(MediaType.APPLICATION_JSON)
        .content(
          objectMapper.writeValueAsBytes(
            CodeDTO(
              code = "test-code1"
            )
          )
        )
        .with(oidcLogin().oidcUser(admin1User))
    )
      .andExpect(status().isOk)
      .andReturn()
    codes = codeRepository.findAll().sortedBy { it.createdDate }
    assertThat(codes).hasSize(2)
    assertThat(codes[0].code).isEqualTo("test-code")
    assertThat(codes[0].validUntil).isNotNull
    assertThat(codes[1].code).isEqualTo("test-code1")
    assertThat(codes[1].validUntil).isNull()
  }

  @Test
  @WithMockUser
  fun `Will not update code when user is bad`() {
    assertThat(codeRepository.findAll()).hasSize(0)
    mockMvc.perform(
      MockMvcRequestBuilders.post("/api/admin/code")
        .contentType(MediaType.APPLICATION_JSON)
        .content(
          objectMapper.writeValueAsBytes(
            CodeDTO(
              code = "test-code"
            )
          )
        )
        .with(oidcLogin().oidcUser(badUser))
    )
      .andExpect(status().isForbidden)
      .andReturn()
    assertThat(codeRepository.findAll()).hasSize(0)
  }

  @Test
  @WithMockUser
  fun `Will not return code when user is bad`() {
    mockMvc.perform(
      MockMvcRequestBuilders.get("/api/admin/code")
        .contentType(MediaType.APPLICATION_JSON)
        .with(oidcLogin().oidcUser(badUser))
    )
      .andExpect(status().isForbidden)
      .andReturn()
  }

  @Test
  @WithMockUser
  fun `Will return code when user is admin`() {
    codeRepository.save(Code(code = "test-code"))
    val result = mockMvc.perform(
      MockMvcRequestBuilders.get("/api/admin/code")
        .contentType(MediaType.APPLICATION_JSON)
        .with(oidcLogin().oidcUser(admin1User))
    )
      .andExpect(status().isOk)
      .andReturn()

    val dto = objectMapper.readValue(result.response.contentAsByteArray, CodeDTO::class.java)
    assertThat(dto.code).isEqualTo("test-code")
  }

  @Test
  @WithMockUser
  fun `Will return config when user is admin`() {
    val result = mockMvc.perform(
      MockMvcRequestBuilders.get("/api/admin/config")
        .contentType(MediaType.APPLICATION_JSON)
        .with(oidcLogin().oidcUser(admin1User))
    )
      .andExpect(status().isOk)
      .andReturn()

    val dto = objectMapper.readValue(result.response.contentAsByteArray, ConfigDTO::class.java)
    assertThat(dto.disableLogs).isEqualTo(false)
    assertThat(dto.clientValidDuration).isEqualTo(10)
  }

  @Test
  @WithMockUser
  fun `Will not return config when user is bad`() {
    mockMvc.perform(
      MockMvcRequestBuilders.get("/api/admin/config")
        .contentType(MediaType.APPLICATION_JSON)
        .with(oidcLogin().oidcUser(badUser))
    )
      .andExpect(status().isForbidden)
      .andReturn()
  }

  @Test
  @WithMockUser
  fun `Will update config when user is admin`() {
    mockMvc.perform(
      MockMvcRequestBuilders.post("/api/admin/config")
        .contentType(MediaType.APPLICATION_JSON)
        .content(
          objectMapper.writeValueAsBytes(
            ConfigDTO(
              disableLogs = false,
              clientValidDuration = 100
            )
          )
        )
        .with(oidcLogin().oidcUser(admin1User))
    )
      .andExpect(status().isOk)
      .andReturn()
    val config = configService.getConfig()
    assertThat(config.disableLogs).isEqualTo(false)
    assertThat(config.clientValidDuration).isEqualTo(100)
  }

  @Test
  @WithMockUser
  fun `Will not update config when user is bad`() {
    mockMvc.perform(
      MockMvcRequestBuilders.post("/api/admin/config")
        .contentType(MediaType.APPLICATION_JSON)
        .content(
          objectMapper.writeValueAsBytes(
            ConfigDTO(
              disableLogs = false,
              clientValidDuration = 100
            )
          )
        )
        .with(oidcLogin().oidcUser(badUser))
    )
      .andExpect(status().isForbidden)
      .andReturn()
  }
}
