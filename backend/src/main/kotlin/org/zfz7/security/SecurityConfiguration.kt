package org.zfz7.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@EnableWebSecurity
@Profile("!integration")
class SecurityConfiguration {
  @Bean
  fun filterChain(http: HttpSecurity): SecurityFilterChain {
    http.csrf().disable()
      .authorizeRequests()
      .antMatchers("/admin").authenticated()
      .antMatchers("/api/admin/**").authenticated()
      .antMatchers("/", "/home", "/webjars/**", "/public/**", "/static").permitAll()
      .antMatchers("/**/*.{js,html,css}").permitAll()
      .antMatchers("/api/**").permitAll()
      .and()
      .oauth2Login()
    return http.build()
  }
}

@EnableWebSecurity
@Profile("integration")
class SecurityIntegrationConfiguration : WebMvcConfigurer {
  @Bean
  fun filterChain(http: HttpSecurity): SecurityFilterChain {
    http.csrf().disable()
      .authorizeRequests()
      .anyRequest().permitAll()
    return http.build()
  }
}
