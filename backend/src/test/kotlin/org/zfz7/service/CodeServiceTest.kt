package org.zfz7.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.zfz7.domain.Code
import org.zfz7.repository.CodeRepository
import java.time.Instant

@SpringBootTest()
@AutoConfigureMockMvc
@Sql("classpath:setup.sql")
class CodeServiceTest {

  @Autowired
  private lateinit var codeService: CodeService

  @Autowired
  private lateinit var codeRepository: CodeRepository

  @Test
  fun `not code saved will always fail code check`() {
    assertThrows<Exception> { codeService.checkCode("test") }
  }

  @Test
  fun `if code is correct`() {
    codeRepository.save(Code(code = "test"))
    assertThat(codeService.checkCode("test")).isEqualTo(true)
    assertThrows<Exception> { codeService.checkCode("thing") }
  }

  @Test
  fun `if code is correct and there are multiple codes in database`() {
    codeRepository.saveAll(listOf(Code(code = "valid"),Code(code = "not valid", validUntil = Instant.now())))
    assertThat(codeService.checkCode("valid")).isEqualTo(true)
    assertThrows<Exception> { codeService.checkCode("not valid") }
  }

  @Test
  fun `if code is correct and there are multiple codes in database in reverse order`() {
    codeRepository.saveAll(listOf(Code(code = "not valid", validUntil = Instant.now()),Code(code = "valid")))
    assertThat(codeService.checkCode("valid")).isEqualTo(true)
    assertThrows<Exception> { codeService.checkCode("not valid") }
  }

  @Test
  fun `if code is correct and there are multiple valid codes in database (takes latest)`() {
    codeRepository.saveAll(listOf(Code(code = "not valid", createdDate = Instant.now()),Code(code = "valid", createdDate = Instant.now().plusSeconds(500))))
    assertThat(codeService.checkCode("valid")).isEqualTo(true)
    assertThrows<Exception> { codeService.checkCode("not valid") }
  }

  @Test
  fun `if code is correct and there are multiple valid codes in database (takes latest) reverse order`() {
    codeRepository.saveAll(listOf(Code(code = "valid", createdDate = Instant.now().plusSeconds(500)),Code(code = "not valid", createdDate = Instant.now())))
    assertThat(codeService.checkCode("valid")).isEqualTo(true)
    assertThrows<Exception> { codeService.checkCode("not valid") }
  }
}
