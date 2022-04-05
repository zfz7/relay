package org.zfz7.security

import org.springframework.context.annotation.Profile
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Component
import org.zfz7.exchange.PrincipalForbiddenException
import java.security.Principal

interface PrincipalValidator{
  fun validate (principal: Principal?)
}

@Component
@Profile("!integration")
class GithubPrincipalValidator: PrincipalValidator {
  override fun validate(principal: Principal?){
    if(principal is OAuth2AuthenticationToken) {
      val oAuth2AuthenticationToken:OAuth2AuthenticationToken = principal
      val oAuth2User: OAuth2User = oAuth2AuthenticationToken.principal
      val attributes = oAuth2User.attributes
      if(attributes["login"] == "zfz7"){
        return
      }
    }
    throw PrincipalForbiddenException()
  }
}

@Component
@Profile("integration")
class IntegrationPrincipalValidator: PrincipalValidator {
  override fun validate(principal: Principal?){ return }
}

