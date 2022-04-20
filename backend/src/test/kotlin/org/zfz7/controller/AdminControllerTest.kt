package org.zfz7.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
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
import org.zfz7.domain.Peer
import org.zfz7.exchange.Peers
import org.zfz7.repository.PeerRepository
import java.time.Instant
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
@Sql("classpath:setup.sql")
class AdminControllerTest {
  @Autowired
  private lateinit var peerRepository: PeerRepository

  @Autowired
  private lateinit var mockMvc: MockMvc

  @Autowired
  private lateinit var jackson2ObjectMapperBuilder: Jackson2ObjectMapperBuilder

  private lateinit var objectMapper: ObjectMapper

  val adminUser: OidcUser = DefaultOidcUser(
    AuthorityUtils.createAuthorityList("SCOPE_message:read"),
    OidcIdToken.withTokenValue("id-token").claim("login", "zfz7").build(),
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
    peerRepository.save(Peer(publicId = publicId, expiration =  now, address = "Hello", privateKey = "", preSharedKey = "", publicKey = ""))

    val result = mockMvc.perform(
      MockMvcRequestBuilders.get("/api/admin/peers")
        .contentType(MediaType.APPLICATION_JSON)
        .with(oidcLogin().oidcUser(adminUser))
    )
      .andExpect(status().isOk)
      .andReturn()

    val response = objectMapper.readValue(result.response.contentAsByteArray, Peers::class.java)
    assertThat(response.peers).hasSize(1)
    assertThat(response.peers[0].expiration).isEqualTo(now)
    assertThat(response.peers[0].address).isEqualTo("Hello")

    assertThat(peerRepository.findAll().size).isEqualTo(1)
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
}
