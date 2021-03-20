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
    val result = mockMvc.perform(MockMvcRequestBuilders.post("/api/peer")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated)
        .andReturn()

    val response = objectMapper.readValue(result.response.contentAsByteArray, PeerDTO::class.java)
    assertThat(response.id).isNotNull
    assertThat(response.expiration).isEqualTo(now.plus(30, ChronoUnit.DAYS))

    assertThat(peerRepository.findAll().size).isEqualTo(1)
  }
}
