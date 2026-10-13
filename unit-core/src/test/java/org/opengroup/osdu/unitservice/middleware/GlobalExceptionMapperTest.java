package org.opengroup.osdu.unitservice.middleware;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.opengroup.osdu.core.common.logging.JaxRsDpsLog;
import org.opengroup.osdu.unitservice.util.AppError;
import org.opengroup.osdu.unitservice.util.AppException;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import java.io.IOException;
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

        ResponseEntity<AppError> response = globalExceptionMapper.handleAppException(exception);
        AppError body = response.getBody();
        assertNotNull(body);
        assertEquals(409, body.getCode());
        assertEquals("any message", body.getMessage());
        assertEquals("any reason", body.getReason());
        Mockito.verify(jaxRsDpsLog).error(exception.getError().getMessage(), exception);
    }

    @Test
    public void should_useGenericValuesInResponse_When_ExceptionIsHandledByGlobalExceptionMapper() {
        Exception exception = new Exception("any message");

        ResponseEntity<AppError> response = globalExceptionMapper.handleGeneralException(exception);
        AppError body = response.getBody();
        assertNotNull(body);
        assertEquals(500, body.getCode());
        assertEquals("An unknown error has occurred.", body.getMessage());
        assertEquals("Server error.", body.getReason());
        Mockito.verify(jaxRsDpsLog).error(Mockito.eq("An unknown error has occurred."), Mockito.any(AppException.class));
    }

    @Test
    public void shouldReturnBadRequest() {
        AppException exception = AppException.createBadRequest("Bad request");

        ResponseEntity<AppError> response = globalExceptionMapper.handleAppException(exception);
        AppError body = response.getBody();
        assertNotNull(body);
        assertEquals(400, body.getCode());
        assertEquals("Bad request", body.getMessage());
        assertEquals("Bad Request", body.getReason());
        Mockito.verify(jaxRsDpsLog).error(exception.getError().getMessage(), exception);
    }

    @Test
    public void should_handleIOException_andReturnInternalServerError() {
        IOException exception = new IOException("IO error");

        ResponseEntity<AppError> response = globalExceptionMapper.handleIOException(exception);
        AppError body = response.getBody();
        assertNotNull(body);
        assertEquals(INTERNAL_SERVER_ERROR.value(), body.getCode());
        assertEquals("IO error", body.getMessage());
        assertEquals("IOException", body.getReason());
        Mockito.verify(jaxRsDpsLog).error(Mockito.eq("IO error"), Mockito.any(AppException.class));
    }

}
