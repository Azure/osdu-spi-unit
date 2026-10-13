package org.opengroup.osdu.unitservice.util;

import lombok.ToString;
import org.apache.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ToString
public class AppException extends RuntimeException {

    private AppError error;

    public AppError getError() {
        return this.error;
    }

    public AppException(int status, String reason, String message) {
        this.error = AppError.builder().code(status).reason(reason).message(message).build();
    }

    public AppException(int status, String reason, String message, String debuggingInfo) {
        this.error = AppError.builder().code(status).reason(reason).message(message).debuggingInfo(debuggingInfo).build();
    }

    public AppException(int status, String reason, String message, Exception originalException) {
        this.error = AppError.builder().code(status).reason(reason).message(message).originalException(originalException).build();
    }

    public static AppException createUnauthorized(String debuggingInfo) {
        return new AppException(HttpStatus.SC_UNAUTHORIZED, "Unauthorized", "The user is not authorized to perform this action", debuggingInfo);
    }

    public static AppException createBadRequest(String message) {
        return new AppException(HttpStatus.SC_BAD_REQUEST, "Bad Request", message);
    }
}
