// Copyright 2017-2019, Schlumberger
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.opengroup.osdu.unitservice.logging;

import com.google.common.base.Strings;
import java.util.List;
import org.opengroup.osdu.core.common.logging.audit.AuditAction;
import org.opengroup.osdu.core.common.logging.audit.AuditPayload;
import org.opengroup.osdu.core.common.logging.audit.AuditStatus;

public class AuditEvents {

  private static final String READ_CATALOG_SUMMARY_ACTION_ID = "UN001";
  private static final String READ_CATALOG_SUMMARY_MESSAGE = "Read Catalog Summary.";
  private static final String READ_CATALOG_LAST_MODIFIED_ACTION_ID = "UN002";
  private static final String READ_CATALOG_LAST_MODIFIED_MESSAGE = "Read last modified date of the Catalog.";

  private static final String READ_MEASUREMENTS_ACTION_ID = "UN003";
  private static final String READ_MEASUREMENTS_MESSAGE = "Read Measurements.";
  private static final String READ_SPECIFIC_MEASUREMENT_ACTION_ID = "UN004";
  private static final String READ_SPECIFIC_MEASUREMENT_MESSAGE = "Read a specific Measurement.";

  private static final String READ_UNITS_ACTION_ID = "UN005";
  private static final String READ_UNITS_MESSAGE = "Read all Units.";
  private static final String READ_UNITS_BY_ESSENCE_ACTION_ID = "UN006";
  private static final String READ_UNITS_BY_ESSENCE_MESSAGE = "Read a single Unit given a persistable reference or essence.";
  private static final String READ_UNITS_BY_UNIT_SYMBOL_ACTION_ID = "UN007";
  private static final String READ_UNITS_BY_UNIT_SYMBOL_MESSAGE = "Read Units given a unit symbol.";
  private static final String READ_UNIT_BY_UNIT_SYMBOL_ACTION_ID = "UN008";
  private static final String READ_UNIT_BY_UNIT_SYMBOL_MESSAGE = "Read a single Unit given a namespace and a symbol.";
  private static final String READ_UNIT_BY_MEASUREMENT_ACTION_ID = "UN009";
  private static final String READ_UNIT_BY_MEASUREMENT_MESSAGE = "Read a single Unit given a Unit System name and a Measurement.";
  private static final String READ_UNITS_BY_MEASUREMENT_ACTION_ID = "UN010";
  private static final String READ_UNITS_BY_MEASUREMENT_MESSAGE = "Read Units given a Measurement.";
  private static final String READ_PREFERRED_UNITS_BY_MEASUREMENT_ACTION_ID = "UN011";
  private static final String READ_PREFERRED_UNITS_BY_MEASUREMENT_MESSAGE = "Read the preferred Units for a Measurement.";

  private static final String READ_UNIT_SYSTEMS_INFO_ACTION_ID = "UN012";
  private static final String READ_UNIT_SYSTEMS_INFO_MESSAGE = "Read a list of Unit System descriptions.";

  private static final String SEARCH_UNITS_BY_KEYWORDS_ACTION_ID = "UN013";
  private static final String SEARCH_UNITS_BY_KEYWORDS_MESSAGE = "Search Units by keywords.";

  private static final String GET_UNIT_MAPS_ACTION_ID = "UN014";
  private static final String GET_UNIT_MAPS_MESSAGE = "Get the UnitMap items.";

  private static final String GET_MEASUREMENT_MAPS_ACTION_ID = "UN015";
  private static final String GET_MEASUREMENT_MAPS_MESSAGE = "Get the MeasurementMap items.";

  private static final String GET_MAP_STATES_ACTION_ID = "UN016";
  private static final String GET_MAP_STATES_MESSAGE = "Get the MapStates known in the catalog.";

  private static final String READ_CONVERSION_ABCD_BY_SYMBOLS_ACTION_ID = "UN017";
  private static final String READ_CONVERSION_ABCD_BY_SYMBOLS_MESSAGE = "Read the conversion coefficient in ABCD from the fromUnit and toUnit.";

  private final String user;
  private final String userIpAddress;
  private final String userAgent;
  private final String userAuthorizedGroupName;

  public AuditEvents(String user, String userIpAddress, String userAgent, String userAuthorizedGroupName) {
    this.user = requireNonEmpty(user, "User not provided for audit events.");
    this.userIpAddress = requireNonEmpty(userIpAddress, "User's IP address is not provided for audit events.");
    this.userAgent = requireNonEmpty(userAgent, "User's agent is not provided for audit events.");
    this.userAuthorizedGroupName = requireNonEmpty(userAuthorizedGroupName, "User's authorized group name is not provided for audit events.");
  }

  public AuditPayload getReadCatalogEventSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    return AuditPayload.builder()
        .action(AuditAction.READ)
        .status(AuditStatus.SUCCESS)
        .actionId(READ_CATALOG_SUMMARY_ACTION_ID)
        .message(READ_CATALOG_SUMMARY_MESSAGE)
        .resources(resources)
        .user(user)
        .requiredGroupsForAction(requiredGroupsForAction)
        .userIpAddress(this.userIpAddress)
        .userAgent(this.userAgent)
        .userAuthorizedGroupName(this.userAuthorizedGroupName)
        .build();
  }

  public AuditPayload getReadCatalogLastModifiedSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    return AuditPayload.builder()
        .action(AuditAction.READ)
        .status(AuditStatus.SUCCESS)
        .actionId(READ_CATALOG_LAST_MODIFIED_ACTION_ID)
        .message(READ_CATALOG_LAST_MODIFIED_MESSAGE)
        .resources(resources)
        .user(user)
        .requiredGroupsForAction(requiredGroupsForAction)
        .userIpAddress(this.userIpAddress)
        .userAgent(this.userAgent)
        .userAuthorizedGroupName(this.userAuthorizedGroupName)
        .build();
  }

  public AuditPayload getReadMeasurementsSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    return AuditPayload.builder()
        .action(AuditAction.READ)
        .status(AuditStatus.SUCCESS)
        .actionId(READ_MEASUREMENTS_ACTION_ID)
        .message(READ_MEASUREMENTS_MESSAGE)
        .resources(resources)
        .user(user)
        .requiredGroupsForAction(requiredGroupsForAction)
        .userIpAddress(this.userIpAddress)
        .userAgent(this.userAgent)
        .userAuthorizedGroupName(this.userAuthorizedGroupName)
        .build();
  }

  public AuditPayload getReadSpecificMeasurementSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    return AuditPayload.builder()
        .action(AuditAction.READ)
        .status(AuditStatus.SUCCESS)
        .actionId(READ_SPECIFIC_MEASUREMENT_ACTION_ID)
        .message(READ_SPECIFIC_MEASUREMENT_MESSAGE)
        .resources(resources)
        .user(user)
        .requiredGroupsForAction(requiredGroupsForAction)
        .userIpAddress(this.userIpAddress)
        .userAgent(this.userAgent)
        .userAuthorizedGroupName(this.userAuthorizedGroupName)
        .build();
  }

  public AuditPayload getReadUnitsSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    return AuditPayload.builder()
        .action(AuditAction.READ)
        .status(AuditStatus.SUCCESS)
        .actionId(READ_UNITS_ACTION_ID)
        .message(READ_UNITS_MESSAGE)
        .resources(resources)
        .user(user)
        .requiredGroupsForAction(requiredGroupsForAction)
        .userIpAddress(this.userIpAddress)
        .userAgent(this.userAgent)
        .userAuthorizedGroupName(this.userAuthorizedGroupName)
        .build();
  }

  public AuditPayload getReadUnitEssenceSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    return AuditPayload.builder()
        .action(AuditAction.READ)
        .status(AuditStatus.SUCCESS)
        .actionId(READ_UNITS_BY_ESSENCE_ACTION_ID)
        .message(READ_UNITS_BY_ESSENCE_MESSAGE)
        .resources(resources)
        .user(user)
        .requiredGroupsForAction(requiredGroupsForAction)
        .userIpAddress(this.userIpAddress)
        .userAgent(this.userAgent)
        .userAuthorizedGroupName(this.userAuthorizedGroupName)
        .build();
  }

  public AuditPayload getReadUnitsByUnitSymbolSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    return AuditPayload.builder()
        .action(AuditAction.READ)
        .status(AuditStatus.SUCCESS)
        .actionId(READ_UNITS_BY_UNIT_SYMBOL_ACTION_ID)
        .message(READ_UNITS_BY_UNIT_SYMBOL_MESSAGE)
        .resources(resources)
        .user(user)
        .requiredGroupsForAction(requiredGroupsForAction)
        .userIpAddress(this.userIpAddress)
        .userAgent(this.userAgent)
        .userAuthorizedGroupName(this.userAuthorizedGroupName)
        .build();
  }

  public AuditPayload getReadUnitByUnitSymbolSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    return AuditPayload.builder()
        .action(AuditAction.READ)
        .status(AuditStatus.SUCCESS)
        .actionId(READ_UNIT_BY_UNIT_SYMBOL_ACTION_ID)
        .message(READ_UNIT_BY_UNIT_SYMBOL_MESSAGE)
        .resources(resources)
        .user(user)
        .requiredGroupsForAction(requiredGroupsForAction)
        .userIpAddress(this.userIpAddress)
        .userAgent(this.userAgent)
        .userAuthorizedGroupName(this.userAuthorizedGroupName)
        .build();
  }

  public AuditPayload getReadUnitByMeasurementSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    return AuditPayload.builder()
        .action(AuditAction.READ)
        .status(AuditStatus.SUCCESS)
        .actionId(READ_UNIT_BY_MEASUREMENT_ACTION_ID)
        .message(READ_UNIT_BY_MEASUREMENT_MESSAGE)
        .resources(resources)
        .user(user)
        .requiredGroupsForAction(requiredGroupsForAction)
        .userIpAddress(this.userIpAddress)
        .userAgent(this.userAgent)
        .userAuthorizedGroupName(this.userAuthorizedGroupName)
        .build();

  }

  public AuditPayload getReadUnitsByMeasurementSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    return AuditPayload.builder()
        .action(AuditAction.READ)
        .status(AuditStatus.SUCCESS)
        .actionId(READ_UNITS_BY_MEASUREMENT_ACTION_ID)
        .message(READ_UNITS_BY_MEASUREMENT_MESSAGE)
        .resources(resources)
        .user(user)
        .requiredGroupsForAction(requiredGroupsForAction)
        .userIpAddress(this.userIpAddress)
        .userAgent(this.userAgent)
        .userAuthorizedGroupName(this.userAuthorizedGroupName)
        .build();
  }

  public AuditPayload getReadPreferredUnitsByMeasurementSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    return AuditPayload.builder()
        .action(AuditAction.READ)
        .status(AuditStatus.SUCCESS)
        .actionId(READ_PREFERRED_UNITS_BY_MEASUREMENT_ACTION_ID)
        .message(READ_PREFERRED_UNITS_BY_MEASUREMENT_MESSAGE)
        .resources(resources)
        .user(user)
        .requiredGroupsForAction(requiredGroupsForAction)
        .userIpAddress(this.userIpAddress)
        .userAgent(this.userAgent)
        .userAuthorizedGroupName(this.userAuthorizedGroupName)
        .build();
  }

  public AuditPayload getReadUnitSystemSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    return AuditPayload.builder()
        .action(AuditAction.READ)
        .status(AuditStatus.SUCCESS)
        .actionId(READ_UNIT_SYSTEMS_INFO_ACTION_ID)
        .message(READ_UNIT_SYSTEMS_INFO_MESSAGE)
        .resources(resources)
        .user(user)
        .requiredGroupsForAction(requiredGroupsForAction)
        .userIpAddress(this.userIpAddress)
        .userAgent(this.userAgent)
        .userAuthorizedGroupName(this.userAuthorizedGroupName)
        .build();
  }

  public AuditPayload getSearchMeasurementsByKeyword(List<String> resources, List<String> requiredGroupsForAction) {
    return AuditPayload.builder()
        .action(AuditAction.READ)
        .status(AuditStatus.SUCCESS)
        .actionId(SEARCH_UNITS_BY_KEYWORDS_ACTION_ID)
        .message(SEARCH_UNITS_BY_KEYWORDS_MESSAGE)
        .resources(resources)
        .user(user)
        .requiredGroupsForAction(requiredGroupsForAction)
        .userIpAddress(this.userIpAddress)
        .userAgent(this.userAgent)
        .userAuthorizedGroupName(this.userAuthorizedGroupName)
        .build();
  }

  public AuditPayload getUnitMapsSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    return AuditPayload.builder()
        .action(AuditAction.READ)
        .status(AuditStatus.SUCCESS)
        .actionId(GET_UNIT_MAPS_ACTION_ID)
        .message(GET_UNIT_MAPS_MESSAGE)
        .resources(resources)
        .user(user)
        .requiredGroupsForAction(requiredGroupsForAction)
        .userIpAddress(this.userIpAddress)
        .userAgent(this.userAgent)
        .userAuthorizedGroupName(this.userAuthorizedGroupName)
        .build();
  }

  public AuditPayload getMeasurementMapsSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    return AuditPayload.builder()
        .action(AuditAction.READ)
        .status(AuditStatus.SUCCESS)
        .actionId(GET_MEASUREMENT_MAPS_ACTION_ID)
        .message(GET_MEASUREMENT_MAPS_MESSAGE)
        .resources(resources)
        .user(user)
        .requiredGroupsForAction(requiredGroupsForAction)
        .userIpAddress(this.userIpAddress)
        .userAgent(this.userAgent)
        .userAuthorizedGroupName(this.userAuthorizedGroupName)
        .build();
  }

  public AuditPayload getMapStatesSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    return AuditPayload.builder()
        .action(AuditAction.READ)
        .status(AuditStatus.SUCCESS)
        .actionId(GET_MAP_STATES_ACTION_ID)
        .message(GET_MAP_STATES_MESSAGE)
        .resources(resources)
        .user(user)
        .requiredGroupsForAction(requiredGroupsForAction)
        .userIpAddress(this.userIpAddress)
        .userAgent(this.userAgent)
        .userAuthorizedGroupName(this.userAuthorizedGroupName)
        .build();
  }

  public AuditPayload getReadConversionABCDBySymbolsSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    return AuditPayload.builder()
        .action(AuditAction.READ)
        .status(AuditStatus.SUCCESS)
        .actionId(READ_CONVERSION_ABCD_BY_SYMBOLS_ACTION_ID)
        .message(READ_CONVERSION_ABCD_BY_SYMBOLS_MESSAGE)
        .resources(resources)
        .user(user)
        .requiredGroupsForAction(requiredGroupsForAction)
        .userIpAddress(this.userIpAddress)
        .userAgent(this.userAgent)
        .userAuthorizedGroupName(this.userAuthorizedGroupName)
        .build();
  }

  private static String requireNonEmpty(String value, String message) {
    if (Strings.isNullOrEmpty(value)) {
        throw new IllegalArgumentException(message);
    }
    return value;
  }
}
