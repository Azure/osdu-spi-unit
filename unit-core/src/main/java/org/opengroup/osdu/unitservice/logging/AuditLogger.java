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

import java.util.List;
import java.util.Objects;
import org.opengroup.osdu.core.common.logging.JaxRsDpsLog;
import org.opengroup.osdu.core.common.logging.audit.AuditPayload;
import org.opengroup.osdu.core.common.model.entitlements.Groups;
import org.opengroup.osdu.core.common.model.http.DpsHeaders;
import org.opengroup.osdu.core.common.util.IpAddressUtil;
import org.opengroup.osdu.unitservice.constant.UnitServiceRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import jakarta.servlet.http.HttpServletRequest;

@Component
@RequestScope
public class AuditLogger {

  private JaxRsDpsLog logger;
  private DpsHeaders dpsHeaders;
  private AuditEvents events = null;
  private final HttpServletRequest httpRequest;

  public AuditLogger(JaxRsDpsLog logger, DpsHeaders dpsHeaders, HttpServletRequest httpRequest) {
    this.logger = logger;
    this.dpsHeaders = dpsHeaders;
    this.httpRequest = httpRequest;
  }

  private AuditEvents getAuditEvents() {
    String userIpAddress = IpAddressUtil.getClientIpAddress(this.httpRequest);
    String userAgent = httpRequest.getHeader("user-agent");

    if (this.events == null) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (Objects.nonNull(authentication) && authentication.getPrincipal() instanceof Groups) {
        Groups groups = (Groups) authentication.getPrincipal();
        this.events = new AuditEvents(groups.getMemberEmail(), userIpAddress, userAgent, UnitServiceRole.UNIT_AUTHENTICATED_USER);
      } else {
        this.events = new AuditEvents(this.dpsHeaders.getUserEmail(), userIpAddress, userAgent, UnitServiceRole.UNIT_AUTHENTICATED_USER);
      }
    }
    return this.events;
  }

  private void writeLog(AuditPayload log) {
    this.logger.audit(log);
  }

  public void readCatalogSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    this.writeLog(this.getAuditEvents().getReadCatalogEventSuccess(resources, requiredGroupsForAction));
  }

  public void readCatalogLastModifiedSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    this.writeLog(this.getAuditEvents().getReadCatalogLastModifiedSuccess(resources, requiredGroupsForAction));

  }

  public void readMeasurementsSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    this.writeLog(this.getAuditEvents().getReadMeasurementsSuccess(resources, requiredGroupsForAction));
  }

  public void readSpecificMeasurementSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    this.writeLog(this.getAuditEvents().getReadSpecificMeasurementSuccess(resources, requiredGroupsForAction));
  }

  public void readUnitsSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    this.writeLog(this.getAuditEvents().getReadUnitsSuccess(resources, requiredGroupsForAction));
  }

  public void readUnitEssenceSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    this.writeLog(this.getAuditEvents().getReadUnitEssenceSuccess(resources, requiredGroupsForAction));
  }

  public void readUnitsByUnitSymbolSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    this.writeLog(this.getAuditEvents().getReadUnitsByUnitSymbolSuccess(resources, requiredGroupsForAction));
  }

  public void readUnitByUnitSymbolSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    this.writeLog(this.getAuditEvents().getReadUnitByUnitSymbolSuccess(resources, requiredGroupsForAction));
  }

  public void readUnitByMeasurementSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    this.writeLog(this.getAuditEvents().getReadUnitByMeasurementSuccess(resources, requiredGroupsForAction));
  }

  public void readUnitsByMeasurementSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    this.writeLog(this.getAuditEvents().getReadUnitsByMeasurementSuccess(resources, requiredGroupsForAction));
  }

  public void readPreferredUnitsByMeasurementSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    this.writeLog(this.getAuditEvents().getReadPreferredUnitsByMeasurementSuccess(resources, requiredGroupsForAction));
  }

  public void readUnitSystemSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    this.writeLog(this.getAuditEvents().getReadUnitSystemSuccess(resources, requiredGroupsForAction));
  }

  public void searchMeasurementsByKeyword(List<String> resources, List<String> requiredGroupsForAction) {
    this.writeLog(this.getAuditEvents().getSearchMeasurementsByKeyword(resources, requiredGroupsForAction));
  }

  public void getUnitMapsSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    this.writeLog(this.getAuditEvents().getUnitMapsSuccess(resources, requiredGroupsForAction));
  }

  public void getMeasurementMapsSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    this.writeLog(this.getAuditEvents().getMeasurementMapsSuccess(resources, requiredGroupsForAction));
  }

  public void getMapStatesSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    this.writeLog(this.getAuditEvents().getMapStatesSuccess(resources, requiredGroupsForAction));
  }

  public void readConversionABCDBySymbolsSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    this.writeLog(this.getAuditEvents().getReadConversionABCDBySymbolsSuccess(resources, requiredGroupsForAction));
  }
}
