package org.zfz7.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class CodeService(
  @Value("\${codeService.code}") val code: String
){
  fun checkCode(cd: String): Boolean{
    if(cd == code){
      return true
    } else {
      throw Exception("Incorrect Code")
    }
  }
}