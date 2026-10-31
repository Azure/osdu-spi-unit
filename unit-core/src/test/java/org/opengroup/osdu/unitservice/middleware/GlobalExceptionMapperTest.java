package org.opengroup.osdu.unitservice.middleware;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.opengroup.osdu.core.common.logging.JaxRsDpsLog;
import org.opengroup.osdu.unitservice.util.AppError;
import org.opengroup.osdu.unitservice.util.AppException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.io.IOException;
import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionMapperTest {

    @InjectMocks
    private GlobalExceptionMapper globalExceptionMapper;

    @Mock
    private JaxRsDpsLog jaxRsDpsLog;

    @Test
    public void should_useValuesInAppExceptionInResponse_When_AppExceptionIsHandledByGlobalExceptionMapper() throws Exception {
        AppException exception = new AppException(409, "any reason", "any message");

        ResponseEntity<Object> response = globalExceptionMapper.handleAppException(exception);
        AppError body = (AppError) response.getBody();
        assertNotNull(body);
        assertEquals(409, body.getCode());
        assertEquals("any message", body.getMessage());
        assertEquals("any reason", body.getReason());
        Mockito.verify(jaxRsDpsLog).error(exception.getError().getMessage(), exception);
    }

    @Test
    public void should_useGenericValuesInResponse_When_ExceptionIsHandledByGlobalExceptionMapper() {
        Exception exception = new Exception("any message");

        ResponseEntity<Object> response = globalExceptionMapper.handleGeneralException(exception);
        AppError body = (AppError) response.getBody();
        assertNotNull(body);
        assertEquals(500, body.getCode());
        assertEquals("An unknown error has occurred.", body.getMessage());
        assertEquals("Server error.", body.getReason());
        Mockito.verify(jaxRsDpsLog).error(Mockito.eq("An unknown error has occurred."), Mockito.any(AppException.class));
    }

    @Test
    public void shouldReturnBadRequest() {
        AppException exception = AppException.createBadRequest("Bad request");

        ResponseEntity<Object> response = globalExceptionMapper.handleAppException(exception);
        AppError body = (AppError) response.getBody();
        assertNotNull(body);
        assertEquals(400, body.getCode());
        assertEquals("Bad request", body.getMessage());
        assertEquals("Bad Request", body.getReason());
        Mockito.verify(jaxRsDpsLog).error(exception.getError().getMessage(), exception);
    }

    @Test
    public void should_handleIOException_andReturnInternalServerError() {
        IOException exception = new IOException("IO error");

        ResponseEntity<Object> response = globalExceptionMapper.handleIOException(exception);
        AppError body = (AppError) response.getBody();
        assertNotNull(body);
        assertEquals(INTERNAL_SERVER_ERROR.value(), body.getCode());
        assertEquals("IO error", body.getMessage());
        assertEquals("IOException", body.getReason());
        Mockito.verify(jaxRsDpsLog).error(Mockito.eq("IO error"), Mockito.any(AppException.class));
    }

    @Test
    public void should_rejectTypeMismatchAsBadRequest() {
        MethodArgumentTypeMismatchException exception = new MethodArgumentTypeMismatchException(
                "2147483648", Integer.class, "offset", null, new NumberFormatException("overflow"));

        ResponseEntity<Object> response = globalExceptionMapper.handleMethodArgumentTypeMismatch(exception);
        AppError body = (AppError) response.getBody();
        assertNotNull(body);
        assertEquals(400, body.getCode());
        assertEquals("Invalid value '2147483648' for parameter 'offset'", body.getMessage());
    }

    @Test
    public void should_rejectMissingParameterAsBadRequest() {
        MissingServletRequestParameterException exception =
                new MissingServletRequestParameterException("ancestry", "String");

        ResponseEntity<Object> response = globalExceptionMapper.handleMissingServletRequestParameter(
                exception, new HttpHeaders(), HttpStatus.BAD_REQUEST, Mockito.mock(WebRequest.class));
        AppError body = (AppError) response.getBody();
        assertNotNull(body);
        assertEquals(400, body.getCode());
        assertEquals(exception.getMessage(), body.getMessage());
    }

    @Test
    public void should_rejectUnsupportedMediaType() {
        HttpMediaTypeNotSupportedException exception = new HttpMediaTypeNotSupportedException("no content type");

        ResponseEntity<Object> response = globalExceptionMapper.handleHttpMediaTypeNotSupported(
                exception, new HttpHeaders(), HttpStatus.UNSUPPORTED_MEDIA_TYPE, Mockito.mock(WebRequest.class));
        AppError body = (AppError) response.getBody();
        assertNotNull(body);
        assertEquals(415, body.getCode());
        assertEquals("Unsupported media type.", body.getReason());
        assertEquals("no content type", body.getMessage());
    }

    @Test
    public void should_rejectUnreadableBodyAsBadRequest() {
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException(
                "JSON parse error", new HttpInputMessage() {
                    @Override
                    public InputStream getBody() {
                        return InputStream.nullInputStream();
                    }

                    @Override
                    public HttpHeaders getHeaders() {
                        return new HttpHeaders();
                    }
                });

        ResponseEntity<Object> response = globalExceptionMapper.handleHttpMessageNotReadable(
                exception, new HttpHeaders(), HttpStatus.BAD_REQUEST, Mockito.mock(WebRequest.class));
        AppError body = (AppError) response.getBody();
        assertNotNull(body);
        assertEquals(400, body.getCode());
        assertEquals("JSON parse error", body.getMessage());
    }

    @Test
    public void should_rejectUnsupportedMethod() {
        HttpRequestMethodNotSupportedException exception = new HttpRequestMethodNotSupportedException("TRACE");

        ResponseEntity<Object> response = globalExceptionMapper.handleHttpRequestMethodNotSupported(
                exception, new HttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED, Mockito.mock(WebRequest.class));
        AppError body = (AppError) response.getBody();
        assertNotNull(body);
        assertEquals(405, body.getCode());
        assertEquals("Method not found.", body.getMessage());
        assertEquals("Method not found.", body.getReason());
    }

}
