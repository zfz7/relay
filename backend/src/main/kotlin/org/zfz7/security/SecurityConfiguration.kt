package org.zfz7.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
@Profile("!integration")
class SecurityConfiguration: WebMvcConfigurer {
  @Bean
  fun filterChain(http: HttpSecurity): SecurityFilterChain {
    http.csrf().disable()
      .authorizeHttpRequests()
      .requestMatchers("/admin").authenticated()
      .requestMatchers("/api/admin/**").authenticated()
      .requestMatchers("/api/**").permitAll()
      .requestMatchers("/", "/static/**").permitAll()
      .requestMatchers("/**.js","/**.html","/**.css").permitAll()
      .and()
      .oauth2Login()
    return http.build()
  }
}
@Configuration
@Profile("integration")
class SecurityIntegrationConfiguration : WebMvcConfigurer {
  @Bean
  fun filterChain(http: HttpSecurity): SecurityFilterChain {
    http.csrf().disable()
      .authorizeHttpRequests()
      .anyRequest().permitAll()
    return http.build()
  }
}
