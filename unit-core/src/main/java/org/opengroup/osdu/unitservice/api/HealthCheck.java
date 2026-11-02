package org.opengroup.osdu.unitservice.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.opengroup.osdu.core.common.model.http.AppError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/_ah","v3/_ah"})
public class HealthCheck {

	@Operation(summary = "${healthCheckApi.livenessCheck.summary}",
			description = "${healthCheckApi.livenessCheck.description}", tags = { "health-check-api" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "OK"),
			@ApiResponse(responseCode = "400", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "403", description = "Forbidden",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
	})
	@SecurityRequirements
	@GetMapping("/liveness_check")
	public ResponseEntity<Void> livenessCheck() {
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "${healthCheckApi.readinessCheck.summary}",
			description = "${healthCheckApi.readinessCheck.description}", tags = { "health-check-api" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "OK"),
			@ApiResponse(responseCode = "400", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "403", description = "Forbidden",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
	})
	@SecurityRequirements
	@GetMapping("/readiness_check")
	public ResponseEntity<Void> readinessCheck() {
		return ResponseEntity.ok().build();
	}
}
