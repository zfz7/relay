package org.zfz7.service
import javax.annotation.PostConstruct

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.zfz7.repository.RelayRepository


@Component
class StartUp {
  @Autowired
  private lateinit var relayRepository: RelayRepository
  @Autowired
  private lateinit var relayService: RelayService
  @PostConstruct
  fun init() {
    if(relayRepository.count() == 0L){
      relayService.createRelay()
    }
    relayService.writeWg0File()
  }
}