package org.zfz7.exchange

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import java.time.Instant

@ControllerAdvice
class ControllerErrorHandler {
  @ExceptionHandler(NotFoundException::class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  fun processNotFoundError(ex: NotFoundException) = ErrorDTO(
    status = HttpStatus.NOT_FOUND.value(),
    error = HttpStatus.NOT_FOUND.reasonPhrase,
    message = ex.localizedMessage,
  )

  @ExceptionHandler(PrincipalForbiddenException::class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ResponseBody
  fun processNotFoundError(ex: PrincipalForbiddenException) = ErrorDTO(
    status = HttpStatus.FORBIDDEN.value(),
    error = HttpStatus.FORBIDDEN.reasonPhrase,
    message = ex.localizedMessage,
  )
}

class NotFoundException(message:String): Exception(message)
class PrincipalForbiddenException(): Exception("This user is forbidden to view this content")

data class ErrorDTO(
  val error: String,
  val message: String,
  val status: Int,
  val timestamp: Instant = Instant.now(),
)