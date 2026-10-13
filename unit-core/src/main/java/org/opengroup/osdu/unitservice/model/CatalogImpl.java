package org.opengroup.osdu.unitservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.opengroup.osdu.unitservice.interfaces.*;
import org.opengroup.osdu.unitservice.helper.MapStateHelper;
import org.opengroup.osdu.unitservice.helper.Utility;
import org.opengroup.osdu.unitservice.index.*;
import org.opengroup.osdu.unitservice.model.extended.*;
import org.opengroup.osdu.unitservice.interfaces.*;
import org.opengroup.osdu.unitservice.helper.*;
import org.opengroup.osdu.unitservice.model.extended.*;

/**
 * The root container of the unit catalog.
 */
@Data
@Schema(description = "The unit of measure catalog")
public class CatalogImpl {
    private static final Logger log= Logger.getLogger( CatalogImpl.class.getName() );
    private Map<String, UnitSystemImpl> nameUnitSystems = null;
    private Map<String, UnitSystemImpl> hashCodeUnitSystems = null;
    private Map<String, MeasurementImpl> hashCodeMeasurements = null;
    private Map<String, UnitImpl> hashCodeUnits = null;
    private Map<String, List<UnitImpl>> symbolUnits = null;
    private Indexer indexer;

    @Expose @SerializedName("nextID")
    @Schema(type = "string")
    private String nextID;

    @Expose @SerializedName("name")
    @Schema(type = "string")
    private String name;

    @Expose @SerializedName("lastModified")
    @Schema(description = "The unit of measure catalog's last modification date.",type = "string")
    private String lastModified;

    @Expose @SerializedName("listOfBaseMeasurements")
    @Schema(type = "array", implementation = MeasurementImpl.class)
    private List<MeasurementImpl> baseMeasurements;

    @Expose @SerializedName("listOfChildMeasurements")
    @Schema(type = "array", implementation = MeasurementImpl.class)
    private List<MeasurementImpl> childMeasurements;

    @Expose @SerializedName("listOfUnits")
    @Schema(description = "The array of units defined in this catalog",type = "array", implementation = UnitImpl.class)
    private List<UnitImpl> units;

    @Expose @SerializedName("listOfUnitSystems")
    @Schema(type = "array", implementation = UnitSystemImpl.class)
    private List<UnitSystemImpl> unitSystems;

    @Expose @SerializedName("listOfMeasurementMaps")
    @Schema(type = "array", implementation = MeasurementMapImpl.class)
    private List<MeasurementMapImpl> measurementMaps;

    @Expose @SerializedName("listOfUnitMaps")
    @Schema(type = "array", implementation = UnitMapImpl.class)
    private List<UnitMapImpl> unitMaps;

    @Expose @SerializedName("wellKnownMapStates")
    @Schema(type = "array", implementation = MapStateImpl.class)
    private List<MapStateImpl> wellknownMapStates;

    @JsonIgnore
    private int unitMapItemCount;

    @JsonIgnore
    private int measurementMapItemCount;

    private CatalogImpl() {
        // Avoid null collection from the catalog file
        baseMeasurements = new ArrayList<>();
        childMeasurements = new ArrayList<>();
        units = new ArrayList<>();
        unitSystems = new ArrayList<>();
        measurementMaps = new ArrayList<>();
        unitMaps = new ArrayList<>();
        wellknownMapStates = new ArrayList<>();
    }

    /**
     * Creates a catalog instance from given reader.
     * @param reader input source reader
     * @return Catalog instance
     * @throws Exception an exception will be thrown if there is any error during the deserialization
     * of the json file and post-initialization of the catalog.
     */
    public static CatalogImpl createCatalog(Reader reader) throws Exception {
		if(reader == null) {
			throw new IOException("Null reader");
		}
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.excludeFieldsWithoutExposeAnnotation().create();
        CatalogImpl catalogImpl = gson.fromJson(reader, CatalogImpl.class);
        catalogImpl.initCatalog();
		catalogImpl.refreshIndex();
        return catalogImpl;
    }

    /**
     * Gets the name of the catalog
     * @return name of the catalog
     */
    public String getName() { return this.name; }


    /**
     * Gets a string representation of the date and time when the catalog was last changed.
     * @return  the last update time.
     */
    public String getLastModified() { return this.lastModified; }

    /********************************************
     Measurement related API
     *********************************************/
    private static final Comparator<MeasurementImpl> measurementsComparator = new Comparator<MeasurementImpl>() {
        @Override
        public int compare(MeasurementImpl m1, MeasurementImpl m2) {
            return m1.getEssence().getAncestry().compareTo(m2.getEssence().getAncestry());
        }};

    /**
     * Gets a list of the base measurements by range
     * @param offset The offset of the first item in the list of all base measurements. It is optional and is 0 by default.
     * @param limit The maximum number of the base measurements returned. It is optional and is -1 (means all) by default.
     * @return a list of the base measurements
     * @throws Exception An exception will be thrown if the startIndex is out of the range
     */
    public Result<MeasurementImpl> getMeasurements(int offset, int limit) throws Exception {
        List<MeasurementImpl> measurements = getBaseMeasurements();
        // catalog.getBaseMeasurements() is immutable
        // So it is ok to add the child measurements to local measurements
        measurements.addAll(getChildMeasurements());
        Collections.sort(measurements, measurementsComparator);
        List<MeasurementImpl> ranges = Utility.getRange(measurements, offset, limit);
        return new Result<>(ranges, offset, measurements.size());
    }

    /**
     * Gets the measurement by posting the given essence of {@link MeasurementImpl}.
     * When no measurement in the catalog has essence same as essence, a new measurement 
     * with deprecation state tagged as "unresolved" will be returned;
     * otherwise, an exception will be thrown.
     * @param essence the essence of {@link MeasurementImpl}
     * @return        a measurement instance
     * @throws Exception An exception will be thrown if essence is null or invalid.
     */
    public MeasurementImpl postMeasurement(MeasurementEssence essence) throws Exception {
        if(essence == null)
            throw new IllegalArgumentException("The measurement essence can not be null");

        try {
            return postMeasurementInCatalog(essence);
        }
        catch(Exception ex) {
        }
        return createMeasurement(essence, true);
    }

    /**
     * Gets the measurement from the given ancestry of {@link MeasurementImpl}.
     * When no measurement in the catalog has ancestry same as ancestry, an exception will be thrown.
     * @param ancestry the ancestry of {@link MeasurementImpl}
     * @return         a measurement instance
     * @throws Exception An exception will be thrown if ancestry is null or invalid.
     */
    public MeasurementImpl getMeasurement(String ancestry) throws Exception {
        ancestry = Utility.trim(ancestry);
        if(Utility.isNullOrEmpty(ancestry))
            throw new IllegalArgumentException("The measurement ancestry can not be null");
        try {
            return getMeasurementInCatalog(ancestry);
        }
        catch(Exception ex) {
        }
        throw new Exception("The measurement ancestry '" + ancestry + "' is invalid");
    }

    /**
     * Gets the measurement from the given essence of {@link MeasurementImpl}.
     * If no measurement in the catalog has essence same as essence, an exception
     * will be thrown. This is different from method {@link #getMeasurement(String)}.
     * @param essence the essence or ancestry of {@link MeasurementImpl}
     * @return                a measurement instance
     * @throws Exception An exception will be thrown if essence is null or invalid.
     */
    public MeasurementImpl postMeasurementInCatalog(MeasurementEssence essence) throws Exception {
        String hashCode;
        MeasurementImpl seed = createMeasurement(essence, false);
        hashCode = Integer.valueOf(seed.hashCode()).toString();

        if(hashCodeMeasurements.containsKey(hashCode))
            return hashCodeMeasurements.get(hashCode);

        log.warning(String.format("hashCodeMeasurements does not contain the hash code for measurement essence '%s'. The size of hashCodeUnitSystems is %d", essence, hashCodeMeasurements.size()));
        throw new IllegalArgumentException("The measurement essence or code '" + essence + "' is invalid");
    }

    /**
     * Gets the measurement from the given ancestry of {@link MeasurementImpl}.
     * If no measurement in the catalog has ancestry same as code, an exception
     * will be thrown. This is different from method {@link #getMeasurement(String)}.
     * @param ancestry the ancestry of {@link MeasurementImpl}
     * @return         a measurement instance
     * @throws Exception An exception will be thrown if Ancestry is null or invalid.
     */
    public MeasurementImpl getMeasurementInCatalog(String ancestry) throws Exception {
        String hashCode;

        hashCode = Integer.valueOf(ancestry.hashCode()).toString();

        if(hashCodeMeasurements.containsKey(hashCode))
            return hashCodeMeasurements.get(hashCode);

        log.warning(String.format("hashCodeMeasurements does not contain the hash code for measurement ancestry '%s'. The size of hashCodeUnitSystems is %d", ancestry, hashCodeMeasurements.size()));
        throw new IllegalArgumentException("The measurement ancestry '" + ancestry + "' is invalid");
    }


    /********************************************
     Unit related API
     *********************************************/

    private static final Comparator<UnitImpl> unitsComparator = new Comparator<UnitImpl>() {
        @Override
        public int compare(UnitImpl o1, UnitImpl o2) {
            return o1.getEssence().getSymbol().compareTo(o2.getEssence().getSymbol());
        }};
    /**
     * Gets a list of {@link UnitImpl} in given range
     * @param offset The offset of the first item in a whole collection of {@link UnitImpl}. It is optional and is 0 by default.
     * @param limit The maximum number of the unitEssences returned. It is optional and is -1 (means all) by default.
     * @return a {@link Result} with a collection of {@link UnitImpl}
     * @throws Exception An exception will be thrown if the startIndex is out of the range
     */
    public Result<UnitImpl> getUnits(int offset, int limit) throws Exception {
        List<UnitImpl> units = getUnits();
        Collections.sort(units, unitsComparator);
        List<UnitImpl> ranges = Utility.getRange(units, offset, limit);
        return new Result<UnitImpl>(ranges, offset, units.size());
    }

    /**
     * Gets the unit by posting the given essence.
     * When no unit in the catalog is found with the given essence, a new unit with deprecation state tagged as
     * "unresolved" will be returned.
     * @param essence the essence of {@link UnitImpl}
     * @return          an unit instance
     * @throws Exception An exception will be thrown if the essence is null or invalid.
     */
    public UnitImpl postUnit(UnitEssenceImpl essence) throws Exception {
        if(essence == null)
            throw new IllegalArgumentException("The essence can not be null");

        UnitImpl reconstructedUnit = new UnitImpl();
        reconstructedUnit.setEssence(essence);

        // Use the hashCode of the reconstructed unit to get the unit in the catalog
        String hashCode = Integer.valueOf(reconstructedUnit.hashCode()).toString();
        if(hashCodeUnits.containsKey(hashCode)) {
            return hashCodeUnits.get(hashCode);
        }

        // If there is no counterpart unit in the catalog, returns the constructed unit with deprecationInfo
        MapStateImpl unresolved = MapStateHelper.getUnresolvedMapState();
        UnitDeprecationInfoImpl deprecationInfo = new UnitDeprecationInfoImpl();
        deprecationInfo.setState(unresolved.getState());
        deprecationInfo.setRemarks(unresolved.getDescription());
        reconstructedUnit.setDeprecationInfo(deprecationInfo);

        return reconstructedUnit;
    }

    /**
     * Gets a collection of {@link UnitImpl} from the given unit symbol. The symbol of unit is not unique in the catalog.
     *
     * @param symbol unit symbol
     * @return       a collection of {@link UnitImpl}
     * @throws Exception An exception will be thrown if the symbol is null or does not exist in the catalog.
     */
    public Result<UnitImpl> getUnitsBySymbol(String symbol) throws Exception {
        symbol = Utility.trim(symbol);
        if(Utility.isNullOrEmpty(symbol))
            throw new IllegalArgumentException("The symbol can not be null");

        if(symbolUnits.containsKey(symbol)) {
            return new Result<>(symbolUnits.get(symbol));
        }

        String errorMessage = String.format("The symbol '%s' does not exist.", symbol);
        log.warning(String.format("%s The size of symbolUnits is %d", errorMessage, symbolUnits.size()));

        throw new IllegalArgumentException(errorMessage);
    }

    /**
     * Gets a unit from the given unit symbol which the unit is selected based the ordered namespaces in c
     * ase there are more than one units having the same given symbol.
     *
     * @param namespaces  namespace list in order
     * @param symbol unit symbol
     * @return unit
     * @throws Exception An exception will be thrown if the symbol is invalid or the symbol does exist in the given namespaces.
     */
    public UnitImpl getUnitBySymbol(String namespaces, String symbol) throws Exception {
        namespaces = Utility.trim(namespaces);
        if(Utility.isNullOrEmpty(namespaces))
            throw new IllegalArgumentException("The namespace(s) can not be null");
        symbol = Utility.trim(symbol);
        if(Utility.isNullOrEmpty(symbol))
            throw new IllegalArgumentException("The symbol can not be null");

        Result<UnitImpl> fromUnits = getUnitsBySymbol(symbol);
        for (String namespace:namespaces.split(",")) {
            for (UnitImpl unit : fromUnits.getItems()) {
                if(namespace.equals(unit.getNamespace()))
                    return unit;
            }
        }
        throw new IllegalArgumentException("Cannot find a Unit for symbol '" + symbol + "' in the given namespaces.");
    }

    /**
     * Gets a collection of {@link UnitImpl} by posting the given essence of {@link MeasurementImpl}.
     *
     * @param essence  essence that could refer to a base measurement or a child measurement
     * @return                a list of unitEssences
     * @throws Exception an exception will be thrown if essence is null or invalid.
     */
    public Result<UnitImpl> postUnitsByMeasurement(MeasurementEssenceImpl essence) throws Exception {
        if(essence == null)
            throw new IllegalArgumentException("The measurement essence can not be null");

        MeasurementImpl measurement = postMeasurementInCatalog(essence);
        if(measurement == null)
            throw new IllegalArgumentException("Measurement not found");

        // Units are associated with base measurement
        // Get the base measurement
        if(!measurement.isBaseMeasurement()) {
            String baseMeasurementEssenceJson = measurement.getBaseMeasurementEssenceJson();
            MeasurementEssenceImpl baseMeasurementEssence = Utility.fromJsonString(baseMeasurementEssenceJson, MeasurementEssenceImpl.class);
            measurement = postMeasurementInCatalog(baseMeasurementEssence);
        }

        return new Result<UnitImpl>(measurement.getUnits());
    }

    /**
     * Gets a collection of {@link UnitImpl} from the given ancestry of {@link MeasurementImpl}.
     *
     * @param ancestry  ancestry that could refer to a base measurement or a child measurement
     * @return                a list of unitEssences
     * @throws Exception an exception will be thrown if ancestry is null or invalid.
     */
    public Result<UnitImpl> getUnitsByMeasurement(String ancestry) throws Exception {
        ancestry = Utility.trim(ancestry);
        if(Utility.isNullOrEmpty(ancestry))
            throw new IllegalArgumentException("The measurement ancestry can not be null");

        MeasurementImpl measurement = getMeasurementInCatalog(ancestry);
        if(measurement == null)
            throw new IllegalArgumentException("Measurement not found");

        // Units are associated with base measurement
        // Get the base measurement
        if(!measurement.isBaseMeasurement()) {
            String baseMeasurementEssenceJson = measurement.getBaseMeasurementEssenceJson();
            MeasurementEssenceImpl baseMeasurementEssence = Utility.fromJsonString(baseMeasurementEssenceJson, MeasurementEssenceImpl.class);
            measurement = postMeasurementInCatalog(baseMeasurementEssence);
        }

        return new Result<UnitImpl>(measurement.getUnits());
    }

    /**
     * Gets a collection of preferred {@link UnitImpl} from the given essence of {@link MeasurementImpl}.
     *
     * @param essence  essence Json string that could refer to a base measurement or a child measurement
     * @return                a list of preferred unitEssences
     * @throws Exception an exception will be thrown if essence Json string is null or invalid.
     */
    public Result<UnitImpl> postPreferredUnitsByMeasurement(MeasurementEssenceImpl essence) throws Exception {
        if(essence == null)
            throw new IllegalArgumentException("The measurement essence can not be null");

        MeasurementImpl measurement = postMeasurementInCatalog(essence);
        if(measurement == null)
            throw new IllegalArgumentException("Measurement not found");

        return new Result<UnitImpl>(measurement.getPreferredUnits());
    }

    /**
     * Gets a collection of preferred {@link UnitImpl} from the given ancestry of {@link MeasurementImpl}.
     *
     * @param ancestry  ancestry that could refer to a base measurement or a child measurement
     * @return                a list of preferred unitEssences
     * @throws Exception an exception will be thrown if ancestry is null or invalid.
     */
    public Result<UnitImpl> getPreferredUnitsByMeasurement(String ancestry) throws Exception {
        ancestry = Utility.trim(ancestry);
        if(Utility.isNullOrEmpty(ancestry))
            throw new IllegalArgumentException("The measurement code can not be null");

        MeasurementImpl measurement = getMeasurementInCatalog(ancestry);
        if(measurement == null)
            throw new IllegalArgumentException("Measurement not found");

        return new Result<UnitImpl>(measurement.getPreferredUnits());
    }

    /**
     *  Gets a unit by posting the given unit system name and measurement essence.
     *
     * @param unitSystemName    unit system name
     * @param measurementEssence   measurement essence
     * @return                  an unit
     * @throws Exception an exception will be thrown if any of the following conditions is met:
     * <ol>
     *     <li>unitSystemName is null or does not exist in the catalog</li>
     *     <li>measurementEssence is null or essence in the catalog</li>
     * </ol>
     */
    public UnitImpl postUnitBySystemAndMeasurement(String unitSystemName, MeasurementEssenceImpl measurementEssence) throws Exception {
        if(Utility.isNullOrEmpty(unitSystemName))
            throw new IllegalArgumentException("The unit system name can not be null");
        if(measurementEssence == null)
            throw new IllegalArgumentException("The measurement essence can not be null");

        MeasurementImpl measurement = postMeasurementInCatalog(measurementEssence);
        if(measurement == null)
            throw new IllegalArgumentException("Measurement not found");

        UnitSystemImpl unitSystem = getUnitSystem(Utility.trim(unitSystemName));
        UnitAssignmentImpl unitAssignment;
        try {
            unitAssignment = unitSystem.getUnitAssignment(measurement);
        }
        catch(Exception ex) {
            // See Task 513514: https://tfs.slb.com/tfs/SLB1/DataAtRest/_workitems/edit/513514?fullScreen=false
            //Ignore, try the Canonical unit system as last resort
            unitSystem = getUnitSystem("Canonical");
            unitAssignment = unitSystem.getUnitAssignment(measurement);
        }

        return unitAssignment.getUnit();
    }

    /**
     *  Gets a unit from the given unit system name and measurement ancestry.
     *
     * @param unitSystemName    unit system name
     * @param measurementAncestry   measurement ancestry
     * @return                  an unit
     * @throws Exception an exception will be thrown if any of the following conditions is met:
     * <ol>
     *     <li>unitSystemName is null or does not exist in the catalog</li>
     *     <li>measurementEssence is null or ancestry in the catalog</li>
     * </ol>
     */
    public UnitImpl getUnitBySystemAndMeasurement(String unitSystemName, String measurementAncestry) throws Exception {
        if(Utility.isNullOrEmpty(unitSystemName))
            throw new IllegalArgumentException("The unit system name can not be null");
        if(Utility.isNullOrEmpty(measurementAncestry))
            throw new IllegalArgumentException("The measurement ancestry can not be null");

        MeasurementImpl measurement = getMeasurementInCatalog(Utility.trim(measurementAncestry));
        if(measurement == null)
            throw new IllegalArgumentException("Measurement not found");

        UnitSystemImpl unitSystem = getUnitSystem(Utility.trim(unitSystemName));
        UnitAssignmentImpl unitAssignment;
        try {
            unitAssignment = unitSystem.getUnitAssignment(measurement);
        }
        catch(Exception ex) {
            // See Task 513514: https://tfs.slb.com/tfs/SLB1/DataAtRest/_workitems/edit/513514?fullScreen=false
            //Ignore, try the Canonical unit system as last resort
            unitSystem = getUnitSystem("Canonical");
            unitAssignment = unitSystem.getUnitAssignment(measurement);
        }

        return unitAssignment.getUnit();
    }

    /**
     * Gets the conversion coefficient in ScaleOffset by posting fromUnit and toUnit essences.
     *
     * @param fromUnitEssence  the fromUnit essence.
     * @param toUnitEssence   the toUnit essence.
     * @return            a conversion result that contains result in {@link ScaleOffset} format
     * @throws Exception an exception will be thrown if any of the following conditions is met:
     * <ol>
     *     <li>fromUnitEssence is null or invalid</li>
     *     <li>toUnitEssence is null or invalid</li>
     *     <li>The fromUnit and toUnit are inconvertible</li>
     * </ol>
     */
     public ConversionResultImpl postConversionScaleOffset(UnitEssenceImpl fromUnitEssence, UnitEssenceImpl toUnitEssence) throws Exception {
        UnitImpl fromUnit = postUnit(fromUnitEssence);
        UnitImpl toUnit = postUnit(toUnitEssence);
        ScaleOffsetImpl scaleOffset = getConversionScaleOffset(fromUnit, toUnit);
        return new ConversionResultImpl(scaleOffset, fromUnit, toUnit);
     }

    /**
     * Gets the conversion coefficient in ABCD by posting fromUnit and toUnit essences.
     *
     * @param fromUnitEssence  the fromUnit essence.
     * @param toUnitEssence   the toUnit essence.
     * @return            a conversion result that contains result in {@link ScaleOffset} format
     * @return            a conversion result that contains result in {@link ABCD} format
     * @throws Exception an exception will be thrown if any of the following conditions is met:
     * <ol>
     *     <li>fromUnitEssence is null or invalid</li>
     *     <li>toUnitEssence is null or invalid</li>
     *     <li>The fromUnit and toUnit are inconvertible</li>
     * </ol>
     */
    public ConversionResultImpl postConversionABCD(UnitEssenceImpl fromUnitEssence, UnitEssenceImpl toUnitEssence) throws Exception {
        UnitImpl fromUnit = postUnit(fromUnitEssence);
        UnitImpl toUnit = postUnit(toUnitEssence);
        ABCDImpl abcd = getConversionABCD(fromUnit, toUnit);
        return new ConversionResultImpl(abcd, fromUnit, toUnit);
    }

    /**
     * Gets the conversion coefficient in ScaleOffset from the fromUnit and
     * toUnit where are selected based the ordered namespaces.
     * @param namespaces namespace list in order
     * @param fromSymbol symbol of the fromUnit
     * @param toSymbol symbol of the toUnit
     * @return         a conversion result that contains result in {@link ScaleOffset} format
     * @throws Exception An exception will be thrown if
     * <ul>
     *     <li>neither fromSymbol nor toSymbol exists in the given namespaces or;</li>
     *     <li>fromUnit and toUnit are not convertible.</li>
     * </ul>
     */
    public ConversionResultImpl getConversionScaleOffsetBySymbols(String namespaces,
                                                                  String fromSymbol,
                                                                  String toSymbol) throws Exception {
        namespaces = Utility.trim(namespaces);
        fromSymbol = Utility.trim(fromSymbol);
        toSymbol = Utility.trim(toSymbol);
        if(Utility.isNullOrEmpty(namespaces))
            throw new IllegalArgumentException("The namespace(s) can not be null");
        if(Utility.isNullOrEmpty(fromSymbol))
            throw new IllegalArgumentException("The symbol of fromUnit can not be null");
        if(Utility.isNullOrEmpty(toSymbol))
            throw new IllegalArgumentException("The symbol of toUnit can not be null");

        UnitImpl[] units = getCandidateUnits(namespaces, fromSymbol, toSymbol);
        UnitImpl fromUnit = units[0];
        UnitImpl toUnit = units[1];
        ScaleOffsetImpl scaleOffset = getConversionScaleOffset(fromUnit, toUnit);
        return new ConversionResultImpl(scaleOffset, fromUnit, toUnit);
    }

    /**
     * Gets the conversion coefficient in ABCD from the fromUnit and
     * toUnit where are selected based the ordered namespaces.
     *
     * @param namespaces namespace list in order
     * @param fromSymbol symbol of the fromUnit
     * @param toSymbol symbol of the toUnit
     * @return         a conversion result that contains result in {@link ABCD} format
     * @throws Exception An exception will be thrown if
     * <ul>
     *     <li>neither fromSymbol nor toSymbol exists in the given namespaces or;</li>
     *     <li>fromUnit and toUnit are not convertible.</li>
     * </ul>
     */
    public ConversionResultImpl getConversionABCDBySymbols(String namespaces,
                                                           String fromSymbol,
                                                           String toSymbol) throws Exception {
        namespaces = Utility.trim(namespaces);
        fromSymbol = Utility.trim(fromSymbol);
        toSymbol = Utility.trim(toSymbol);
        if(Utility.isNullOrEmpty(fromSymbol))
            throw new IllegalArgumentException("The namespace(s) can not be null");
        if(Utility.isNullOrEmpty(fromSymbol))
            throw new IllegalArgumentException("The symbol of fromUnit can not be null");
        if(Utility.isNullOrEmpty(toSymbol))
            throw new IllegalArgumentException("The symbol of toUnit can not be null");

        UnitImpl[] units = getCandidateUnits(namespaces, fromSymbol, toSymbol);
        UnitImpl fromUnit = units[0];
        UnitImpl toUnit = units[1];
        ABCDImpl abcd = getConversionABCD(fromUnit, toUnit);
        return new ConversionResultImpl(abcd, fromUnit, toUnit);
    }

    /********************************************
     UnitSystem related API
     *********************************************/
    /**
     * Gets all supported unit system names
     *
     * @return A list of unit system names
     */
    public UnitSystemInfoResponse getUnitSystemInfoList(int offset, int limit) {
        List<UnitSystemInfo> infoList = new ArrayList<>();
        List<UnitSystemImpl> systems = Utility.getRange(this.getUnitSystems(), offset, limit);
        for (UnitSystemImpl us: systems) {
            infoList.add(new UnitSystemInfo(us.getName(), us.getDescription(),
                    us.getAncestry(), us.getPersistableReference()));
        }
        return new UnitSystemInfoResponse(infoList, this.getUnitSystems().size(), offset);
    }

    /**
     * Gets a unit system by posting the given unit system essence
     *
     * @param essence a unit system essence
     * @return     a unit system
     * @throws Exception an exception will be thrown if the essence is null or does not exist in the catalog.
     */
    public UnitSystemImpl postUnitSystem(UnitSystemEssenceImpl essence, int offset, int limit) throws Exception {
        if(essence == null)
            throw new IllegalArgumentException("The unit system essence can not be null");

        String hashCode = Integer.valueOf(essence.hashCode()).toString();
        if(hashCodeUnitSystems.containsKey(hashCode)) {
            UnitSystemImpl us = hashCodeUnitSystems.get(hashCode);
            return us.getCopyByOffsetAndLimit(offset, limit);
        }
        log.warning(String.format("hashCodeUnitSystems does not contain the hash code for unit system essence '%s'. The size of hashCodeUnitSystems is %d", essence, hashCodeUnitSystems.size()));

        throw new IllegalArgumentException("The unit system '" + essence.toJsonString() + "' is not supported");
    }

    /**
     * Get the entire unit system definition
     * @param name The name of the requested unit system
     * @return the {@link UnitSystemImpl}
     * @throws Exception
     */
    public UnitSystemImpl getUnitSystem(String name) throws Exception {
        return this.getUnitSystem(name, 0, -1);
    }
    /**
     * Gets a unit system from the given unit system name
     *
     * @param name a unit system name
     * @param offset The offset into the list of unit assignments staring with 0
     * @param limit  The maximum requested items in the returned unit assignment list
     * @return     a unit system by its name
     * @throws Exception an exception will be thrown if the name is null or does not exist in the catalog.
     */
    public UnitSystemImpl getUnitSystem(String name, int offset, int limit) throws Exception {
        name = Utility.trim(name);
        if(Utility.isNullOrEmpty(name))
            throw new IllegalArgumentException("The unit system name can not be null");

        if(nameUnitSystems.containsKey(name)) {
            UnitSystemImpl us = nameUnitSystems.get(name);
            return us.getCopyByOffsetAndLimit(offset, limit);
        }
        log.warning(String.format("nameUnitSystems does not contain the unit system name '%s'. The size of nameUnitSystems is %d", name, nameUnitSystems.size()));

        throw new IllegalArgumentException("The unit system '" + name + "' is not supported");
    }

    /********************************************
     Search related API
     *********************************************/
    /**
     * Search the unitEssences by keyword and return the results in given range
     * @param keyword The search keyword
     * @param offset  The offset of the first item in the list of all unitEssences. It is optional and is 0 by default.
     * @param limit   The maximum number of the unitEssences returned. It is optional and is 100 by default.
     * @return a collection of units
     * @throws Exception an exception will be thrown if the keyword is null or has invalid syntax
     * @throws Exception an exception will be thrown if offset is out of the range, e.g. negative or too big
     * @throws Exception an exception will be thrown if catalog is not valid or index creation fails
     */
    public Result<UnitImpl> searchUnits(String keyword, int offset, int limit) throws Exception {
        refreshIndex();        
        Result<IndexRow> indexRows = searchUnit(keyword, offset, limit);
        List<UnitImpl> results = new ArrayList<>();
        int total = indexRows.getTotalCount();
        for (IndexRow indexRow: indexRows.getItems()) {
            try {
                if(UnitConverter.isSupported(indexRow)) {
                    UnitImpl unit = UnitConverter.toUnit(indexRow, this);
                    results.add(unit);
                }
                else
                    total--;
            }
            catch (Exception ex) {
                total--;
                log.warning("Can not find unit with essence: " + indexRow.toJsonString() + "\nError: " + ex.getMessage());
            }
        }

        return new Result<UnitImpl>(results, indexRows.getOffset(), indexRows.getTotalCount());
    }

    /**
     * Search the unitEssences by keyword and return the results in given range
     * @param keyword The search keyword
     * @param offset  The offset of the first item in the list of all measurements. It is optional and is 0 by default.
     * @param limit   The maximum number of the measurements returned. It is optional and is 100 by default.
     * @return a collection of measurements
     * @throws Exception an exception will be thrown if the keyword is null or has invalid syntax
     * @throws Exception an exception will be thrown if offset is out of the range, e.g. negative or too big
     * @throws Exception an exception will be thrown if catalog is not valid or index creation fails
     */
    public Result<MeasurementImpl> searchMeasurements(String keyword, int offset, int limit) throws Exception {
        refreshIndex();        
        Result<IndexRow> indexRows = searchMeasurement(keyword, offset, limit);
        List<MeasurementImpl> results = new ArrayList<>();
        int total = indexRows.getTotalCount();
        for (IndexRow indexRow: indexRows.getItems()) {
            try {
                if(MeasurementConverter.isSupported(indexRow)) {
                    MeasurementImpl measurement = MeasurementConverter.toMeasurement(indexRow, this);
                    results.add(measurement);
                }
                else
                    total--;
            }
            catch (Exception ex) {
                total--;
                log.warning("Can not find measurement with essence: " + indexRow.toJsonString() + "\nError: " + ex.getMessage());
            }
        }

        return new Result<MeasurementImpl>(results, indexRows.getOffset(), total);
    }

    /**
     * Search by keyword and return the results in given range
     * @param keyword The search keyword
     * @param offset  The offset of the first item in the list of the search items. It is optional and is 0 by default.
     * @param limit   The maximum number of the items returned. It is optional and is 500 by default.
     * @return a {@link QueryResultImpl} that may include:
     * <ol>
     *     <li>a collection of units</li>
     *     <li>a collection of measurements</li>
     *     <li>a collection of unit map items</li>
     *     <li>a collection of measurement map items</li>
     * </ol>
     * @throws Exception an exception will be thrown if the keyword is null or has invalid syntax
     * @throws Exception an exception will be thrown if offset is out of the range, e.g. negative or too big
     * @throws Exception an exception will be thrown if catalog is not valid or index creation fails
     */
    public QueryResultImpl search(String keyword, int offset, int limit) throws Exception {
        if(Utility.isNullOrEmpty(keyword))
            throw new IllegalArgumentException("Keyword can not be null");

        refreshIndex();

        assertSearch();

        SearchInput searchInput = new SearchInput(keyword);
        searchInput.setOffset(offset);
        searchInput.setSize(limit);

        Result<IndexRow> result = indexer.search(searchInput);

        int total = result.getTotalCount();

        QueryResultImpl queryResult = new QueryResultImpl();
        for (IndexRow indexRow: result.getItems()) {
            try {
                if(UnitConverter.isSupported(indexRow)) {
                    UnitImpl unit = UnitConverter.toUnit(indexRow, this);
                    queryResult.addUnit(unit);
                    continue;
                }

                if(MeasurementConverter.isSupported(indexRow)) {
                    MeasurementImpl measurement = MeasurementConverter.toMeasurement(indexRow, this);
                    queryResult.addMeasurement(measurement);
                    continue;
                }

                if(UnitMapItemConverter.isSupported(indexRow)) {
                    UnitMapItemImpl mapItem = UnitMapItemConverter.toUnitMapItem(indexRow, this);
                    queryResult.addUnitMapItem(mapItem);
                    continue;
                }

                if(MeasurementMapItemConverter.isSupported(indexRow)) {
                    MeasurementMapItemImpl mapItem = MeasurementMapItemConverter.toMeasurementMapItem(indexRow, this);
                    queryResult.addMeasurementMapItem(mapItem);
                    continue;
                }

                log.warning("Object type '" + indexRow.getType() + "' can not be resolved.");
                total--;
            } catch (Exception ex) {
                log.warning("Object type '" + indexRow.getType() + "' with essence '" + indexRow.toJsonString() + "' can not be resolved.\nError: " + ex.getMessage());
                total--;
            }
        }

        queryResult.setOffset(offset);
        queryResult.setTotalCount(total);
        return queryResult;
    }

    /**
	 * Perform indexing on the catalog data to support searching. 
     * @throws Exception an exception will be thrown if catalog is not valid or index creation fails
	 */
	private void refreshIndex() throws Exception {
        // index only on the first search call to create the searcher object
        if(indexer != null) 
            return;

        try {

            indexer = new Indexer(getLastModified());
            validate();
            indexCatalog();
        } catch (Exception ex) {
            log.log(Level.SEVERE, "Failed to create indexes with error message: \n" + ex.toString(), ex);
            // reset indexHelper to null
            indexer = null;
            // rethrow the exception
            throw ex;
        }
    }

    /***************************************************************************************************
     * Public convenient methods
     ***************************************************************************************************/
    /**
     * Gets a list of base measurements, {@link MeasurementImpl}s.
     * @return a list of base measurements
     */
    public List<MeasurementImpl> getBaseMeasurements() { return new ArrayList<MeasurementImpl>(this.baseMeasurements); }

    /**
     * Gets a list of non-base measurements, {@link MeasurementImpl}s.
     * @return a list of non-base measurements
     */
    public List<MeasurementImpl> getChildMeasurements() { return new ArrayList<MeasurementImpl>(this.childMeasurements); }

    /**
     * Gets a list of units, {@link UnitImpl}s.
     * @return a list of units
     */
    public List<UnitImpl> getUnits() {  return new ArrayList<>(this.units);  }

    /**
     * Gets a list of unit systems, {@link UnitSystemImpl}s.
     * @return a list of unit systems
     */
    public List<UnitSystemImpl> getUnitSystems() { return new ArrayList<>(this.unitSystems); }

    /**
     * Gets a list of measurement maps, {@link MeasurementMapImpl}s.
     * @return a list of measurement maps
     */
    public List<MeasurementMapImpl> getMeasurementMaps() { return new ArrayList<>(this.measurementMaps); }

    /**
     * Gets a list of unit maps, {@link UnitMapImpl}s.
     * @return a list of unit maps
     */
    public List<UnitMapImpl> getUnitMaps() { return new ArrayList<>(this.unitMaps); }

    /**
     * Gets a list of well-known map states, {@link MapStateImpl}s.
     * @return a list of well-known map states
     */
    public List<MapStateImpl> getWellknownMapStates() { return new ArrayList<>(this.wellknownMapStates); }

    /**
     * Gets the measurement map, {@link MeasurementMapImpl}, for the given fromNamespace and toNamespace from the catalog.
     * @param fromNamespace from namespace
     * @param toNamespace to namespace
     * @return measurement map
     * @throws IllegalArgumentException an exception will be thrown if any namespace is null or empty.
     */
    public MeasurementMapImpl getMeasurementMap(String fromNamespace, String toNamespace) throws IllegalArgumentException {
        if(Utility.isNullOrEmpty(fromNamespace))
            throw new IllegalArgumentException("The fromNamespace can not be null");
        if(Utility.isNullOrEmpty(toNamespace))
            throw new IllegalArgumentException("The toNamespace can not be null");

        for (MeasurementMapImpl measurementMap : measurementMaps) {
            if(measurementMap.getFromNamespace().equals(fromNamespace) &&
               measurementMap.getToNamespace().equals(toNamespace))
                return measurementMap;
        }

        return null;
    }

    /**
     * Gets the unit map, {@link UnitMapImpl}, for the given fromNamespace and toNamespace from the catalog.
     * @param fromNamespace from namespace
     * @param toNamespace to namespace
     * @return unit map
     * @throws IllegalArgumentException an exception will be thrown if any namespace is null or empty.
     */
    public UnitMapImpl getUnitMap(String fromNamespace, String toNamespace) throws IllegalArgumentException {
        if(Utility.isNullOrEmpty(fromNamespace))
            throw new IllegalArgumentException("The fromNamespace can not be null");
        if(Utility.isNullOrEmpty(toNamespace))
            throw new IllegalArgumentException("The toNamespace can not be null");

        for (UnitMapImpl unitMap: unitMaps) {
            if(unitMap.getFromNamespace().equals(fromNamespace) &&
               unitMap.getToNamespace().equals(toNamespace))
                return unitMap;
        }

        return null;
    }

    /**
     * Validate the integrity of the catalog.
     * Note: this method should not be called as part of the instantiation of the catalog given it could slow down
     * the response of instantiation of the GAE instance.
     * This method is called if the catalog is changed when the catalog is deployed.
     * It should be used by the unit tests to validate the catalog before the (new) catalog is deployed.
     * @throws IllegalArgumentException an exception will be thrown if there is integrity issue.
     */
    public void validate() throws IllegalArgumentException {
        log.info("Validate the catalog...");
        StringBuilder errorMessageBuilder = new StringBuilder();
        Map<String, List<MeasurementImpl>> codeMeasurements = new HashMap<>();

        // Test base measurements
        for (MeasurementImpl measurement : baseMeasurements) {
            String msg = validateBaseMeasurement(measurement);
            if(msg != null)
                errorMessageBuilder.append(msg + "\n");

            // To test the global uniqueness of the measurement's code
            String code = measurement.getCode();
            if(!Utility.isNullOrEmpty(code)) {
                List<MeasurementImpl> measurements;
                if(codeMeasurements.containsKey(code))
                    measurements = codeMeasurements.get(code);
                else {
                    measurements = new ArrayList<>();
                    codeMeasurements.put(code, measurements);
                }
                measurements.add(measurement);
            }
        }

        // Test child measurements
        for (MeasurementImpl measurement : childMeasurements) {
            String msg = validateChildMeasurement(measurement);
            if(msg != null)
                errorMessageBuilder.append(msg + "\n");

            // To test the global uniqueness of the measurement's code
            String code = measurement.getCode();
            if(!Utility.isNullOrEmpty(code)) {
                List<MeasurementImpl> measurements;
                if(codeMeasurements.containsKey(code))
                    measurements = codeMeasurements.get(code);
                else {
                    measurements = new ArrayList<>();
                    codeMeasurements.put(code, measurements);
                }
                measurements.add(measurement);
            }
        }

        // Test the global uniqueness of the measurements' codes
        for (Map.Entry<String, List<MeasurementImpl>> entry: codeMeasurements.entrySet()) {
            if(entry.getValue().size() <= 1)
                continue;
            StringBuilder sb = new StringBuilder();
            for (MeasurementImpl measurement : entry.getValue()) {
                sb.append(measurement.getInternalID() + " ");
            }
            String msg = "Duplicate measurement code '" + entry.getKey() + "' exits in measurements with ID: " + sb.toString();
            errorMessageBuilder.append(msg + "\n");
        }

        // Test units
        for (UnitImpl unit: units) {
            String msg = validateUnit(unit);
            if(msg != null)
                errorMessageBuilder.append(msg + "\n");
        }

        // Test mandatory WellKnownStates
        String wellKnownMapStateMsg = validateWellknownMapStates();
        if(wellKnownMapStateMsg != null)
            errorMessageBuilder.append(wellKnownMapStateMsg + "\n");

        if(errorMessageBuilder.length() > 0)
           throw new IllegalArgumentException(errorMessageBuilder.toString());
        log.info("Catalog has been validated successfully.");
    }

    private String validateBaseMeasurement(MeasurementImpl baseMeasurement) {
        StringBuilder errorMessageBuilder = new StringBuilder();
        if(Utility.isNullOrEmpty(baseMeasurement.getCode()))
            errorMessageBuilder.append("code is null. ");
        if(Utility.isNullOrEmpty(baseMeasurement.getDimensionCode()))
            errorMessageBuilder.append("dimension code is null. ");
        if(Utility.isNullOrEmpty(baseMeasurement.getName()))
            errorMessageBuilder.append("name is null. ");
        if(Utility.isNullOrEmpty(baseMeasurement.getNamespace()))
            errorMessageBuilder.append("namespace is null. ");
        if(Utility.isNullOrEmpty(baseMeasurement.getEssence().getAncestry()))
            errorMessageBuilder.append("ancestry is null. ");
        if(baseMeasurement.getUnits() == null ||baseMeasurement.getUnits().size() == 0)
            errorMessageBuilder.append("has not unit associated. ");
        if(errorMessageBuilder.length() > 0) {
            return "Base measurement with internal ID " + baseMeasurement.getInternalID() + " is invalid: " + errorMessageBuilder;
        }
        return null;
    }

    private String validateChildMeasurement(MeasurementImpl childMeasurement) {
        StringBuilder errorMessageBuilder = new StringBuilder();
        if(Utility.isNullOrEmpty(childMeasurement.getCode()))
            errorMessageBuilder.append("code is null. ");
        if(Utility.isNullOrEmpty(childMeasurement.getDimensionCode()))
            errorMessageBuilder.append("dimension code is null. ");
        if(Utility.isNullOrEmpty(childMeasurement.getName()))
            errorMessageBuilder.append("name is null. ");
        if(Utility.isNullOrEmpty(childMeasurement.getNamespace()))
            errorMessageBuilder.append("namespace is null. ");
        if(Utility.isNullOrEmpty(childMeasurement.getEssence().getAncestry()))
            errorMessageBuilder.append("ancestry is null. ");
        if(childMeasurement.getParent() == null)
            errorMessageBuilder.append("parent is null. ");
        if(errorMessageBuilder.length() > 0) {
            return "Child measurement with internal ID " + childMeasurement.getInternalID() + " is invalid: " + errorMessageBuilder;
        }
        return null;
    }

    private String validateUnit(UnitImpl unit) {
        UnitEssenceImpl essence = unit.getEssence();

        StringBuilder errorMessageBuilder = new StringBuilder();
        if(essence.getSymbol() == null) // empty symbol with space is valid
            errorMessageBuilder.append("symbol is null. ");
        if(essence.getBaseMeasurement() == null)
            errorMessageBuilder.append("base measurement is null. ");

        if(essence.getABCD() != null && essence.getScaleOffset() != null)
            errorMessageBuilder.append("ambiguous factor as both ScaleOffset and ABCD are defined. ");
        else if(essence.getABCD() == null && essence.getScaleOffset() == null)
            errorMessageBuilder.append("missing factor as neither ScaleOffset nor ABCD is defined. ");
        else if(essence.getABCD() != null && !essence.getABCD().isValid())
            errorMessageBuilder.append("ABCD definition is invalid. ");
        else if(essence.getScaleOffset() != null && !essence.getScaleOffset().isValid())
            errorMessageBuilder.append("ScaleOffset definition is invalid. ");
        if(errorMessageBuilder.length() > 0) {
            return "Unit with internal ID " + unit.getInternalID() + " is invalid: " + errorMessageBuilder;
        }
        return null;
    }

    private String validateWellknownMapStates() {
        String[] mandatoryStates = new String[] {"identical", "precision", "corrected", "conversion","conditional","unsupported"};
        StringBuilder missingStates = new StringBuilder();
        for (String state: mandatoryStates) {
            boolean defined = false;
            for (MapStateImpl mapState : wellknownMapStates) {
                if(state.equals(mapState.getState())) {
                    defined = true;
                    continue;
                }
            }
            if(!defined)
                missingStates.append(state + " ");
        }
        if(missingStates.length() > 0) {
            return "Following wellknown map states are not defined: " + missingStates.toString();
        }
        return null;
    }

    /***************************************************************************************************
     * Helper methods to initialize the catalog after the catalog is populated from catalog file
     ***************************************************************************************************/
    private void initCatalog() {
        //Build the map for fast match
        Map<String, MeasurementImpl> idMeasurements = new HashMap<>();
        Map<String, UnitImpl> idUnits = new HashMap<>();
        for(MeasurementImpl measurement : baseMeasurements) {
            String id = measurement.getInternalID();
            if(idMeasurements.containsKey(id)) {
                String ref1 = measurement.getEssence().toJsonString();
                String ref2 = idMeasurements.get(id).getEssence().toJsonString();
                throw new IllegalArgumentException("Unexpected duplicate id for measurements: \n" + ref1 + "\n" + ref2);
            }
            measurement.setIsBaseMeasurement(true);
            idMeasurements.put(measurement.getInternalID(), measurement);
        }
        for(MeasurementImpl measurement : childMeasurements) {
            String id = measurement.getInternalID();
            if(idMeasurements.containsKey(id)) {
                String ref1 = measurement.getEssence().toJsonString();
                String ref2 = idMeasurements.get(id).getEssence().toJsonString();
                throw new IllegalArgumentException("Unexpected duplicate id for measurements: \n" + ref1 + "\n" + ref2);
            }
            measurement.setIsBaseMeasurement(false);
            idMeasurements.put(measurement.getInternalID(), measurement);
        }

        hashCodeMeasurements = createHashCodeMeasurements();

        for (UnitImpl unit : units) {
            String id = unit.getInternalID();
            if(idUnits.containsKey(id)) {
                String ref1 = unit.getEssence().toJsonString();
                String ref2 = idUnits.get(id).getEssence().toJsonString();
                throw new IllegalArgumentException("Unexpected duplicate id for units: \n" + ref1 + "\n" + ref2);
            }

            idUnits.put(id, unit);
        }
        this.hashCodeUnits = createHashCodeUnits();
        this.symbolUnits = createSymbolUnits();

        // post deserialization
        // post deserialization of measurements
        for(MeasurementImpl measurement : baseMeasurements) {
            measurement.postDeserialization(idMeasurements, idUnits);

            MeasurementDeprecationInfoImpl deprecationInfo = measurement.getDeprecationInfo();
            if(deprecationInfo != null)
                deprecationInfo.postDeserialization(idMeasurements);
        }
        for(MeasurementImpl measurement : childMeasurements) {
            measurement.postDeserialization(idMeasurements, idUnits);

            MeasurementDeprecationInfoImpl deprecationInfo = measurement.getDeprecationInfo();
            if(deprecationInfo != null)
                deprecationInfo.postDeserialization(idMeasurements);
        }

        // post deserialization of units and base measurements
        for (UnitImpl unit : units) {
            MeasurementEssence  measurementEssence = unit.getEssence().getBaseMeasurementEssence();
            MeasurementImpl baseMeasurement = null;
            try{
                baseMeasurement = postMeasurement(measurementEssence);
            }
            catch(Exception ex) {
                log.log(Level.SEVERE, "Failed to find measurement with error message: \n" + ex.toString(), ex);
                return;
            }
            String baseMeasurementID = baseMeasurement.getInternalID();
            // String baseMeasurementID = unit.getBaseMeasurementID();
            if(!idMeasurements.containsKey(baseMeasurementID))
                throw new IllegalArgumentException("Base measurement id '" + baseMeasurementID + "' in unit with id '"
                        + unit.getInternalID() + "' does not have measurement definition associated.");
            baseMeasurement.addUnit(unit);
            unit.getEssence().setBaseMeasurement((MeasurementImpl)baseMeasurement);

            UnitDeprecationInfoImpl deprecationInfo = unit.getDeprecationInfo();
            if(deprecationInfo != null)
                deprecationInfo.postDeserialization(idUnits);
        }

        // post deserialization of MeasurementMap
        for(MeasurementMapImpl measurementMap : measurementMaps) {
            for (MeasurementMapItem measurementMapItem: measurementMap.getMeasurementMapItems()) {
                ((MeasurementMapItemImpl)measurementMapItem).postDeserialization(idMeasurements, measurementMap.getFromNamespace(), measurementMap.getToNamespace());
                this.measurementMapItemCount++;
            }
        }

        // post deserialization of UnitMap
        for(UnitMapImpl unitMap : unitMaps) {
            for(UnitMapItem unitMapItem : unitMap.getUnitMapItems()) {
                ((UnitMapItemImpl)unitMapItem).postDeserialization(idUnits, unitMap.getFromNamespace(), unitMap.getToNamespace());
                this.unitMapItemCount++;
            }
        }
        this.nameUnitSystems = createNameUnitSystems();
        this.hashCodeUnitSystems = createHashCodeUnitSystems();

        // post deserialization of unit systems
        for (UnitSystemImpl unitSystem : unitSystems) {
            unitSystem.postDeserialization(unitSystems);

            for(UnitAssignment unitAssignment : unitSystem.getNativeUnitAssignments()) {
                ((UnitAssignmentImpl)unitAssignment).postDeserialization(idMeasurements, idUnits);
            }
        }

        // Currently, our codes only refer to MapStates 'identical' and 'unresolved'
        for ( MapStateImpl mapState : getWellknownMapStates()) {
            if(MapStateHelper.isUnresolved(mapState.getState()))
                MapStateHelper.setUnresolvedMapState(mapState);
            if(MapStateHelper.isIdentical(mapState.getState()))
                MapStateHelper.setIdenticalState(mapState);
        }

        indexer = null;
    }

     /***************************************************
     * Wrapper for Read/Search API
     ***************************************************/
    /***************************************************************************************************
     * Public convenient methods
     ***************************************************************************************************/
    /**
     * Gets a list of {@link MeasurementImpl} from the catalog.
     * @return a list of {@link MeasurementImpl}
     */
    public List<MeasurementImpl> getMeasurements() {
        List<MeasurementImpl> measurements = getBaseMeasurements();
        measurements.addAll(getChildMeasurements());
        return measurements;
    }

    /**
     * A copy of the catalog that includes
     * <ul>
     *     <li>{@link Unit} collection</li>
     *     <li>{@link Measurement} collection</li>
     *     <li>{@link UnitSystem} collection</li>
     *     <li>{@link UnitMap} collection</li>
     *     <li>{@link MeasurementMap} collection</li>
     *     <li>{@link MapState} collection</li>
     * </ul>
     * @return A copy of the catalog
     */
    public CatalogResponse getCatalogResponse() {
        CatalogResponse catalogResponse = new CatalogResponse();
        catalogResponse.setLastModified(getLastModified());

        catalogResponse.setTotalMeasurementCount(getMeasurements().size());
        catalogResponse.setTotalUnitCount(getUnits().size());

        catalogResponse.setTotalUnitSystemCount(getUnitSystems().size());
        catalogResponse.setTotalUnitMapCount(this.unitMapItemCount);
        catalogResponse.setTotalMeasurementMapCount(this.measurementMapItemCount);
        catalogResponse.setTotalMapStateCount(getWellknownMapStates().size());

        return catalogResponse;
    }
   
    /********************************************
     Help Methods for unit systems
     *********************************************/
    private Map<String, UnitSystemImpl> createNameUnitSystems() {
        Map<String, UnitSystemImpl> nameUnitSystems = new HashMap<>();
        String name;
        for (UnitSystemImpl unitSystem : getUnitSystems()) {
            name = unitSystem.getName();
            if(nameUnitSystems.containsKey(name)) {
                log.warning("Unexpected duplicate unit system name: " + name);
                continue;
            }

            nameUnitSystems.put(name, unitSystem);
        }

        log.info(String.format("Completed the initialization of the nameUnitSystems which size is %d", nameUnitSystems.size()));
        return nameUnitSystems;
    }

    private Map<String, UnitSystemImpl> createHashCodeUnitSystems() {
        Map<String, UnitSystemImpl> hashCodeUnitSystems = new HashMap<>();
        String hashCode;
        for (UnitSystemImpl unitSystem : getUnitSystems()) {
            hashCode = Integer.valueOf(unitSystem.hashCode()).toString();
            if(hashCodeUnitSystems.containsKey(hashCode)) {
                String ref1 = unitSystem.getEssence().toJsonString();
                String ref2 = hashCodeUnitSystems.get(hashCode).getEssence().toJsonString();
                log.warning("Unexpected duplicate unit systems: \n" + ref1 + "\n" + ref2);

                continue;
            }

            hashCodeUnitSystems.put(hashCode, unitSystem);
        }

        log.info(String.format("Completed the initialization of the hashCodeUnitSystems which size is %d", hashCodeUnitSystems.size()));
        return hashCodeUnitSystems;
    }


    /********************************************
     Help Methods for measurements
     *********************************************/
    private Map<String, MeasurementImpl> createHashCodeMeasurements() {
        Map<String, MeasurementImpl> hashCodeMeasurements = new HashMap<>();
        String hashCode;
        for (MeasurementImpl baseMeasurement : getBaseMeasurements()) {
            hashCode = Integer.valueOf(baseMeasurement.hashCode()).toString();
            if(hashCodeMeasurements.containsKey(hashCode)) {
                String ref1 = baseMeasurement.getEssence().toJsonString();
                String ref2 = hashCodeMeasurements.get(hashCode).getEssence().toJsonString();
                log.warning("Unexpected duplicate measurements: \n" + ref1 + "\n" + ref2);
                continue;
            }

            hashCodeMeasurements.put(hashCode, baseMeasurement);
        }
        for (MeasurementImpl childMeasurement : getChildMeasurements()) {
            hashCode = Integer.valueOf(childMeasurement.hashCode()).toString();
            if(hashCodeMeasurements.containsKey(hashCode)) {
                String ref1 = childMeasurement.getEssence().toJsonString();
                String ref2 = hashCodeMeasurements.get(hashCode).getEssence().toJsonString();
                log.warning("Unexpected duplicate measurements: \n" + ref1 + "\n" + ref2);
                continue;
            }

            hashCodeMeasurements.put(hashCode, childMeasurement);
        }

        log.info(String.format("Completed the initialization of the hashCodeMeasurements which size is %d", hashCodeMeasurements.size()));
        return hashCodeMeasurements;
    }

    private MeasurementImpl createMeasurement(MeasurementEssence essence, boolean markDeprecation) throws IllegalArgumentException
    {
        if(essence == null) {
            throw new IllegalArgumentException("measurement essence cannot be null");
        }
        MeasurementImpl measurement = new MeasurementImpl();
        if(essence.getAncestry().contains(Utility.AncestryDelimiter))
            measurement.setIsBaseMeasurement(false);
        else
            measurement.setIsBaseMeasurement(true);
        measurement.setEssence((MeasurementEssenceImpl)essence);

        if(markDeprecation) {
            MapStateImpl unresolved = MapStateHelper.getUnresolvedMapState();
            MeasurementDeprecationInfoImpl deprecationInfo = new MeasurementDeprecationInfoImpl();
            deprecationInfo.setState(unresolved.getState());
            deprecationInfo.setRemarks(unresolved.getDescription());
            measurement.setDeprecationInfo(deprecationInfo);
        }

        return measurement;
    }

    /********************************************
     Helper methods for units
     *********************************************/
    private Map<String, UnitImpl> createHashCodeUnits() {
        Map<String, UnitImpl> hashCodeUnits = new HashMap<>();
        String hashCode;
        for (UnitImpl unit : getUnits()) {
            hashCode = Integer.valueOf(unit.hashCode()).toString();
            if(hashCodeUnits.containsKey(hashCode)) {
                String ref1 = unit.getEssence().toJsonString();
                String ref2 = hashCodeUnits.get(hashCode).getEssence().toJsonString();
                log.warning("Unexpected duplicate units: \n" + ref1 + "\n" + ref2);
                continue;
            }

            hashCodeUnits.put(hashCode, unit);
        }

        log.info(String.format("Completed the initialization of the hashCodeUnits which size is %d", hashCodeUnits.size()));
        return hashCodeUnits;
    }

    private Map<String, List<UnitImpl>> createSymbolUnits() {
        Map<String, List<UnitImpl>> symbolUnits = new HashMap<>();
        for (UnitImpl unit : getUnits()) {
            String symbol = unit.getEssence().getSymbol();
            List<UnitImpl> unitSet;
            if(symbolUnits.containsKey(symbol)) {
                unitSet = symbolUnits.get(symbol);
            }
            else {
                unitSet = new ArrayList<>();
                symbolUnits.put(symbol, unitSet);
            }
            unitSet.add(unit);
        }

        log.info(String.format("Completed the initialization of the symbolUnits which size is %d", symbolUnits.size()));
        return symbolUnits;
    }

    public ScaleOffsetImpl getConversionScaleOffset(UnitImpl sourceUnit, UnitImpl targetUnit) {
        assertValidConversion(sourceUnit, targetUnit);

        ScaleOffsetImpl sourceScaleOffset = getScaleOffset(sourceUnit);
        ScaleOffsetImpl targetScaleOffset = getScaleOffset(targetUnit);

        // ValueInSI = scale1 * (x1 - offset1) -- x1 in source unit
        // ValueInSI = scale2 * (x2 - offset2) -- x2 in target unit
        // => x2 = (scale1/scale2) * { x1 - [offset1 - (scale2/scale1) * offset2]}
        // => scale = scale1/scale2, offset = offset1 - offset2/scale
        double scale = sourceScaleOffset.getScale()/targetScaleOffset.getScale();
        double offset = sourceScaleOffset.getOffset() - targetScaleOffset.getOffset()/scale;
        return new ScaleOffsetImpl(scale, offset);
    }

    public ABCDImpl getConversionABCD(UnitImpl sourceUnit, UnitImpl targetUnit) {
        assertValidConversion(sourceUnit, targetUnit);

        ABCDImpl sourceABCD = getABCD(sourceUnit);
        ABCDImpl targetABCD = getABCD(targetUnit);

        // ValueInSI = (A1 + B1 * x1)/(C1 + D1 * x1 ) -- x1 in source unit
        // ValueInSI = (A2 + B2 * x2)/(C2 + D2 * x2 ) -- x2 in target unit
        // => x2 = [(A1*C2 - C1*A2) + (B1*C2 - D1*A2)*X1]/[(C1*B2 - A1*D2) + (D1*B2 - B1*D2)*X1]
        // => A = A1*C2 - C1*A2, B = B1*C2 - D1*A2, C = C1*B2 - A1*D2, D = D1*B2 - B1*D2
        double a = sourceABCD.getA() * targetABCD.getC() - sourceABCD.getC() * targetABCD.getA();
        double b = sourceABCD.getB() * targetABCD.getC() - sourceABCD.getD() * targetABCD.getA();
        double c = sourceABCD.getC() * targetABCD.getB() - sourceABCD.getA() * targetABCD.getD();
        double d = sourceABCD.getD() * targetABCD.getB() - sourceABCD.getB() * targetABCD.getD();

        if(b < 0) {
            // To avoid the weird negative coefficients, we should invert the flags for all of them
            // e.g. from "ft" to "m", a = 0, b = -0.3048, c = -1, d = 0
            b *= -1;
            if(!Utility.isZeroValue(a))
                a *= -1; // Avoid -0.0
            if(!Utility.isZeroValue(c))
                c *= -1; // Avoid -0.0
            if(!Utility.isZeroValue(d))
                d *= -1; // Avoid -0.0
        }
        //Normalize c
        if(!Utility.isEqual(c, 1.0) && !Utility.isZeroValue(c)) {
            double normalizedFactor = c;
            a /= c;
            b /= c;
            c = 1.0;
            d /= c;
        }

        return new ABCDImpl(a,b,c,d);
    }

    private ABCDImpl getABCD(UnitImpl unit)  {
        UnitEssenceImpl essence = unit.getEssence();
        if(isValid(essence.getABCD()))
            return essence.getABCD();

        if(isValid(essence.getScaleOffset()))
            return toABCD(essence.getScaleOffset());

        return null;
    }

    private static ScaleOffsetImpl getScaleOffset(UnitImpl unit)  {
        UnitEssenceImpl essence = unit.getEssence();
        if(isValid(essence.getScaleOffset()))
            return essence.getScaleOffset();

        if(isValid(essence.getABCD()))
            return toScaleOffset(essence.getABCD());

        return null;
    }

    private void assertValidConversion(UnitImpl sourceUnit, UnitImpl targetUnit) throws IllegalArgumentException {
        if(!isCompatible(sourceUnit, targetUnit))
            throw new IllegalArgumentException("The source/target unitEssences are inconvertible");

        UnitEssenceImpl sourceEssence = sourceUnit.getEssence();
        if(sourceEssence == null || (!isValid(sourceEssence.getABCD()) && !isValid(sourceEssence.getScaleOffset()))) {
            throw new IllegalArgumentException("The source unit does not have valid definition");
        }
        UnitEssenceImpl targetEssence = targetUnit.getEssence();
        if(targetEssence == null || (!isValid(targetEssence.getABCD()) && !isValid(targetEssence.getScaleOffset()))) {
            throw new IllegalArgumentException("The target unit does not have valid definition");
        }
    }

    private boolean isCompatible(UnitImpl fromUnit, UnitImpl toUnit) {
        MeasurementEssence fromMeasurementEssence = fromUnit.getEssence().getBaseMeasurementEssence();
        MeasurementEssence toMeasurementEssence = toUnit.getEssence().getBaseMeasurementEssence();

        if(fromMeasurementEssence == null || toMeasurementEssence == null) {
            return false;
        }

        // Test unitEssences' basement measurements
        if(fromMeasurementEssence.equals(toMeasurementEssence))
            return true;

        MeasurementImpl fromMeasurement = fromUnit.getEssence().getBaseMeasurement();
        MeasurementImpl toMeasurement = toUnit.getEssence().getBaseMeasurement();

        if(fromMeasurement == null || toMeasurement == null) {
            return false;
        }

        // Test the unit map
        try {
            UnitMapImpl unitMap = getUnitMap(fromUnit.getNamespace(), toUnit.getNamespace());
            if(unitMap != null && unitMap.isMatch(fromUnit, toUnit))
                return true;

            UnitMapImpl inverseUnitMap = getUnitMap(toUnit.getNamespace(), fromMeasurement.getNamespace());
            if(inverseUnitMap != null && inverseUnitMap.isMatch(toUnit, fromUnit))
                return true;
        }
        catch(Exception ex) {
            //Ignore
        }

        // Test the measurement map
        try {
            MeasurementMapImpl measurementMap = getMeasurementMap(fromMeasurement.getNamespace(), toMeasurement.getNamespace());
            if(measurementMap != null && measurementMap.isMatch(fromMeasurement, toMeasurement))
                return true;

            MeasurementMapImpl inverseMeasurementMap = getMeasurementMap(toMeasurement.getNamespace(), fromMeasurement.getNamespace());
            if(inverseMeasurementMap != null && inverseMeasurementMap.isMatch(toMeasurement, fromMeasurement))
                return true;
        }
        catch (Exception ex) {
            //Ignore
        }

        return false;
    }

    private static boolean isValid(ABCDImpl abcd) {
        return abcd != null && abcd.isValid();
    }

    private static boolean isValid(ScaleOffsetImpl scaleOffset) {
        return scaleOffset != null && scaleOffset.isValid();
    }

    private static ABCDImpl toABCD(ScaleOffsetImpl scaleOffset) {
        if(!isValid(scaleOffset))  return null;

        // Y = (A + B*X)/(C + D*X) = Scale * (X - Offset)
        // convert to scaleOffset from abcd
        // Assume d = 0, c = 1
        // Y = Scale (X - Offset) = [(-Scale * Offset) + Scale * X]/(1 + 0 * X)
        // => a = -Scale * Offset, b = Scale
        double c = 1;
        double d = 0;
        double a = scaleOffset.getScale() * scaleOffset.getOffset();
        if(!Utility.isZeroValue(a))
            a *= -1;
        double b = scaleOffset.getScale();
        return new ABCDImpl(a,b,c,d);
    }

    private static ScaleOffsetImpl toScaleOffset(ABCDImpl abcd) {
        if(!isValid(abcd)) return null;

        // Y = (A + B*X)/(C + D*X) = Scale * (X - Offset)
        // convert to scaleOffset from abcd
        // when b != 0 && c != 0 and d == 0
        if(abcd.getB() != 0 && abcd.getC() != 0 && abcd.getD() == 0 )
        {
            // Y = (B/C)*(X - (-A/B))
            // => Scale = B/C, Offset = -A/B
            double scale = abcd.getB()/abcd.getC();
            double offset = abcd.getA()/abcd.getB();
            if(!Utility.isZeroValue(offset))
                offset *= -1;

            return new ScaleOffsetImpl(scale, offset);
        }

        return null;
    }

    private UnitImpl[] getCandidateUnits(String namespaces,
                                         String fromSymbol,
                                         String toSymbol) throws Exception {
        if(Utility.isNullOrEmpty(namespaces))
            throw new IllegalArgumentException("The namespace list can not be null");
        if(Utility.isNullOrEmpty(fromSymbol))
            throw new IllegalArgumentException("The symbol of fromUnit can not be null");
        if(Utility.isNullOrEmpty(toSymbol))
            throw new IllegalArgumentException("The symbol of toUnit can not be null");

        Result<UnitImpl> fromUnits = getUnitsBySymbol(fromSymbol);
        Result<UnitImpl> toUnits = getUnitsBySymbol(toSymbol);
        UnitImpl fromUnit = null;
        UnitImpl toUnit = null;
        for (String namespace:namespaces.split(",")) {
            if(fromUnit == null) {
                for (UnitImpl unit : fromUnits.getItems()) {
                    if(namespace.equals(unit.getNamespace()))
                    {
                        fromUnit = unit;
                        break;
                    }
                }
            }
            if(toUnit == null) {
                for (UnitImpl unit : toUnits.getItems()) {
                    if(namespace.equals(unit.getNamespace()))
                    {
                        toUnit = unit;
                        break;
                    }
                }
            }
            if(fromUnit != null && toUnit != null)
                break;
        }

        StringBuilder stringBuilder = new StringBuilder();
        if(fromUnit == null)
            stringBuilder.append("Cannot find a Unit for fromSymbol '" + fromSymbol + "' in the given namespaces. ");
        if(toUnit == null)
            stringBuilder.append("Cannot find a Unit for toSymbol '" + toSymbol + "' in the given namespaces.");
        if(stringBuilder.length() > 0)
            throw new IllegalArgumentException(stringBuilder.toString());
        return new UnitImpl[] {fromUnit, toUnit};
    }


    /**
     * Method to index the catalog.
     */
    public void indexCatalog() {
        if (indexer == null)
            return;

        try {
            log.info("Indexing the catalog...");
            indexer.ensureWriterOpen();

            indexUnits();
            indexMeasurements();
            indexUnitMaps();
            indexMeasurementMaps();

            indexer.commit();
            log.info("Catalog has been indexed successfully.");
        } catch (Exception ex) {
            log.log(Level.SEVERE, "Failed to update the index of the catalog\n" + ex.toString(), ex);
        }
    }

    /**
     * Uses keyword to search measurements, {@link MeasurementImpl}s and returns the result based on range parameters.
     * @param keyword keyword for search
     * @param offset offset of the return in all the search result.
     * @param limit maximum size of the return
     * @return a list of rows, {@link IndexRow}, that contains minimum identity properties,
     * such as id, type and reference.
     * @throws Exception an exception will be thrown if any of the following conditions is met:
     * <ol>
     *     <li>The keyword is null or uses invalid syntax</li>
     *     <li>The offset is negative or too large</li>
     *     <li>The index was not created</li>
     * </ol>
     */
    public Result<IndexRow> searchMeasurement(String keyword, int offset, int limit) throws Exception {
        assertSearch();

        SearchInput searchInput = new SearchInput(keyword, LuceneConstants.Type_Measurement);
        searchInput.setOffset(offset);
        searchInput.setSize(limit);

        return indexer.search(searchInput);
    }

    /**
     * Uses keyword to search units, {@link UnitImpl}s and returns the result based on range parameters.
     * @param keyword keyword for search
     * @param offset offset of the return in all the search result.
     * @param limit maximum size of the return
     * @return a list of rows, {@link IndexRow}, that contains minimum identity properties,
     * such as id, type and reference.
     * @throws Exception an exception will be thrown if any of the following conditions is met:
     * <ol>
     *     <li>The keyword is null or uses invalid syntax</li>
     *     <li>The offset is negative or too large</li>
     *     <li>The index was not created</li>
     * </ol>
     */
    public Result<IndexRow> searchUnit(String keyword, int offset, int limit) throws Exception {
        assertSearch();

        SearchInput searchInput = new SearchInput(keyword, LuceneConstants.Type_Unit);
        searchInput.setOffset(offset);
        searchInput.setSize(limit);

        return indexer.search(searchInput);
    }

    /***************************************************************************************************
     * Helper methods to initialize the catalog index after the catalog is populated from catalog file
     ***************************************************************************************************/
    private void assertSearch() {
        if(indexer != null)
            return;

        throw new IllegalArgumentException("Search can not be completed because of system error.");
    }

    private void indexUnits() {

        for (UnitImpl unit : getUnits()) {
            try {
                IndexRow indexRow = UnitConverter.toIndexRow(unit);
                indexer.index(indexRow);
            } catch (Exception ex) {
                log.log(Level.SEVERE, "Failed to index unit with reference '" + unit.getEssence().toJsonString() + "'\n" + ex.toString(), ex);
            }
        }
    }

    private void indexMeasurements() {
        for (MeasurementImpl measurement : getBaseMeasurements()) {
            try {
                IndexRow indexRow = MeasurementConverter.toIndexRow(measurement);
                indexer.index(indexRow);
            } catch (Exception ex) {
                log.log(Level.SEVERE, "Failed to index unit with reference '" + measurement.getEssence().toJsonString() + "'\n" + ex.toString(), ex);
            }
        }

        for (MeasurementImpl measurement : getChildMeasurements()) {
            try {
                IndexRow indexRow = MeasurementConverter.toIndexRow(measurement);
                indexer.index(indexRow);
            } catch (Exception ex) {
                log.log(Level.SEVERE, "Failed to index unit with reference '" + measurement.getEssence().toJsonString() + "'\n" + ex.toString(), ex);
            }
        }
    }

    private void indexUnitMaps() {
        for(UnitMapImpl unitMap : getUnitMaps()) {
            for (UnitMapItem unitMapItem : unitMap.getUnitMapItems()) {
                String fromUnitSymbol = unitMapItem.getFromUnit().getEssence().getSymbol();
                String toUnitSymbol = unitMapItem.getToUnit().getEssence().getSymbol();

                try {
                    IndexRow indexRow = UnitMapItemConverter.toIndexRow((UnitMapItemImpl)unitMapItem);
                    indexer.index(indexRow);
                } catch (Exception ex) {
                    log.log(Level.SEVERE, "Failed to index unit map between units '" +
                            fromUnitSymbol + " and " + toUnitSymbol + "'\n" + ex.toString(), ex);
                }
            }
        }
    }

    private void indexMeasurementMaps() {
        for(MeasurementMapImpl measurementMap : getMeasurementMaps()) {
            for (MeasurementMapItem measurementMapItem : measurementMap.getMeasurementMapItems()) {
                String fromMeasurementAncestry = measurementMapItem.getFromMeasurement().getEssence().getAncestry();
                String toMeasurementAncestry = measurementMapItem.getFromMeasurement().getEssence().getAncestry();
                try {
                    IndexRow indexRow = MeasurementMapItemConverter.toIndexRow((MeasurementMapItemImpl)measurementMapItem);
                    indexer.index(indexRow);
                } catch (Exception ex) {
                    log.log(Level.SEVERE, "Failed to index unit map between measurements '" +
                            fromMeasurementAncestry + " and " + toMeasurementAncestry + "'\n" + ex.toString(), ex);
                }
            }
        }
    }


}
