package org.opengroup.osdu.unitservice.middleware;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.opengroup.osdu.core.common.http.ResponseHeadersFactory;
import org.opengroup.osdu.core.common.model.http.DpsHeaders;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UnitFilterTest {

    @Mock
    private DpsHeaders dpsHeaders;

    @Mock
    private ResponseHeadersFactory responseHeadersFactory;

    @Mock
    private FilterChain filterChain;

    @Mock
    private ServletRequest servletRequest;

    @Mock
    private HttpServletResponse httpServletResponse;

    @InjectMocks
    private UnitFilter unitFilter;

    @BeforeEach
    public void setUp() {
        unitFilter.ACCESS_CONTROL_ALLOW_ORIGIN_DOMAINS = "*";
    }

    @Test
    public void should_AddHeadersIn_doFilter() throws IOException, ServletException {
        // Arrange
        when(responseHeadersFactory.getResponseHeaders("*")).thenReturn(Map.of("Custom-Header", "Value"));
        when(dpsHeaders.getCorrelationId()).thenReturn("correlation-id");

        // Act
        unitFilter.doFilter(servletRequest, httpServletResponse, filterChain);

        // Assert
        verify(filterChain).doFilter(servletRequest, httpServletResponse);
        verify(dpsHeaders).addCorrelationIdIfMissing();
        verify(httpServletResponse).setHeader("Custom-Header", "Value");
        verify(httpServletResponse).addHeader(DpsHeaders.CORRELATION_ID, "correlation-id");
    }

    @AfterEach
    public void tearDown() {
        unitFilter.destroy();
    }

}
