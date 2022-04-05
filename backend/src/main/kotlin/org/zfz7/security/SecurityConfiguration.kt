package org.zfz7.security

import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter


@EnableWebSecurity
@Profile("!integration")
class SecurityConfiguration : WebSecurityConfigurerAdapter() {

  override fun configure(http: HttpSecurity) {
    http.csrf().disable()
      .authorizeRequests()
      .antMatchers("/admin").authenticated()
      .antMatchers("/api/admin").authenticated()
      .antMatchers("/", "/home", "/webjars/**", "/public/**", "/static").permitAll()
      .antMatchers("/**/*.{js,html,css}").permitAll()
      .antMatchers("/api/**").permitAll()
      .and()
      .oauth2Login()
  }
}

@EnableWebSecurity
@Profile("integration")
class SecurityIntegrationConfiguration : WebSecurityConfigurerAdapter() {

  override fun configure(http: HttpSecurity) {
    http.csrf().disable()
      .authorizeRequests()
      .anyRequest().permitAll()
  }
}
