package org.zfz7.exchange

import org.zfz7.domain.Config


data class ConfigDTO(
  val disableLogs: Boolean,
  val clientValidDuration: Int,
)

fun Config.toDto(): ConfigDTO = ConfigDTO(
  disableLogs = disableLogs,
  clientValidDuration = clientValidDuration
)