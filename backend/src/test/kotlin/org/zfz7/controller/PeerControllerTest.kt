package org.zfz7.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.mockkStatic
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.zfz7.domain.LogType
import org.zfz7.domain.Peer
import org.zfz7.exchange.PeerConfigRequest
import org.zfz7.exchange.PeerDTO
import org.zfz7.exchange.PeerRequest
import org.zfz7.repository.LogEventRepository
import org.zfz7.repository.PeerRepository
import org.zfz7.service.PeerService
import org.zfz7.service.WgService
import java.time.Instant
import java.time.temporal.ChronoUnit

@SpringBootTest
@AutoConfigureMockMvc
@Sql("classpath:setup.sql")
class PeerControllerTest {
  @Autowired
  private lateinit var peerRepository: PeerRepository

  @Autowired
  private lateinit var peerService: PeerService

  @Autowired
  private lateinit var logEventRepository: LogEventRepository

  @Autowired
  private lateinit var wgService: WgService

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
        .content(
          objectMapper.writeValueAsBytes(
            PeerRequest(
              code = "test-code"
            )
          )
        )
    )
      .andExpect(status().isAccepted)
      .andReturn()

    val response = objectMapper.readValue(result.response.contentAsByteArray, PeerDTO::class.java)
    assertThat(response.id).isNotNull
    assertThat(response.expiration).isEqualTo(now.plus(10, ChronoUnit.DAYS))

    assertThat(peerRepository.findAll().size).isEqualTo(1)
    assertThat(peerRepository.findAll()[0].endPoint).isEqualTo("example.com:51820")
  }

  @Test
  @DisplayName("Create new peer")
  fun `Should not create a new peer when code is bad`() {
    mockMvc.perform(
      MockMvcRequestBuilders.post("/api/peer")
        .contentType(MediaType.APPLICATION_JSON)
        .content(
          objectMapper.writeValueAsBytes(
            PeerRequest(
              code = "wrong code"
            )
          )
        )
    )
      .andExpect(status().isBadRequest)
      .andReturn()
    assertThat(peerRepository.findAll().size).isEqualTo(0)

    val logs = logEventRepository.findAll()
    assertThat(logs.size).isEqualTo(1)
    assertThat(logs[0].key1).isEqualTo("127.0.0.1")
    assertThat(logs[0].message).contains("An invalid credential request from this address: 127.0.0.1")
    assertThat(logs[0].logType).isEqualTo(LogType.INVALID_ACCESS_CODE)
  }

  @Test
  @DisplayName("Get peer's config file")
  fun getPeerConfig() {
    var peer = Peer(
      address = "10.8.0.3/24,fd42:42:42::3/64",
      privateKey = "ABC",
      allowedIps = "0.0.0.0/0,::/0",
      endPoint = "example.com:51820",
      preSharedKey = "DEF",
      publicKey = "GHI",
      expiration = Instant.parse("2018-11-30T18:35:24.00Z")
    )
    peer = peerRepository.save(peer)
    val fileResponse = mockMvc.perform(
      MockMvcRequestBuilders.post("/api/peer/config")
        .contentType(MediaType.APPLICATION_JSON)
        .content(
          objectMapper.writeValueAsBytes(
            PeerConfigRequest(
              id = peer.publicId
            )
          )
        )
    )
      .andExpect(status().isOk)
      .andReturn().response

    assertThat(fileResponse.headerNames).contains("Content-Disposition")
    assertThat(fileResponse.getHeaderValue("Content-Disposition").toString()).contains("relayExp30Nov.conf")
    assertThat(fileResponse.contentAsString).contains("[Interface]")
    assertThat(fileResponse.contentAsString).contains("Address = 10.8.0.3/24,fd42:42:42::3/64")
    assertThat(fileResponse.contentAsString).contains("PrivateKey = ABC")
    assertThat(fileResponse.contentAsString).contains("DNS = 10.0.0.1")
    assertThat(fileResponse.contentAsString).contains("[Peer]")
    assertThat(fileResponse.contentAsString).contains("AllowedIPs = 0.0.0.0/0,::/0")
    assertThat(fileResponse.contentAsString).contains("Endpoint = example.com:51820")
    assertThat(fileResponse.contentAsString).contains("PresharedKey = DEF")
    assertThat(fileResponse.contentAsString).contains("PublicKey =")
  }

  @Test
  @DisplayName("support up to 256^2 peers")
  fun supportManyPeers() {
    var peer =  peerService.createNewPeer()
    assertThat(peer.address).isEqualTo("10.0.0.2/32")
    peer =  peerService.createNewPeer()
    assertThat(peer.address).isEqualTo("10.0.0.3/32")
    peerRepository.save(Peer(
      address = "10.0.0.255/32",
      privateKey = "ABC",
      allowedIps = "0.0.0.0/0,::/0",
      endPoint = "example.com:51820",
      preSharedKey = "DEF",
      publicKey = "GHI",
      expiration = Instant.now()
    ))
    peer =  peerService.createNewPeer()
    assertThat(peer.address).isEqualTo("10.0.1.0/32")
    peer =  peerService.createNewPeer()
    assertThat(peer.address).isEqualTo("10.0.1.1/32")

    peerRepository.save(Peer(
      address = "10.0.255.254/32",
      privateKey = "ABC",
      allowedIps = "0.0.0.0/0,::/0",
      endPoint = "example.com:51820",
      preSharedKey = "DEF",
      publicKey = "GHI",
      expiration = Instant.now()
    ))
    peer =  peerService.createNewPeer()
    assertThat(peer.address).isEqualTo("10.0.255.255/32")
    assertThrows<Exception> {
      peer =  peerService.createNewPeer()
    }
  }

  @Test
  fun removePeersAfterExpiration() {
    peerRepository.save(Peer(
      address = "10.0.0.1/32",
      privateKey = "ABC",
      allowedIps = "0.0.0.0/0,::/0",
      endPoint = "example.com:51820",
      preSharedKey = "DEF",
      publicKey = "GHI",
      expiration = Instant.now().minus(15, ChronoUnit.DAYS)
    ))

    peerRepository.save(Peer(
      address = "10.0.0.2/32",
      privateKey = "ABC",
      allowedIps = "0.0.0.0/0,::/0",
      endPoint = "example.com:51820",
      preSharedKey = "DEF",
      publicKey = "GHI",
      expiration = Instant.now().plus(15, ChronoUnit.DAYS)
    ))
    peerService.removeExpiredPeers()

    val peers = peerRepository.findAll()
    assertThat(peers.size).isEqualTo(1)
    assertThat(peers[0].address).isEqualTo("10.0.0.2/32")

    val logs = logEventRepository.findAll()
    assertThat(logs.size).isEqualTo(1)
    assertThat(logs[0].key1).isEqualTo("10.0.0.1/32")
    assertThat(logs[0].message).contains("An expired peer was removed. Peer address: 10.0.0.1/32")
    assertThat(logs[0].logType).isEqualTo(LogType.PEER_REMOVED)
  }
}
