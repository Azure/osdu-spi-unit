package org.opengroup.osdu.unitservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import org.opengroup.osdu.unitservice.middleware.AuthenticationRequestFilter;
import org.opengroup.osdu.unitservice.middleware.AuthenticationService;
import org.opengroup.osdu.unitservice.util.AppError;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig implements AccessDeniedHandler, AuthenticationEntryPoint {

    private AuthenticationRequestFilter authFilter;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String[] AUTH_WHITELIST = {
            "/",
            "/v2/api-docs",
            "/v3/api-docs",
            "/swagger",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v3/info",
            "/webjars/**",
            "/_ah/**",
            "/actuator/**",
            "/error",
            "/favicon.ico",
            "/csrf",
            "/api/unit",
            "/api/unit/v2/api-docs",
            "/api/unit/v3/api-docs",
            "/api/unit/swagger-resources/**",
            "/api/unit/swagger-ui.html",
            "/api/unit/webjars/**",
            "/api/unit/_ah/**",
            "/api/unit/error",
            "/api/unit/favicon.ico",
            "/api/unit/actuator",
            "/api/unit/actuator/**",
            "/api/unit/actuator/health",
            "/api/unit/csrf" // Required to prevent errors in logs while Swagger is trying to discover a valid csrf token. Should be deleted after the issue on the Swagger's side https://github.com/springfox/springfox/issues/2578 is resolved
    };

    //AuthenticationRequestFilter is not a recognized bean, so construct it manually
    public SecurityConfig(AuthenticationService authenticationService) {
        authFilter = new AuthenticationRequestFilter(authenticationService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Spring Security 7 removed authorizeRequests(); requests stay permitted at this layer as before,
        // authentication is enforced by AuthenticationRequestFilter.
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.NEVER))
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                .addFilterBefore((Filter) authFilter, (Class<? extends Filter>) UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(AUTH_WHITELIST);
    }

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException {
        writeUnauthorizedError(httpServletResponse);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        writeUnauthorizedError(response);
    }

    private static void writeUnauthorizedError(HttpServletResponse response) throws IOException {
        AppError appError = AppError.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .message("The user is not authorized to perform this action")
                .reason("Unauthorized")
                .build();
        String body = OBJECT_MAPPER.writeValueAsString(appError);

        PrintWriter out = response.getWriter();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(body);
        out.flush();
    }
}
