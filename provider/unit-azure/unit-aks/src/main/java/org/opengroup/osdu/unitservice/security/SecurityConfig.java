package org.opengroup.osdu.unitservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.opengroup.osdu.unitservice.middleware.AuthenticationRequestFilter;
import org.opengroup.osdu.unitservice.middleware.AuthenticationService;
import org.opengroup.osdu.unitservice.util.AppError;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig implements AccessDeniedHandler, AuthenticationEntryPoint {

    private AuthenticationRequestFilter authFilter;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String[] AUTH_WHITELIST = {
            "/",
            "/v2/api-docs",
            "/v3/api-docs",
            "/swagger",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/api-docs.yaml",
            "/actuator/*",
            "/v3/info",
            "/webjars/**",
            "/_ah/**",
            "/error",
            "/favicon.ico",
            "/csrf",
            "/api-docs/swagger-config",
            "/index.html",
            "/api-docs/**",
    };

    //AuthenticationRequestFilter is not a recognized bean, so construct it manually
    public SecurityConfig(AuthenticationService authenticationService) {
        authFilter = new AuthenticationRequestFilter(authenticationService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .and()
                .authorizeRequests()
                .requestMatchers(AUTH_WHITELIST).permitAll()
                .and()
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
