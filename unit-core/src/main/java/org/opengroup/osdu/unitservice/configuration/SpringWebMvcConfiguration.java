package org.opengroup.osdu.unitservice.configuration;

import org.springframework.boot.http.converter.autoconfigure.ServerHttpMessageConvertersCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageConverters.ServerBuilder;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class SpringWebMvcConfiguration implements WebMvcConfigurer {

    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        firewall.setAllowUrlEncodedPercent(true);
        return firewall;
    }

    /**
     * Swaps the MVC JSON converter for one backed by a strict copy of the shared mapper. Registered as a
     * customizer, not as an {@code HttpMessageConverter} bean: Boot 4 puts converter beans ahead of the
     * String converter, which would JSON-quote String responses. Ordered after Boot's own Jackson
     * customizer ({@code @Order(0)}) so this converter wins.
     */
    @Bean
    @Order(1)
    public StrictJsonConverterCustomizer strictJsonConverterCustomizer(JsonMapper jsonMapper) {
        return new StrictJsonConverterCustomizer(JacksonConfiguration.strictCopyOf(jsonMapper));
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        urlPathHelper.setUrlDecode(false);
        configurer.setUrlPathHelper(urlPathHelper);
    }

    public static final class StrictJsonConverterCustomizer implements ServerHttpMessageConvertersCustomizer {

        private final JsonMapper jsonMapper;

        StrictJsonConverterCustomizer(JsonMapper jsonMapper) {
            this.jsonMapper = jsonMapper;
        }

        public JsonMapper getJsonMapper() {
            return jsonMapper;
        }

        @Override
        public void customize(ServerBuilder builder) {
            builder.withJsonConverter(new JacksonJsonHttpMessageConverter(jsonMapper));
        }
    }
}
