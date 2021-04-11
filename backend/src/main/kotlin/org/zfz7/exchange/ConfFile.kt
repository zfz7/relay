package org.zfz7.exchange

import java.io.InputStream

data class ConfFile(
  val filename: String,
  val file: InputStream,
)
