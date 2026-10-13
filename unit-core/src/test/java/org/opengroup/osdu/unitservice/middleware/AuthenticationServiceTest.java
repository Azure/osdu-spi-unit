package org.opengroup.osdu.unitservice.middleware;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.opengroup.osdu.core.common.entitlements.EntitlementsFactory;
import org.opengroup.osdu.core.common.entitlements.IEntitlementsService;
import org.opengroup.osdu.core.common.logging.JaxRsDpsLog;
import org.opengroup.osdu.core.common.model.entitlements.EntitlementsException;
import org.opengroup.osdu.core.common.model.entitlements.Groups;
import org.opengroup.osdu.core.common.model.http.DpsHeaders;
import org.opengroup.osdu.unitservice.util.AppException;

import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.Enumeration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private JaxRsDpsLog jaxRsDpsLog;

    @Mock
    private HandlerExceptionResolver handlerExceptionResolver;

    @BeforeEach
    public void init() {
        setField(authenticationService, "entitlementsUrl", "entitlementsUrl");
    }

    @Test
    public void shouldHandleEntitlementsException() {
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = Mockito.mock(HttpServletResponse.class);
        Enumeration<String> headerNames = Mockito.mock(Enumeration.class);
        Mockito.when(headerNames.hasMoreElements()).thenReturn(false);
        Mockito.when(httpServletRequest.getHeaderNames()).thenReturn(headerNames);

        authenticationService.initEntitlementsFactory();
        boolean result = authenticationService.isAuthorized(httpServletRequest, httpServletResponse);

        Assertions.assertFalse(result);
        Mockito.verify(jaxRsDpsLog)
            .error("User not authenticated. Response: HttpResponse(headers=null," +
                " body=, contentType=, responseCode=0, exception=org.apache.http.client.ClientProtocolException, "
                + "request=entitlementsUrl/groups, httpMethod=GET, latency=0)");
        Mockito.verify(handlerExceptionResolver).resolveException(Mockito.eq(httpServletRequest),
                Mockito.eq(httpServletResponse), Mockito.eq(null), Mockito.any(AppException.class));
    }

    @Test
    public void shouldHandleNPEFromEntitlementsService() throws EntitlementsException {
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = Mockito.mock(HttpServletResponse.class);
        Enumeration<String> headerNames = Mockito.mock(Enumeration.class);
        Mockito.when(headerNames.hasMoreElements()).thenReturn(false);
        Mockito.when(httpServletRequest.getHeaderNames()).thenReturn(headerNames);
        EntitlementsFactory entitlementsFactory = Mockito.mock(EntitlementsFactory.class);
        setField(authenticationService, "entitlementsFactory", entitlementsFactory);
        IEntitlementsService entitlementsService = Mockito.mock(IEntitlementsService.class);
        Mockito.when(entitlementsFactory.create(Mockito.any(DpsHeaders.class))).thenReturn(entitlementsService);
        Mockito.when(entitlementsService.getGroups()).thenThrow(new NullPointerException());

        boolean result = authenticationService.isAuthorized(httpServletRequest, httpServletResponse);

        Assertions.assertFalse(result);
        Mockito.verify(jaxRsDpsLog).error("User not authenticated. Null pointer exception: null");
        Mockito.verify(handlerExceptionResolver).resolveException(Mockito.eq(httpServletRequest),
                Mockito.eq(httpServletResponse), Mockito.eq(null), Mockito.any(AppException.class));
    }

    @Test
    public void shouldVerifyAuthenticationSuccessfully() throws EntitlementsException {
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = Mockito.mock(HttpServletResponse.class);
        Enumeration<String> headerNames = Mockito.mock(Enumeration.class);
        Mockito.when(headerNames.hasMoreElements()).thenReturn(false);
        Mockito.when(httpServletRequest.getHeaderNames()).thenReturn(headerNames);
        EntitlementsFactory entitlementsFactory = Mockito.mock(EntitlementsFactory.class);
        setField(authenticationService, "entitlementsFactory", entitlementsFactory);
        IEntitlementsService entitlementsService = Mockito.mock(IEntitlementsService.class);
        Mockito.when(entitlementsFactory.create(Mockito.any(DpsHeaders.class))).thenReturn(entitlementsService);
        Groups groups = new Groups();
        groups.setMemberEmail("email");
        Mockito.when(entitlementsService.getGroups()).thenReturn(groups);

        boolean result = authenticationService.isAuthorized(httpServletRequest, httpServletResponse);

        Assertions.assertTrue(result);
        Mockito.verify(jaxRsDpsLog).debug("User authenticated | User: email");
        Mockito.verifyNoMoreInteractions(handlerExceptionResolver);
    }

    private static void setField(Object target, String name, Object value) {
        try {
            Field field = AuthenticationService.class.getDeclaredField(name);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
