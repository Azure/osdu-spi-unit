package org.opengroup.osdu.unitservice.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.opengroup.osdu.core.common.openapi.OpenApiContractProperties;
import org.opengroup.osdu.core.common.openapi.springdoc.DataPartitionHeaderOperationCustomizer;
import org.opengroup.osdu.unitservice.api.HealthCheck;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.ObjectProvider;
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
    public GroupedOpenApi apiV2(ObjectProvider<OpenApiContractProperties> contractProperties) {
        String[] paths = {"/v2/**"};
        return GroupedOpenApi.builder()
                .group("v2")
                .pathsToExclude("/api/unit/error")
                .pathsToMatch(paths)
                .addOpenApiCustomizer(buildV2OpenAPI())
                .addOperationCustomizer(dataPartitionHeaderCustomizer(contractProperties))
                .addOperationCustomizer(unsupportedMediaTypeResponse())
                .addOperationCustomizer(emptyHealthResponse())
                .addOperationCustomizer(problemDetailResponse())
                .addOpenApiCustomizer(problemDetailSchema())
                .build();
    }

    @Bean
    public GroupedOpenApi apiV3(ObjectProvider<OpenApiContractProperties> contractProperties) {
        String[] paths = {"/v3/**"};
        return GroupedOpenApi.builder()
                .group("v3")
                .pathsToExclude("/api/unit/error")
                .pathsToMatch(paths)
                .packagesToScan("org.opengroup.osdu.unitservice")
                .addOpenApiCustomizer(buildV3OpenAPI())
                .addOperationCustomizer(dataPartitionHeaderCustomizer(contractProperties))
                .addOperationCustomizer(unsupportedMediaTypeResponse())
                .addOperationCustomizer(emptyHealthResponse())
                .addOperationCustomizer(problemDetailResponse())
                .addOpenApiCustomizer(problemDetailSchema())
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

    private static OperationCustomizer dataPartitionHeaderCustomizer(
            ObjectProvider<OpenApiContractProperties> contractProperties) {
        return DataPartitionHeaderOperationCustomizer.create(contractProperties.getIfAvailable());
    }

    /**
     * Probes return an empty body. Springdoc still documents {@code application/json} string
     * for a raw {@code ResponseEntity}, which makes Schemathesis require a JSON body.
     * Same outcome as file: 200 with no content schema.
     */
    static OperationCustomizer emptyHealthResponse() {
        return (operation, handlerMethod) -> {
            if (!HealthCheck.class.equals(handlerMethod.getBeanType())) {
                return operation;
            }
            ApiResponses responses = operation.getResponses();
            if (responses == null) {
                return operation;
            }
            ApiResponse ok = responses.get("200");
            if (ok != null) {
                ok.setContent(null);
            }
            return operation;
        };
    }

    /**
     * POST bodies without a Content-Type return 415. Document that status as AppError JSON,
     * matching storage and indexer.
     */
    static OperationCustomizer unsupportedMediaTypeResponse() {
        return (operation, handlerMethod) -> {
            if (operation.getRequestBody() == null) {
                return operation;
            }
            ApiResponses responses = operation.getResponses();
            if (responses == null) {
                responses = new ApiResponses();
                operation.setResponses(responses);
            }
            if (responses.containsKey("415")) {
                return operation;
            }
            Schema<?> appError = new Schema<>().$ref("#/components/schemas/AppError");
            MediaType json = new MediaType().schema(appError);
            responses.addApiResponse("415", new ApiResponse()
                    .description("Unsupported Media Type")
                    .content(new Content().addMediaType("application/json", json)));
            return operation;
        };
    }

    /**
     * Spring rejects some requests with RFC 7807 {@code application/problem+json}
     * (overflowing query numbers, unreadable bodies) before the AppError mapper runs.
     */
    static OperationCustomizer problemDetailResponse() {
        return (operation, handlerMethod) -> {
            ApiResponses responses = operation.getResponses();
            if (responses == null) {
                return operation;
            }
            responses.forEach((code, response) -> {
                if (code == null || code.isEmpty() || code.charAt(0) < '4' || code.charAt(0) > '5') {
                    return;
                }
                Content content = response.getContent();
                if (content == null) {
                    content = new Content();
                    response.setContent(content);
                }
                if (!content.containsKey("application/problem+json")) {
                    content.addMediaType("application/problem+json",
                            new MediaType().schema(new Schema<>().$ref("#/components/schemas/ProblemDetail")));
                }
            });
            return operation;
        };
    }

    static OpenApiCustomizer problemDetailSchema() {
        return openApi -> {
            Components components = openApi.getComponents();
            if (components == null) {
                components = new Components();
                openApi.setComponents(components);
            }
            if (components.getSchemas() != null && components.getSchemas().containsKey("ProblemDetail")) {
                return;
            }
            Schema<Object> problem = new Schema<>();
            problem.setType("object");
            problem.setDescription("RFC 7807 problem detail returned when request parsing fails before the service error body.");
            problem.addProperty("type", stringSchema());
            problem.addProperty("title", stringSchema());
            problem.addProperty("status", integerSchema());
            problem.addProperty("detail", stringSchema());
            problem.addProperty("instance", stringSchema());
            components.addSchemas("ProblemDetail", problem);
        };
    }

    private static Schema<String> stringSchema() {
        Schema<String> schema = new Schema<>();
        schema.setType("string");
        return schema;
    }

    private static Schema<Integer> integerSchema() {
        Schema<Integer> schema = new Schema<>();
        schema.setType("integer");
        schema.setFormat("int32");
        return schema;
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
