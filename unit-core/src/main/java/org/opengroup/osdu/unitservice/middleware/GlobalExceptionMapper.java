package org.opengroup.osdu.unitservice.middleware;

import org.opengroup.osdu.core.common.logging.JaxRsDpsLog;
import org.opengroup.osdu.core.common.model.entitlements.EntitlementsException;
import org.opengroup.osdu.unitservice.util.AppError;
import org.opengroup.osdu.unitservice.util.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionMapper extends ResponseEntityExceptionHandler {

    @Autowired
    private JaxRsDpsLog jaxRsDpsLog;

    @ExceptionHandler(IOException.class)
    protected ResponseEntity<Object> handleIOException(IOException e) {
        return this.getErrorResponse(new AppException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "IOException", e.getMessage()));
    }

    @ExceptionHandler(EntitlementsException.class)
    protected ResponseEntity<Object> handleEntitlementsException(EntitlementsException e) {
        return this.getErrorResponse(new AppException(e.getHttpResponse().getResponseCode(),
                "EntitlementsException", e.getMessage()));
    }

    @ExceptionHandler(AppException.class)
    protected ResponseEntity<Object> handleAppException(AppException e) {
        return this.getErrorResponse(e);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleGeneralException(Exception e) {
        return this.getErrorResponse(
                new AppException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Server error.",
                        "An unknown error has occurred.", e));
    }

    /**
     * Query/path conversion failures (for example {@code offset=2147483648}). Without this
     * override Spring returns RFC 7807 {@code application/problem+json}. Same pattern as storage.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        String errorMessage = String.format("Invalid value '%s' for parameter '%s'", e.getValue(), e.getName());
        return this.getErrorResponse(
                new AppException(HttpStatus.BAD_REQUEST.value(), "Bad Request", errorMessage, e));
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            @NonNull MissingServletRequestParameterException e,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        return this.getErrorResponse(
                new AppException(HttpStatus.BAD_REQUEST.value(), "Bad Request", e.getMessage(), e));
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            @NonNull HttpMediaTypeNotSupportedException e,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        return this.getErrorResponse(
                new AppException(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "Unsupported media type.",
                        e.getMessage(), e));
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            @NonNull HttpMessageNotReadableException e,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        return this.getErrorResponse(
                new AppException(HttpStatus.BAD_REQUEST.value(), "Bad Request", e.getMessage(), e));
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            @NonNull HttpRequestMethodNotSupportedException e,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        return this.getErrorResponse(
                new AppException(HttpStatus.METHOD_NOT_ALLOWED.value(), "Method not found.",
                        "Method not found.", e));
    }

    private ResponseEntity<Object> getErrorResponse(AppException e) {
        jaxRsDpsLog.error(e.getError().getMessage(), e);
        AppError appError = e.getError();
        return new ResponseEntity<>(appError, HttpStatus.resolve(appError.getCode()));
    }
}
