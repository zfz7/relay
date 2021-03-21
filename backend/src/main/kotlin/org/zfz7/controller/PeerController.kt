package org.zfz7.controller

import org.springframework.core.io.InputStreamResource
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.zfz7.exchange.PeerDTO
import org.zfz7.service.PeerService
import java.util.*


@RestController
@RequestMapping("/api/peer")
class PeerController(
    val peerService: PeerService
) {

    @PostMapping()
    @ResponseStatus(code = HttpStatus.CREATED)
    fun createPeer(): PeerDTO {
        return peerService.createNewPeer()
    }

    @GetMapping("/{peerId}")
    @ResponseStatus(code = HttpStatus.OK)
    fun getPeerConfig(@PathVariable peerId: UUID): ResponseEntity<InputStreamResource> {
        val fileName = "relay.conf"

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_OCTET_STREAM
        headers.contentDisposition =
            ContentDisposition.builder("attachment")
                .filename(fileName)
                .build()
        val peerConfig = peerService.getPeerConfig(peerId)
        return ResponseEntity(InputStreamResource(peerConfig.inputStream()), headers, HttpStatus.OK)
    }
}