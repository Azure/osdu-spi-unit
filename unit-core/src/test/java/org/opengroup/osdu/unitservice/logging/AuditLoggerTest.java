/*
 * Copyright 2021 Google LLC
 * Copyright 2021 EPAM Systems, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opengroup.osdu.unitservice.logging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.opengroup.osdu.core.common.logging.JaxRsDpsLog;
import org.opengroup.osdu.core.common.model.http.DpsHeaders;
import org.opengroup.osdu.unitservice.constant.UnitServiceRole;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuditLoggerTest {

  @Mock
  private JaxRsDpsLog log;

  @InjectMocks
  private AuditLogger sut;

  @Mock
  private DpsHeaders dpsHeaders;

  @Mock
  private HttpServletRequest httpRequest;

  private List<String> resource;
  private List<String> requiredGroupsForAction;

  @BeforeEach
  public void setup() {
    

    resource = Collections.singletonList("1");
    requiredGroupsForAction = Collections.singletonList(UnitServiceRole.UNIT_AUTHENTICATED_USER);
    lenient().when(dpsHeaders.getUserEmail()).thenReturn("user");
    lenient().when(httpRequest.getHeader("X-Forwarded-For")).thenReturn("127.0.0.0:1234");
    lenient().when(httpRequest.getHeader("user-agent")).thenReturn("testAgent");
  }

  @Test
  public void should_writeReadCatalogLastModifiedEvent() {
    this.sut.readCatalogLastModifiedSuccess(resource, requiredGroupsForAction);

    verify(this.log, times(1)).audit(any());
  }

  @Test
  public void should_writeReadCatalogLastModifiedEvent_whenIPv4XForwardedForIPHeaderIsPopulated() {
    when(httpRequest.getHeader("X-Forwarded-For")).thenReturn("111.111.111.111:1234");

    sut.readCatalogLastModifiedSuccess(resource, requiredGroupsForAction);

    verify(log, times(1)).audit(any());
  }

  @Test
  public void should_writeReadCatalogLastModifiedEvent_whenIPv4XForwardedForIPHeaderIsNotPopulated() {
    when(httpRequest.getHeader("X-Forwarded-For")).thenReturn(null);
    when(httpRequest.getRemoteAddr()).thenReturn("0.0.0.0:1234");

    sut.readCatalogLastModifiedSuccess(resource, requiredGroupsForAction);

    verify(log, times(1)).audit(any());
  }

  @Test
  public void should_writeReadCatalogLastModifiedEvent_whenIPv4IPHeadersContainMultipleIPs() {
    when(httpRequest.getHeader("X-Forwarded-For")).thenReturn("0.0.0.0:1234,0.0.0.1:1234");
    sut.readCatalogLastModifiedSuccess(resource, requiredGroupsForAction);

    verify(log, times(1)).audit(any());
  }

  @Test
  public void should_writeReadCatalogLastModifiedEvent_whenIPv6XForwardedForIPHeaderIsPopulated() {
    when(httpRequest.getHeader("X-Forwarded-For")).thenReturn("[0000:0000:0000:0000:0000:0000:0000:0000]:1234");

    sut.readCatalogLastModifiedSuccess(resource, requiredGroupsForAction);

    verify(log, times(1)).audit(any());
  }

  @Test
  public void should_writeReadCatalogLastModifiedEvent_whenIPv6XForwardedForIPHeaderIsNotPopulated() {
    when(httpRequest.getHeader("X-Forwarded-For")).thenReturn(null);
    when(httpRequest.getRemoteAddr()).thenReturn("[0000:0000:0000:0000:0000:0000:0000:0000]:1234");

    sut.readCatalogLastModifiedSuccess(resource, requiredGroupsForAction);

    verify(log, times(1)).audit(any());
  }

  @Test
  public void should_writeReadCatalogLastModifiedEvent_whenIPv6IPHeadersContainMultipleIPs() {
    when(httpRequest.getHeader("X-Forwarded-For")).thenReturn("[0000:0000:0000:0000:0000:0000:0000:0000]:1234,[0000:0000:0000:0000:0000:0000:0000:0001]:1234");
    sut.readCatalogLastModifiedSuccess(resource, requiredGroupsForAction);

    verify(log, times(1)).audit(any());
  }

  @Test
  public void should_writeReadCatalogEvent() {
    this.sut.readCatalogSuccess(resource, requiredGroupsForAction);

    verify(this.log, times(1)).audit(any());
  }

  @Test
  public void should_writeReadSpecificMeasurementEvent() {
    this.sut.readSpecificMeasurementSuccess(resource, requiredGroupsForAction);

    verify(this.log, times(1)).audit(any());
  }

  @Test
  public void should_writeReadUnitsSuccessEvent() {
    this.sut.readUnitsSuccess(resource, requiredGroupsForAction);

    verify(this.log, times(1)).audit(any());
  }

  @Test
  public void should_writeReadUnitEssenceEvent() {
    this.sut.readUnitEssenceSuccess(resource, requiredGroupsForAction);

    verify(this.log, times(1)).audit(any());
  }

  @Test
  public void should_writeReadUnitsByUnitSymbolEvent() {
    this.sut.readUnitsByUnitSymbolSuccess(resource, requiredGroupsForAction);

    verify(this.log, times(1)).audit(any());
  }

  @Test
  public void should_writeReadUnitByUnitSymbolEvent() {
    this.sut.readUnitByUnitSymbolSuccess(resource, requiredGroupsForAction);

    verify(this.log, times(1)).audit(any());
  }

  @Test
  public void should_writeReadUnitByMeasurementEvent() {
    this.sut.readUnitByMeasurementSuccess(resource, requiredGroupsForAction);

    verify(this.log, times(1)).audit(any());
  }

  @Test
  public void should_writeReadUnitsByMeasurementEvent() {
    this.sut.readUnitsByMeasurementSuccess(resource, requiredGroupsForAction);

    verify(this.log, times(1)).audit(any());
  }

  @Test
  public void should_writeReadMeasurementsEvent() {
    this.sut.readMeasurementsSuccess(resource, requiredGroupsForAction);

    verify(this.log, times(1)).audit(any());
  }

  @Test
  public void should_writeReadPreferredUnitsByMeasurementEvent() {
    this.sut.readPreferredUnitsByMeasurementSuccess(resource, requiredGroupsForAction);

    verify(this.log, times(1)).audit(any());
  }

  @Test
  public void should_writeReadUnitSystemEvent() {
    this.sut.readUnitSystemSuccess(resource, requiredGroupsForAction);

    verify(this.log, times(1)).audit(any());
  }

  @Test
  public void should_writeSearchMeasurementsByKeywordEvent() {
    this.sut.searchMeasurementsByKeyword(resource, requiredGroupsForAction);

    verify(this.log, times(1)).audit(any());
  }

  @Test
  public void should_writeGetUnitMapsEvent() {
    this.sut.getUnitMapsSuccess(resource, requiredGroupsForAction);

    verify(this.log, times(1)).audit(any());
  }

  @Test
  public void should_writeGetMeasurementMapsEvent() {
    this.sut.getMeasurementMapsSuccess(resource, requiredGroupsForAction);

    verify(this.log, times(1)).audit(any());
  }

  @Test
  public void should_throwIllegalArgumentException_whenUserIpAddressIsNull() {
    lenient().when(httpRequest.getHeader("X-Forwarded-For")).thenReturn(null);
    lenient().when(httpRequest.getHeader("X-Client-IP")).thenReturn(null);
    lenient().when(httpRequest.getRemoteAddr()).thenReturn(null);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      sut.getMeasurementMapsSuccess(resource, requiredGroupsForAction);
    });
    assertNotNull(exception);
    assertEquals("User's IP address is not provided for audit events.", exception.getMessage());
  }

  @Test
  public void should_throwIllegalArgumentException_whenUserAgentIsNull() {
    when(httpRequest.getHeader("user-agent")).thenReturn(null);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      sut.getMeasurementMapsSuccess(resource, requiredGroupsForAction);
    });
    assertNotNull(exception);
    assertEquals("User's agent is not provided for audit events.", exception.getMessage());
  }

  @Test
  public void should_writeGetMapStateEvent() {
    this.sut.getMapStatesSuccess(resource, requiredGroupsForAction);

    verify(this.log, times(1)).audit(any());
  }
}
