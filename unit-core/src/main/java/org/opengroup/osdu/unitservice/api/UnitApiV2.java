package org.opengroup.osdu.unitservice.api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.opengroup.osdu.core.common.model.http.AppError;
import org.opengroup.osdu.unitservice.interfaces.*;
import org.opengroup.osdu.unitservice.model.*;
import org.opengroup.osdu.unitservice.model.extended.QueryResultImpl;
import org.opengroup.osdu.unitservice.model.extended.UnitSystemEssenceImpl;
import org.opengroup.osdu.unitservice.model.extended.UnitSystemInfoResponse;
import org.opengroup.osdu.unitservice.request.*;
import org.opengroup.osdu.unitservice.util.AppException;
import org.opengroup.osdu.unitservice.helper.Utility;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Deprecated
@RestController
@RequestMapping("/v2")
public class UnitApiV2 {
    private CatalogImpl catalog;

    public UnitApiV2(CatalogImpl catalog) {
        this.catalog = catalog;
    }

    void assertRange(int offset, int limit) {
        StringBuilder stringBuilder = new StringBuilder();
        if(offset < 0)
            stringBuilder.append("Offset can not be negative. ");

        if(limit < -1)
            stringBuilder.append("'-1' is the only valid negative value and means all. Other negative values for limit is invalid.");
        
        if(stringBuilder.length() > 0)
            throw AppException.createBadRequest(stringBuilder.toString());
    }

    /********************************************
     Catalog related API
     *********************************************/
    /**
     * Gets the full catalog
     * @return A full catalog
     */
    @Deprecated
    @Operation(summary = "${unitApiV2.catalog.summary}", description = "${unitApiV2.catalog.description}",
            security = {@SecurityRequirement(name = "Authorization")}, tags = {"UoM Catalog (DEPRECATED)"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = Catalog.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
    })
    @GetMapping(value = "/catalog", produces = MediaType.APPLICATION_JSON_VALUE)
    public Catalog getCatalog() {
        return catalog.getCatalogResponse();
    }

    /**
     * Gets a string representation of the date and time when the catalog was last changed.
     * @return  the last update time.
     */
    @Deprecated
    @Operation(summary = "${unitApiV2.catalogLastmodified.summary}", description = "${unitApiV2.catalogLastmodified.description}",
            security = {@SecurityRequirement(name = "Authorization")}, tags = {"UoM Catalog (DEPRECATED)"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = CatalogLastModified.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
    })
    @GetMapping(value = "/catalog/lastmodified", produces = MediaType.APPLICATION_JSON_VALUE)
    public CatalogLastModified getLastModified() {
        return new CatalogLastModifiedImpl(catalog.getLastModified());
    }

    /********************************************
     Measurement related API
     *********************************************/
    /**
     * Gets a {@link QueryResult} with a collection of {@link Measurement} with given range
     * @param offset The offset of the first item in the list of all measurements. It is optional and is 0 by default.
     * @param limit The maximum number of the measurements returned. Minus 1("-1") means all. It is optional and is 100 by default.
     * @return a {@link QueryResult} with a collection of {@link Measurement}
     * @throws AppException An exception will be thrown if the startIndex is out of the range
     */
    @Deprecated
    @Operation(summary = "${unitApiV2.measurement.summary}", description = "${unitApiV2.measurement.description}",
            security = {@SecurityRequirement(name = "Authorization")}, tags = {"Measurements (DEPRECATED)"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = QueryResult.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
    })
    @GetMapping(value = "/measurement", produces = MediaType.APPLICATION_JSON_VALUE)
    public QueryResult getMeasurements(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                       @RequestParam(value = "limit", defaultValue = "100") int limit) {
        assertRange(offset, limit);
        try {
            return Utility.createQueryResultForMeasurements(catalog.getMeasurements(offset, limit));
        }
        catch(Exception ex) {
            throw AppException.createBadRequest(ex.getMessage());
        }
    }

    /**
     * Gets the measurement from the given essence Json {@link Measurement}.
     * When no measurement in the catalog has essence same as essence, a new measurement 
     * with deprecation state tagged as "unresolved" will be returned;
     * otherwise, an exception will be thrown.
     * @param request MeasurementRequest
     * @return a base or child measurement
     * @throws AppException An exception will be thrown if the essence or code is invalid
     */
    @Deprecated
    @Operation(summary = "${unitApiV2.oneMeasurement.summary}", description = "${unitApiV2.oneMeasurement.description}",
            security = {@SecurityRequirement(name = "Authorization")}, tags = {"Measurements (DEPRECATED)"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = Measurement.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
    })
    @PostMapping(value = "/measurement", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Measurement postMeasurement(@RequestBody MeasurementRequest request) {

        try {
            MeasurementEssenceImpl essence = request.getMeasurementEssence();
            return catalog.postMeasurement(essence);
        }
        catch(Exception ex) {
            throw AppException.createBadRequest(ex.getMessage());
        }
    }

    /**
     * Gets the measurement from the given ancestry of {@link Measurement}.
     * When no measurement in the catalog has ancestry same as ancestry, a new measurement 
     * with deprecation state tagged as "unresolved" will be returned;
     * otherwise, an exception will be thrown.
     * @param ancestry ancestry of the measurement
     * @return a base or child measurement
     * @throws AppException An exception will be thrown if the ancestry is invalid
     */
    @Deprecated
    @Operation(summary = "${unitApiV2.oneMeasurementAncestry.summary}", description = "${unitApiV2.oneMeasurementAncestry.description}",
            security = {@SecurityRequirement(name = "Authorization")}, tags = {"Measurements (DEPRECATED)"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = Measurement.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
    })
    @GetMapping(value = "/measurement/{ancestry}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Measurement getMeasurement(@PathVariable("ancestry") String ancestry) {

        try {
            return catalog.getMeasurement(ancestry);
        }
        catch(Exception ex) {
            throw AppException.createBadRequest(ex.getMessage());
        }
    }

    /********************************************
     Unit related API
     *********************************************/
    /**
     * Gets a list of the unitEssences by range
     * @param offset The offset of the first item in the list of all unitEssences. It is optional and is 0 by default.
     * @param limit The maximum number of the unitEssences returned. Minus 1("-1") means all. It is optional and is 100 by default.
     * @return IQueryResult
     * @throws AppException An exception will be thrown if the startIndex is out of the range
     */
    @Deprecated
    @Operation(summary = "${unitApiV2.units.summary}", description = "${unitApiV2.units.description}",
            security = {@SecurityRequirement(name = "Authorization")}, tags = {"Units (DEPRECATED)"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = QueryResult.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
    })
    @GetMapping(value = "/unit", produces = MediaType.APPLICATION_JSON_VALUE)
    public QueryResult getUnits(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                @RequestParam(value = "limit", defaultValue = "100") int limit) {
        assertRange(offset, limit);
        try {
            return Utility.createQueryResultForUnits(catalog.getUnits(offset, limit));
        }
        catch(Exception ex) {
            throw AppException.createBadRequest(ex.getMessage());
        }
    }

    /**
     * Gets the unit by posting the given unit essence.
     *
     * @param request UnitRequest
     * @return        an unit
     * @throws AppException An exception will be thrown if the essence Json is invalid
     */
    @Deprecated
    @Operation(summary = "${unitApiV2.oneUnit.summary}", description = "${unitApiV2.oneUnit.description}",
            security = {@SecurityRequirement(name = "Authorization")}, tags = {"Units (DEPRECATED)"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = Unit.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
    })
    @PostMapping(value = "/unit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Unit postUnit(@RequestBody UnitRequest request) {
        try {
            UnitEssenceImpl essence = request.getUnitEssence();
            return catalog.postUnit(essence);
        }
        catch(Exception ex) {
            throw AppException.createBadRequest(ex.getMessage());
        }
    }

    /**
     * Gets a list of the units from the given unit symbol. The symbol of unit is not unique in the catalog.
     *
     * @param symbol unit symbol
     * @return IQueryResult      The list of the unitEssences
     * @throws AppException An exception will be thrown if the symbol is invalid
     */
    @Deprecated
    @Operation(summary = "${unitApiV2.unitsBySymbol.summary}", description = "${unitApiV2.unitsBySymbol.description}",
            security = {@SecurityRequirement(name = "Authorization")}, tags = {"Units (DEPRECATED)"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = QueryResult.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
    })
    @GetMapping(value = "/unit/symbol/{symbol}", produces = MediaType.APPLICATION_JSON_VALUE)
    public QueryResult getUnitsBySymbol(@PathVariable(value = "symbol") String symbol) {
        try {
            return Utility.createQueryResultForUnits(catalog.getUnitsBySymbol(symbol));
        }
        catch(Exception ex) {
            throw AppException.createBadRequest(ex.getMessage());
        }
    }

    /**
     * Gets a unit from the given unit symbol which the unit is selected based the ordered namespaces 
     * in case there are more than one units having the same given symbol.
     *
     * @param namespaces  namespace list in order
     * @param symbol unit symbol
     * @return a matched unit
     * @throws AppException An exception will be thrown if the symbol is invalid or the symbol does exist in the given namespaces.
     */
    @Deprecated
    @Operation(summary = "${unitApiV2.oneUnitByNamespaceSymbol.summary}", description = "${unitApiV2.oneUnitByNamespaceSymbol.description}",
            security = {@SecurityRequirement(name = "Authorization")}, tags = {"Units (DEPRECATED)"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = Unit.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
    })
    @GetMapping(value = "/unit/symbol/{namespaces}/{symbol}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Unit getUnitBySymbol(@PathVariable("namespaces") String namespaces, @PathVariable("symbol") String symbol) {
        try {
            return catalog.getUnitBySymbol(namespaces, symbol);
        }
        catch(Exception ex) {
            throw AppException.createBadRequest(ex.getMessage());
        }
    }

    /**
     * Gets the unitEssences of the measurement by posting the given measurement essence Json.
     *
     * @param request MeasurementUnitsRequest
     * @return        a list of unitEssences
     * @throws AppException An exception will be thrown if the essence of the measurement is invalid
     */
    @Deprecated
    @Operation(summary = "${unitApiV2.unitsForMeasurement.summary}", description = "${unitApiV2.unitsForMeasurement.description}",
            security = {@SecurityRequirement(name = "Authorization")}, tags = {"Units (DEPRECATED)"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = QueryResult.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
    })
    @PostMapping(value = "/unit/measurement", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public QueryResult postUnitsByMeasurement(@RequestBody MeasurementRequest request) {
        try {
            MeasurementEssenceImpl essence = request.getMeasurementEssence();
            return Utility.createQueryResultForUnits(catalog.postUnitsByMeasurement(essence));
        }
        catch(Exception ex) {
            throw AppException.createBadRequest(ex.getMessage());
        }
    }

    /**
     * Gets the unitEssences of the measurement from the given measurement ancestry.
     *
     * @param ancestry ancestry that could refer to a base measurement or a child measurement
     * @return a list of unitEssences
     * @throws AppException An exception will be thrown if the ancestry of the measurement is invalid
     */
    @Deprecated
    @Operation(summary = "${unitApiV2.unitsForMeasurementByAncestry.summary}", description = "${unitApiV2.unitsForMeasurementByAncestry.description}",
            security = {@SecurityRequirement(name = "Authorization")}, tags = {"Units (DEPRECATED)"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = QueryResult.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
    })
    @GetMapping(value = "/unit/measurement/{ancestry}", produces = MediaType.APPLICATION_JSON_VALUE)
    public QueryResult getUnitsByMeasurement(@PathVariable("ancestry") String ancestry) {

        try {
            return Utility.createQueryResultForUnits(catalog.getUnitsByMeasurement(ancestry));
        }
        catch(Exception ex) {
            throw AppException.createBadRequest(ex.getMessage());
        }
    }

    /**
     * Gets the preferred unitEssences of the measurement by posting the given measurement essence.
     *
     * @param request MeasurementRequest
     * @return IQueryResult   a list of preferred units
     * @throws AppException An exception will be thrown if the essence of the measurement is invalid
     */
    @Deprecated
    @Operation(summary = "${unitApiV2.preferredUnitsMeasurement.summary}", description = "${unitApiV2.preferredUnitsMeasurement.description}",
            security = {@SecurityRequirement(name = "Authorization")}, tags = {"Units (DEPRECATED)"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = QueryResult.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
    })
    @PostMapping(value = "/unit/measurement/preferred", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public QueryResult postPreferredUnitsByMeasurement(@RequestBody MeasurementRequest request) {
        try {
            MeasurementEssenceImpl essence = request.getMeasurementEssence();
            return Utility.createQueryResultForUnits(catalog.postPreferredUnitsByMeasurement(essence));
        }
        catch(Exception ex) {
            throw AppException.createBadRequest(ex.getMessage());
        }
    }

    /**
     * Gets the preferred unitEssences of the measurement from the given measurement ancestry.
     *
     * @param ancestry ancestry that could refer to a base measurement or a child measurement
     * @return IQueryResult   a list of preferred units
     * @throws AppException An exception will be thrown if the ancestry of the measurement is invalid
     */
    @Deprecated
    @Operation(summary = "${unitApiV2.preferredUnitsMeasurementAncestry.summary}", description = "${unitApiV2.preferredUnitsMeasurementAncestry.description}",
            security = {@SecurityRequirement(name = "Authorization")}, tags = {"Units (DEPRECATED)"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = QueryResult.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
    })
    @GetMapping(value = "/unit/measurement/preferred/{ancestry}", produces = MediaType.APPLICATION_JSON_VALUE)
    public QueryResult getPreferredUnitsByMeasurement(@PathVariable("ancestry") String ancestry) {

        try {
            return Utility.createQueryResultForUnits(catalog.getPreferredUnitsByMeasurement(ancestry));
        }
        catch(Exception ex) {
            throw AppException.createBadRequest(ex.getMessage());
        }
    }

    /**
     *  Gets a unit by posting the given unit system name and measurement essence.
     *
     * @param unitSystemName  unit system name
     * @param request         MeasurementRequest
     * @return              an unit object
     * @throws AppException An exception will be thrown if the unit system name/essence or
     *  measurement essence is invalid or there is no unit system for given unit system and measurement.
     * 
     */
    @Deprecated
    @Operation(summary = "${unitApiV2.unitsystems.summary}", description = "${unitApiV2.unitsystems.description}",
            security = {@SecurityRequirement(name = "Authorization")}, tags = {"Unit Systems (DEPRECATED)"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = Unit.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
    })
    @PostMapping(value = "/unit/unitsystem/{unitSystemName}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Unit postUnitBySystemAndMeasurement(@PathVariable("unitSystemName") String unitSystemName,
                                               @RequestBody MeasurementRequest request) {
        try {
           MeasurementEssenceImpl essence = request.getMeasurementEssence();
            return catalog.postUnitBySystemAndMeasurement(unitSystemName, essence);
        }
        catch(Exception ex) {
            throw AppException.createBadRequest(ex.getMessage());
        }
    }

    /**
     *  Gets a unit from the given unit system name and measurement ancestry.
     *
     * @param unitSystemName    unit system name
     * @param ancestry   measurement ancestry
     * @return              an unit object
     * @throws AppException An exception will be thrown if the unit system name/ancestry is invalid or
     * there is no unit system for given unit system and measurement.
     * 
     */
    @Deprecated
    @Operation(summary = "${unitApiV2.unitsystemsAncestry.summary}", description = "${unitApiV2.unitsystemsAncestry.description}",
            security = {@SecurityRequirement(name = "Authorization")}, tags = {"Unit Systems (DEPRECATED)"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = Unit.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
    })
    @GetMapping(value = "/unit/unitsystem/{unitSystemName}/{ancestry}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Unit getUnitBySystemAndMeasurement(@PathVariable("unitSystemName") String unitSystemName,
                                              @PathVariable("ancestry") String ancestry) {

        try {
            return catalog.getUnitBySystemAndMeasurement(unitSystemName, ancestry);
        }
        catch(Exception ex) {
            throw AppException.createBadRequest(ex.getMessage());
        }
    }

    /**
     * Gets the conversion coefficient in ScaleOffset by posting the fromUnit essence and toUnit essence.
     *
     * @param request ConversionScaleOffsetRequest
     * @return        a conversion result that contains result in {@link ScaleOffset} format
     * @throws AppException An exception will be thrown if
     * <ul>
     *     <li>the essence of the fromUnit or toUnit is invalid or;</li>
     *     <li>fromUnit and toUnit are not convertible.</li>
     * </ul>
     */
    @Deprecated
    @Operation(summary = "${unitApiV2.conversions.summary}", description = "${unitApiV2.conversions.description}",
            security = {@SecurityRequirement(name = "Authorization")}, tags = {"Conversions (DEPRECATED)"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = ConversionResult.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
    })
    @PostMapping(value = "/conversion/scale", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ConversionResult postConversionScaleOffset(@RequestBody ConversionScaleOffsetRequest request) {
        try {
            UnitEssenceImpl fromUnitEssence = request.getFromUnitEssence();
            UnitEssenceImpl toUnitEssence = request.getToUnitEssence();
            return catalog.postConversionScaleOffset(fromUnitEssence, toUnitEssence);
        }
        catch (Exception ex) {
            throw AppException.createBadRequest(ex.getMessage());
        }
    }

    /**
     * Gets the conversion coefficient in ABCD by posting the fromUnit essence and toUnit essence
     *
     * @param request ConversionABCDRequest
     * @return        a conversion result that contains result in {@link ABCD} format
     * @throws AppException An exception will be thrown if
     * <ul>
     *     <li>the essence of the fromUnit or toUnit is invalid or;</li>
     *     <li>fromUnit and toUnit are not convertible.</li>
     * </ul>
     */
    @Deprecated
    @Operation(summary = "${unitApiV2.conversionsABCD.summary}", description = "${unitApiV2.conversionsABCD.description}",
            security = {@SecurityRequirement(name = "Authorization")}, tags = {"Conversions (DEPRECATED)"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = ConversionResult.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
    })
    @PostMapping(value = "/conversion/abcd", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ConversionResult postConversionABCD(@RequestBody ConversionABCDRequest request) {
        try {
            UnitEssenceImpl fromUnitEssence = request.getFromUnitEssence();
            UnitEssenceImpl toUnitEssence = request.getToUnitEssence();
            return catalog.postConversionABCD(fromUnitEssence, toUnitEssence);
        }
        catch (Exception ex) {
            throw AppException.createBadRequest(ex.getMessage());
        }
    }

    /**
     * Gets the conversion coefficient in ScaleOffset from the fromUnit and
     * toUnit where are selected based the ordered namespaces.
     *
     * @param namespaces namespace list in order
     * @param fromSymbol symbol of the fromUnit
     * @param toSymbol symbol of the toUnit
     * @return         a conversion result that contains result in {@link ScaleOffset} format
     * @throws AppException An exception will be thrown if
     * <ul>
     *     <li>neither fromSymbol nor toSymbol exists in the given namespaces or;</li>
     *     <li>fromUnit and toUnit are not convertible.</li>
     * </ul>
     */
    @Deprecated
    @Operation(summary = "${unitApiV2.conversionsScale.summary}", description = "${unitApiV2.conversionsScale.description}",
            security = {@SecurityRequirement(name = "Authorization")}, tags = {"Conversions (DEPRECATED)"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = ConversionResult.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
    })
    @GetMapping(value = "/conversion/scale/{namespaces}/{fromSymbol}/{toSymbol}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ConversionResult getConversionScaleOffsetBySymbols(@PathVariable("namespaces") String namespaces,
                                                              @PathVariable("fromSymbol") String fromSymbol,
                                                              @PathVariable("toSymbol") String toSymbol) {

        try {
            return catalog.getConversionScaleOffsetBySymbols(namespaces, fromSymbol, toSymbol);
        }
        catch (Exception ex) {
            throw AppException.createBadRequest(ex.getMessage());
        }
    }

    /**
     * Gets the conversion coefficient in ABCD from the fromUnit and
     * toUnit where are selected based the ordered namespaces.
     *
     * @param namespaces namespace list in order
     * @param fromSymbol symbol of the fromUnit
     * @param toSymbol symbol of the toUnit
     * @return         a conversion result that contains result in {@link ABCD} format
     * @throws AppException An exception will be thrown if
     * <ul>
     *     <li>neither fromSymbol nor toSymbol exists in the given namespaces or;</li>
     *     <li>fromUnit and toUnit are not convertible.</li>
     * </ul>
     */
    @Deprecated
    @Operation(summary = "${unitApiV2.conversionsUnitABCD.summary}", description = "${unitApiV2.conversionsUnitABCD.description}",
            security = {@SecurityRequirement(name = "Authorization")}, tags = {"Conversions (DEPRECATED)"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = ConversionResult.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
    })
    @GetMapping(value = "/conversion/abcd/{namespaces}/{fromSymbol}/{toSymbol}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ConversionResult getConversionABCDBySymbols(@PathVariable("namespaces") String namespaces,
                                                       @PathVariable("fromSymbol") String fromSymbol,
                                                       @PathVariable("toSymbol") String toSymbol) {

        try {
            return catalog.getConversionABCDBySymbols(namespaces, fromSymbol, toSymbol);
        }
        catch (Exception ex) {
            throw AppException.createBadRequest(ex.getMessage());
        }
    }

    /********************************************
     UnitSystem related API
     *********************************************/
    /**
     * Gets a list of {@link UnitSystemInfoResponse}
     *
     * @return A list of unit system infos {@link UnitSystemInfoResponse}
     */
    @Deprecated
    @Operation(summary = "${unitApiV2.unitsystemList.summary}", description = "${unitApiV2.unitsystemList.description}",
            security = {@SecurityRequirement(name = "Authorization")}, tags = {"Unit Systems (DEPRECATED)"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A successful response containing the list of UnitSystemInfo structures", content = { @Content(schema = @Schema(implementation = UnitSystemInfoResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad input format - offset/limit out of range",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
    })
    @GetMapping(value = "/unitsystem/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public UnitSystemInfoResponse getUnitSystemInfoList(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit) {

        return catalog.getUnitSystemInfoList(offset, limit);
    }

    /**
     * Gets a unit system by posting the given unit system essence Json string
     *
     * @param request UnitSystemRequest
     * @return     a unit system
     * @throws AppException An exception will be thrown if the essence of the unit system is invalid
     */
    @Deprecated
    @Operation(summary = "${unitApiV2.unitsystem.summary}", description = "${unitApiV2.unitsystem.description}",
            security = {@SecurityRequirement(name = "Authorization")}, tags = {"Unit Systems (DEPRECATED)"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = UnitSystem.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
    })
    @PostMapping(value = "/unitsystem", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UnitSystem postUnitSystem(@RequestBody UnitSystemRequest request,
                                     @RequestParam(value = "offset", defaultValue = "0") int offset,
                                     @RequestParam(value = "limit", defaultValue = "100") int limit) {
        try {
            UnitSystemEssenceImpl essence = request.getUnitSystemEssence();
            return catalog.postUnitSystem(essence, offset, limit);
        }
        catch(Exception ex) {
            throw AppException.createBadRequest(ex.getMessage());
        }
    }

    /**
     * Gets a unit system from the given unit system name
     *
     * @param name a unit system name
     * @return     a unit system
     * @throws AppException An exception will be thrown if the name of the unit system is invalid
     */
    @Deprecated
    @Operation(summary = "${unitApiV2.unitsystemName.summary}", description = "${unitApiV2.unitsystemName.description}",
            security = {@SecurityRequirement(name = "Authorization")}, tags = {"Unit Systems (DEPRECATED)"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = UnitSystem.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
    })
    @GetMapping(value = "/unitsystem/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UnitSystem getUnitSystem(@PathVariable("name") String name,
                                    @RequestParam(value = "offset", defaultValue = "0") int offset,
                                    @RequestParam(value = "limit", defaultValue = "100") int limit) {

        try {
            return catalog.getUnitSystem(name, offset, limit);
        }
        catch(Exception ex) {
            throw AppException.createBadRequest(ex.getMessage());
        }
    }

    /********************************************
     Search related API
     *********************************************/
    /**
     * Search the unitEssences by keyword and return the results in given range
     * @param request The {@link SearchRequest} containing the query string
     * @param offset  The offset of the first item in the list of all unitEssences. It is optional and is 0 by default.
     * @param limit   The maximum number of the unitEssences returned. Minus 1("-1") means all. It is optional and is 100 by default.
     * @return IQueryResult
     * @throws AppException An exception will be thrown if the startIndex is out of the range or the keyword is empty or null
     */
    @Deprecated
    @Operation(summary = "${unitApiV2.unitsSearch.summary}", description = "${unitApiV2.unitsSearch.description}",
            security = {@SecurityRequirement(name = "Authorization")}, tags = {"Units (DEPRECATED)"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = QueryResult.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
    })
    @PostMapping(value = "/unit/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public QueryResult postSearchUnits(@RequestBody SearchRequest request,
                                       @RequestParam(value = "offset", defaultValue = "0") int offset,
                                       @RequestParam(value = "limit", defaultValue = "100") int limit) {
        assertRange(offset, limit);
        try {
            return Utility.createQueryResultForUnits(catalog.searchUnits(request.getQuery(), offset, limit));
        }
        catch(Exception ex) {
            throw AppException.createBadRequest(ex.getMessage());
        }
    }

    /**
     * Search the unitEssences by keyword and return the results in given range
     * @param request The {@link SearchRequest} containing the query string
     * @param offset  The offset of the first item in the list of all measurements. It is optional and is 0 by default.
     * @param limit   The maximum number of the measurements returned. Minus 1("-1") means all. It is optional and is 100 by default.
     * @return IQueryResult
     * @throws AppException An exception will be thrown if the startIndex is out of the range or the keyword is empty or null
     */
    @Deprecated
    @Operation(summary = "${unitApiV2.measurementsSearch.summary}", description = "${unitApiV2.measurementsSearch.description}",
            security = {@SecurityRequirement(name = "Authorization")}, tags = {"Measurements (DEPRECATED)"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = QueryResult.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
    })
    @PostMapping(value = "/measurement/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public QueryResult postSearchMeasurements(@RequestBody SearchRequest request,
                                              @RequestParam(value = "offset", defaultValue = "0") int offset,
                                              @RequestParam(value = "limit", defaultValue = "100") int limit) {
        assertRange(offset, limit);
        try {
            return Utility.createQueryResultForMeasurements(catalog.searchMeasurements(request.getQuery(), offset, limit));
        }
        catch(Exception ex) {
            throw AppException.createBadRequest(ex.getMessage());
        }
    }

    /**
     * POST a Generic search query
     * @param request The {@link SearchRequest} containing the query string
     * @param offset The offset into the response array
     * @param limit The limit to the response size
     * @return The {@link QueryResult}
     * @throws AppException An exception will be thrown if the startIndex is out of the range or the keyword is empty or null
     */
    @Deprecated
    @Operation(summary = "${unitApiV2.catalogSearch.summary}", description = "${unitApiV2.catalogSearch.description}",
            security = {@SecurityRequirement(name = "Authorization")}, tags = {"UoM Catalog (DEPRECATED)"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = QueryResult.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
    })
    @PostMapping(value = "/catalog/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public QueryResult postSearch(@RequestBody SearchRequest request,
                                  @RequestParam(value = "offset", defaultValue = "0") int offset,
                                  @RequestParam(value = "limit", defaultValue = "100") int limit) {
        assertRange(offset, limit);
        try {
            return catalog.search(request.getQuery(), offset, limit);
        }
        catch(Exception ex) {
            throw AppException.createBadRequest(ex.getMessage());
        }
    }

    @Deprecated
    @Operation(summary = "${unitApiV2.unitMaps.summary}", description = "${unitApiV2.unitMaps.description}",
            security = {@SecurityRequirement(name = "Authorization")}, tags = {"Units (DEPRECATED)"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = QueryResult.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
    })
    @GetMapping(value = "/unit/maps", produces = MediaType.APPLICATION_JSON_VALUE)
    public QueryResult getUnitMaps(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit) {
        try {
            List<UnitMapItem> allItems = new ArrayList<>();
            for (UnitMapImpl unitMap : catalog.getUnitMaps()) {
                allItems.addAll(unitMap.getUnitMapItems());
            }
            QueryResultImpl result = new QueryResultImpl();
            List<UnitMapItem> items = Utility.getRange(allItems, offset, limit);
            for (UnitMapItem item : items) {
                result.addUnitMapItem(item);
            }
            result.setTotalCount(allItems.size());
            result.setOffset(offset);
            return result;
        } catch (IndexOutOfBoundsException ex) {
            throw AppException.createBadRequest(ex.getMessage());
        }
    }

    @Deprecated
    @Operation(summary = "${unitApiV2.measurementsMaps.summary}", description = "${unitApiV2.measurementsMaps.description}",
            security = {@SecurityRequirement(name = "Authorization")}, tags = {"Measurements (DEPRECATED)"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = QueryResult.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
    })
    @GetMapping(value = "/measurement/maps", produces = MediaType.APPLICATION_JSON_VALUE)
    public QueryResult getMeasurementMaps(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit) {
        try {
            List<MeasurementMapItem> allItems = new ArrayList<>();
            for (MeasurementMapImpl measurementMap : catalog.getMeasurementMaps()) {
                allItems.addAll(measurementMap.getMeasurementMapItems());
            }
            QueryResultImpl result = new QueryResultImpl();
            List<MeasurementMapItem> items = Utility.getRange(allItems, offset, limit);
            for (MeasurementMapItem item : items) {
                result.addMeasurementMapItem(item);
            }
            result.setTotalCount(allItems.size());
            result.setOffset(offset);
            return result;
        } catch (IndexOutOfBoundsException ex) {
            throw AppException.createBadRequest(ex.getMessage());
        }
    }
    
    @Deprecated
    @Operation(summary = "${unitApiV2.catalogMap.summary}", description = "${unitApiV2.catalogMap.description}",
            security = {@SecurityRequirement(name = "Authorization")}, tags = {"UoM Catalog (DEPRECATED)"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = QueryResult.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
    })
    @GetMapping(value = "/catalog/mapstates", produces = MediaType.APPLICATION_JSON_VALUE)
    public QueryResult getMapStates(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit) {
        try {
            QueryResultImpl result = new QueryResultImpl();
            result.setMapStates(Utility.getRange(catalog.getWellknownMapStates(), offset, limit));
            result.setTotalCount(catalog.getWellknownMapStates().size());
            result.setOffset(offset);
            return result;
        } catch (IndexOutOfBoundsException ex) {
            throw AppException.createBadRequest(ex.getMessage());
        }
    }
}
