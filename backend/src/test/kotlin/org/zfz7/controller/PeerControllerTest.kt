package org.zfz7.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.mockkStatic
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.zfz7.domain.Peer
import org.zfz7.exchange.PeerDTO
import org.zfz7.repository.PeerRepository
import java.time.Instant
import java.time.temporal.ChronoUnit

@SpringBootTest
@AutoConfigureMockMvc
@Sql("classpath:setup.sql")
class PeerControllerTest {
  @Autowired
  private lateinit var peerRepository: PeerRepository

  @Autowired
  private lateinit var mockMvc: MockMvc

  @Autowired
  private lateinit var jackson2ObjectMapperBuilder: Jackson2ObjectMapperBuilder

  private lateinit var objectMapper: ObjectMapper

  @BeforeEach
  fun setup() {
    objectMapper = jackson2ObjectMapperBuilder.build()
  }

  @Test
  @DisplayName("Create new peer")
  fun `Should create a new peer`() {
    val now = Instant.now()
    mockkStatic(Instant::class)
    every { Instant.now() } returns now
    val result = mockMvc.perform(
      MockMvcRequestBuilders.post("/api/peer")
        .contentType(MediaType.APPLICATION_JSON)
    )
      .andExpect(status().isCreated)
      .andReturn()

    val response = objectMapper.readValue(result.response.contentAsByteArray, PeerDTO::class.java)
    assertThat(response.id).isNotNull
    assertThat(response.expiration).isEqualTo(now.plus(30, ChronoUnit.DAYS))

    assertThat(peerRepository.findAll().size).isEqualTo(1)
  }

  @Test
  @DisplayName("Get peer's config file")
  fun getPeerConfig() {
    var peer = Peer(
      address = "10.8.0.3/24,fd42:42:42::3/64",
      privateKey = "ABC",
      allowedIps = "0.0.0.0/0,::/0",
      endPoint = "relay.zfz7.org:51820",
      preSharedKey = "DEF",
      publicKey = "GHI"
    )
    peer = peerRepository.save(peer)
    val fileResponse = mockMvc.perform(
      MockMvcRequestBuilders.get("/api/peer/${peer.publicId}")
        .contentType(MediaType.TEXT_PLAIN)
    )
      .andExpect(status().isOk)
      .andReturn().response

    assertThat(fileResponse.headerNames).contains("Content-Disposition")
    assertThat(fileResponse.getHeaderValue("Content-Disposition").toString()).contains("relay.conf")
    assertThat(fileResponse.contentAsString).contains("[Interface]")
    assertThat(fileResponse.contentAsString).contains("Address = 10.8.0.3/24,fd42:42:42::3/64")
    assertThat(fileResponse.contentAsString).contains("PrivateKey = ABC")
    assertThat(fileResponse.contentAsString).contains("[Peer]")
    assertThat(fileResponse.contentAsString).contains("AllowedIPs = 0.0.0.0/0,::/0")
    assertThat(fileResponse.contentAsString).contains("Endpoint = relay.zfz7.org:51820")
    assertThat(fileResponse.contentAsString).contains("PresharedKey = DEF")
    assertThat(fileResponse.contentAsString).contains("PublicKey = GHI")
  }
}
