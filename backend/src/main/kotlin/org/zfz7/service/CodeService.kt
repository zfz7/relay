package org.zfz7.service

import org.springframework.stereotype.Service
import org.zfz7.domain.Code
import org.zfz7.repository.CodeRepository
import java.time.Instant

@Service
class CodeService(
  val codeRepository: CodeRepository
) {
  fun checkCode(cd: String): Boolean {
    if (cd == getCode()) {
      return true
    } else {
      throw Exception("Incorrect Code")
    }
  }

  fun getCode(): String? {
    return codeRepository.findAllByValidUntilIsNull().maxByOrNull { it.createdDate }?.code
  }

  fun updateCode(code: String) {
    val now = Instant.now()
    val validCode = codeRepository.findAllByValidUntilIsNull()
    codeRepository.saveAll(
      listOf(
        validCode.map { it.copy(validUntil = now) },
        listOf(Code(code = code))
      ).flatten()
    )
  }
}