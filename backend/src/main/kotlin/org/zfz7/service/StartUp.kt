package org.zfz7.service
import org.slf4j.LoggerFactory
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

  private val logger = LoggerFactory.getLogger(StartUp::class.java)
  @PostConstruct
  fun init() {
    if(relayRepository.count() == 0L){
      relayService.createRelay()
      logger.info("New relay created")
    }
  }
}