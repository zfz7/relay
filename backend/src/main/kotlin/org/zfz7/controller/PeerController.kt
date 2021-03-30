package org.zfz7.controller

import org.springframework.core.io.InputStreamResource
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.zfz7.exchange.PeerConfigRequest
import org.zfz7.exchange.PeerDTO
import org.zfz7.exchange.PeerRequest
import org.zfz7.service.CodeService
import org.zfz7.service.PeerService


@RestController
@RequestMapping("/api/peer")
class PeerController(
  val peerService: PeerService,
  val codeService: CodeService
) {

  @PostMapping()
  fun createPeer(@RequestBody body: PeerRequest): ResponseEntity<PeerDTO> {
    return try {
      codeService.checkCode(body.code)
      return ResponseEntity.accepted().body(peerService.createNewPeer())
    } catch (e: Exception) {
      ResponseEntity(HttpStatus.BAD_REQUEST)
    }
  }

  @PostMapping("/config")
  @ResponseStatus(code = HttpStatus.OK)
  fun getPeerConfig(@RequestBody body: PeerConfigRequest): ResponseEntity<InputStreamResource> {
    val fileName = "relay.conf"
    val headers = HttpHeaders()
    headers.contentType = MediaType.APPLICATION_OCTET_STREAM
    headers.contentDisposition =
      ContentDisposition.builder("attachment")
        .filename(fileName)
        .build()
    val peerConfig = peerService.getPeerConfig(body.id)
    return ResponseEntity(InputStreamResource(peerConfig.inputStream()), headers, HttpStatus.OK)
  }
}