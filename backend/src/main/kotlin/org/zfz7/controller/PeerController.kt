package org.zfz7.controller

import org.springframework.core.io.InputStreamResource
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.zfz7.exchange.PeerConfigRequest
import org.zfz7.exchange.PeerDTO
import org.zfz7.exchange.PeerRequest
import org.zfz7.exchange.toDto
import org.zfz7.service.CodeService
import org.zfz7.service.LogService
import org.zfz7.service.PeerService
import javax.servlet.http.HttpServletRequest


@RestController
@RequestMapping("/api/peer")
class PeerController(
  val peerService: PeerService,
  val codeService: CodeService,
  val logService: LogService,
) {

  @PostMapping()
  fun createPeer(@RequestBody body: PeerRequest, request: HttpServletRequest): ResponseEntity<PeerDTO> {
    return try {
      codeService.checkCode(body.code)
      return ResponseEntity.accepted().body(peerService.createNewPeer().toDto())
    } catch (e: Exception) {
      logService.logInvalidAccessCodeEvent(request.remoteAddr)
      ResponseEntity(HttpStatus.BAD_REQUEST)
    }
  }

  @PostMapping("/config")
  @ResponseStatus(code = HttpStatus.OK)
  fun getPeerConfig(@RequestBody body: PeerConfigRequest): ResponseEntity<InputStreamResource> {
    val headers = HttpHeaders()
    val conf = peerService.getPeerConfig(body.id)
    headers.contentType = MediaType.APPLICATION_OCTET_STREAM
    headers.contentDisposition =
      ContentDisposition.builder("attachment")
        .filename(conf.filename)
        .build()
    return ResponseEntity(InputStreamResource(conf.file), headers, HttpStatus.OK)
  }
}