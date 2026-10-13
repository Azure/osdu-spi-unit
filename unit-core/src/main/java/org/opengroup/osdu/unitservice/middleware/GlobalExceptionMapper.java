package org.opengroup.osdu.unitservice.middleware;

import org.apache.http.HttpStatus;
import org.opengroup.osdu.core.common.logging.JaxRsDpsLog;
import org.opengroup.osdu.core.common.model.entitlements.EntitlementsException;
import org.opengroup.osdu.unitservice.util.AppError;
import org.opengroup.osdu.unitservice.util.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.resolve;

@RestControllerAdvice
public class GlobalExceptionMapper extends ResponseEntityExceptionHandler {

    @Autowired
    private JaxRsDpsLog jaxRsDpsLog;

    @ExceptionHandler(IOException.class)
    protected ResponseEntity<AppError> handleIOException(IOException e) {
        return this.getErrorResponse(new AppException(INTERNAL_SERVER_ERROR.value(),
                "IOException", e.getMessage()));
    }

    @ExceptionHandler(EntitlementsException.class)
    protected ResponseEntity<AppError> handleEntitlementsException(EntitlementsException e) {
        return this.getErrorResponse(new AppException(e.getHttpResponse().getResponseCode(),
                "EntitlementsException", e.getMessage()));
    }

    @ExceptionHandler(AppException.class)
    protected ResponseEntity<AppError> handleAppException(AppException e) {
        return this.getErrorResponse(e);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<AppError> handleGeneralException(Exception e) {
        return this.getErrorResponse(
                new AppException(INTERNAL_SERVER_ERROR.value(), "Server error.",
                        "An unknown error has occurred.", e));
    }

    private ResponseEntity<AppError> getErrorResponse(AppException e) {
        jaxRsDpsLog.error(e.getError().getMessage(), e);
        AppError appError = e.getError();
        return new ResponseEntity<>(appError, resolve(appError.getCode()));
    }
}
