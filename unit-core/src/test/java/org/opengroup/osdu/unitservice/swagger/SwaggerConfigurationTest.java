package org.opengroup.osdu.unitservice.swagger;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.junit.jupiter.api.Test;
import org.opengroup.osdu.unitservice.api.HealthCheck;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class SwaggerConfigurationTest {

    @Test
    public void healthCheckDropsTheDocumentedJsonBody() throws Exception {
        Operation operation = new Operation().responses(new ApiResponses().addApiResponse("200",
                new ApiResponse().description("OK").content(new Content().addMediaType("application/json", new MediaType()))));
        HandlerMethod handler = new HandlerMethod(new HealthCheck(), HealthCheck.class.getMethod("livenessCheck"));

        Operation customized = SwaggerConfiguration.emptyHealthResponse().customize(operation, handler);

        assertNull(customized.getResponses().get("200").getContent());
    }

    @Test
    public void nonHealthOperationKeepsItsResponseBody() throws Exception {
        Operation operation = new Operation().responses(new ApiResponses().addApiResponse("200",
                new ApiResponse().content(new Content())));
        HandlerMethod handler = new HandlerMethod(new Sample(), Sample.class.getMethod("post", String.class));

        Operation customized = SwaggerConfiguration.emptyHealthResponse().customize(operation, handler);

        assertSame(operation, customized);
        assertNotNull(customized.getResponses().get("200").getContent());
    }

    @Test
    public void postOperationDocumentsUnsupportedMediaType() throws Exception {
        Method method = Sample.class.getMethod("post", String.class);
        Operation operation = new Operation().requestBody(new RequestBody()).responses(new ApiResponses());

        Operation customized = SwaggerConfiguration.unsupportedMediaTypeResponse()
                .customize(operation, new HandlerMethod(new Sample(), method));

        ApiResponse unsupported = customized.getResponses().get("415");
        assertNotNull(unsupported);
        assertNotNull(unsupported.getContent().get("application/json").getSchema().get$ref());
    }

    @Test
    public void errorResponsesDocumentProblemJson() throws Exception {
        Operation operation = new Operation().responses(new ApiResponses()
                .addApiResponse("200", new ApiResponse().content(new Content().addMediaType("application/json", new MediaType())))
                .addApiResponse("400", new ApiResponse().description("Bad Request")
                        .content(new Content().addMediaType("application/json", new MediaType()))));
        HandlerMethod handler = new HandlerMethod(new Sample(), Sample.class.getMethod("post", String.class));

        Operation customized = SwaggerConfiguration.problemDetailResponse().customize(operation, handler);

        assertNull(customized.getResponses().get("200").getContent().get("application/problem+json"));
        assertNotNull(customized.getResponses().get("400").getContent().get("application/problem+json").getSchema().get$ref());
    }

    static class Sample {
        @SuppressWarnings("unused")
        public void post(@org.springframework.web.bind.annotation.RequestBody String body) {
        }
    }
}
