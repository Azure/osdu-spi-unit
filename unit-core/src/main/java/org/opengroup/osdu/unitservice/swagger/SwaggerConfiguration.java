package org.opengroup.osdu.unitservice.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.opengroup.osdu.core.common.model.http.DpsHeaders;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Collections;

import jakarta.servlet.ServletContext;

@Configuration
@Profile("!noswagger")
public class SwaggerConfiguration {

    @Autowired
    private SwaggerConfigurationProperties configurationProperties;

    @Bean
    public GroupedOpenApi apiV2() {
        String[] paths = {"/v2/**"};
        return GroupedOpenApi.builder()
                .group("v2")
                .pathsToExclude("/api/unit/error")
                .pathsToMatch(paths)
                .addOpenApiCustomizer(buildV2OpenAPI())
                .addOperationCustomizer(customize())
                .build();
    }

    @Bean
    public GroupedOpenApi apiV3() {
        String[] paths = {"/v3/**"};
        return GroupedOpenApi.builder()
                .group("v3")
                .pathsToExclude("/api/unit/error")
                .pathsToMatch(paths)
                .packagesToScan("org.opengroup.osdu.unitservice")
                .addOpenApiCustomizer(buildV3OpenAPI())
                .addOperationCustomizer(customize())
                .build();
    }

    @Bean
    public OpenAPI openApi(ServletContext servletContext) {
        Server server = new Server().url(servletContext.getContextPath());
        OpenAPI openAPI = new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("Authorization",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("Authorization")
                                        .in(SecurityScheme.In.HEADER)
                                        .name("Authorization")))
                .info(apiInfo())
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList("Authorization"));
        if (configurationProperties.isApiServerFullUrlEnabled())
            return openAPI;
        return openAPI
                .servers(Collections.singletonList(server));

    }

    @Bean
    public OperationCustomizer customize() {
        return (operation, handlerMethod) -> {
            operation.addParametersItem(
                    new Parameter()
                            .in("header")
                            .required(true)
                            .description("Tenant Id")
                            .name(DpsHeaders.DATA_PARTITION_ID));
            return operation;
        };
    }

    public OpenApiCustomizer buildV2OpenAPI() {
        return openApi -> {
            openApi.info(openApi.getInfo().version("2.0.0"));
            openApi.addTagsItem(new Tag().name("UoM Catalog (DEPRECATED)").description("UoM Catalog endpoints are <b>DEPRECATED</b>"));
            openApi.addTagsItem(new Tag().name("Measurements (DEPRECATED)").description("Measurements endpoints are <b>DEPRECATED</b>"));
            openApi.addTagsItem(new Tag().name("Units (DEPRECATED)").description("Units endpoints are <b>DEPRECATED</b>"));
            openApi.addTagsItem(new Tag().name("Unit Systems (DEPRECATED)").description("Unit Systems endpoints are <b>DEPRECATED</b>"));
            openApi.addTagsItem(new Tag().name("Conversions (DEPRECATED)").description("Conversions endpoints are <b>DEPRECATED</b>"));


        };
    }

    public OpenApiCustomizer buildV3OpenAPI() {
        return openApi -> {
            openApi.info(openApi.getInfo().version("3.0.0"));
            openApi.addTagsItem(new Tag().name("info-api-v3").description("Version info endpoint"));
            openApi.addTagsItem(new Tag().name("health-check-api").description("Health related API"));
            openApi.addTagsItem(new Tag().name("unit-api-v3").description("Unit related API"));
        };
    }

    private Info apiInfo() {
        return new Info()
                .title(configurationProperties.getApiTitle())
                .description(configurationProperties.getApiDescription())
                .license(new License().name(configurationProperties.getApiLicenseName()).url(configurationProperties.getApiLicenseUrl()))
                .contact(new Contact().name(configurationProperties.getApiContactName()).email(configurationProperties.getApiContactEmail()));
    }
}
