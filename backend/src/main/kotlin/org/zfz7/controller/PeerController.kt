package org.zfz7.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.zfz7.domain.Peer
import org.zfz7.exchange.PeerDTO
import org.zfz7.exchange.toDto
import org.zfz7.repository.PeerRepository

@RestController
@RequestMapping("/api/peer")
class PeerController(
    val peerRepository: PeerRepository
) {

    @PostMapping()
    @ResponseStatus(code = HttpStatus.CREATED)
    fun createPeer(): PeerDTO {
        return peerRepository.save(Peer()).toDto()
    }
}