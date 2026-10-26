/*
 Copyright 2020-2024 Google LLC
 Copyright 2020-2024 EPAM Systems, Inc

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
*/

package org.opengroup.osdu.unitservice.security;

import org.opengroup.osdu.unitservice.middleware.AuthenticationRequestFilter;
import org.opengroup.osdu.unitservice.middleware.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  private final AuthenticationRequestFilter authFilter;

  // Spring Security 7 path patterns must start with "/" and may contain only one "**".
  private static final String[] AUTH_WHITELIST = {
    "/",
    "/v2/api-docs",
    "/v3/api-docs",
    "/swagger",
    "/swagger-resources/**",
    "/swagger-ui.html",
    "/v3/info",
    "/v3/_ah/**",
    "/api/unit/v3/info",
    "/webjars/**",
    "/_ah/**",
    "/actuator/**",
    "/error",
    "/favicon.ico",
    "/csrf",
    "/error",
    "/favicon.ico",
    "/api/unit/actuator/health",
    "/swagger-ui/**",
    "/api-docs/swagger-config",
    "/api-docs/**",
    "/api-docs.yaml",
    "/api-docs.yaml/**",
    "/unit",
    "/health/**"
  };

  public SecurityConfiguration(AuthenticationService authenticationService) {
    authFilter = new AuthenticationRequestFilter(authenticationService);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            authorizeRequests ->
                authorizeRequests
                    .requestMatchers(AUTH_WHITELIST)
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .sessionManagement(
            sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.NEVER))
        .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
        .csrf(csrf -> csrf.disable());
    return http.build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring().requestMatchers(AUTH_WHITELIST);
  }

}
