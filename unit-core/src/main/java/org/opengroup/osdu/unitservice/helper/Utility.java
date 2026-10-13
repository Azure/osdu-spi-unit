package org.opengroup.osdu.unitservice.helper;

import com.google.gson.JsonSyntaxException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.opengroup.osdu.unitservice.model.CatalogImpl;
import org.opengroup.osdu.unitservice.model.MeasurementImpl;
import org.opengroup.osdu.unitservice.model.UnitImpl;
import org.opengroup.osdu.unitservice.model.extended.QueryResultImpl;
import org.opengroup.osdu.unitservice.model.extended.Result;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A utility class to share common utility methods
 */
public final class Utility {
    public static final String AncestryDelimiter = ".";

    /**
     * Compares two double values whether they are equal
     * @param a double value to be compared
     * @param b double value to be compared
     * @return true if none of them is Double.NaN and the difference is not
     * more than Double.MIN_VALUE; otherwise, return false.
     */
    public static boolean isEqual(double a, double b) {
        if(Double.isNaN(a) || Double.isNaN(b))
            return false;

        return Math.abs(a - b) <= Double.MIN_VALUE;
    }

    /**
     * Checks whether a double value is 0
     * @param a double value to be checked
     * @return true of the difference between a and 0 is not more than Double.MIN_VALUE;
     * otherwise, return false;
     */
    public static boolean isZeroValue(double a) {
        return isEqual(a, 0);
    }

    /**
     * Checks whether a string is null or empty
     * @param value string value to be checked.
     * @return true if the string is null or empty;
     * otherwise, returns false;
     */
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }

    /**
     * Checks whether a string is null, empty or whitespaces
     * @param value string value to be checked.
     * @return true if the string is not null, not empty and not a string with whitespaces only;
     * otherwise, returns false;
     */
    public static boolean isNullOrWhiteSpace(String value) {
        return value == null || value.trim().equals("");
    }

    /**
     * Trim non-white space string.
     * Note: Pure white space string has meaning, e.g. unit symbol for dimensionless.
     * If it is a white space, don't trim it.
     * @param value string value to be checked.
     * @return trim string if it is not a white space string
     */
    public static String trim(String value) {
        if(isNullOrWhiteSpace(value))
            return value;
        return value.trim();
    }

    /**
     * Gets a subset of the object list.
     * @param objects the original object list
     * @param offset the start index in the object list
     * @param limit the maximum size of the subset
     * @param <T> type of the objects
     * @return a subset of the object list
     * @throws IndexOutOfBoundsException an exception will be thrown
     * if offset is negative or is not smaller than the size of the object list.
     */
    public static <T> List<T> getRange(List<T> objects, int offset, int limit) throws IndexOutOfBoundsException {
        if(objects == null || objects.size() == 0 || limit == 0)
            return new ArrayList<T>();

        if(offset < 0 || offset >= objects.size())
            throw new IndexOutOfBoundsException("The offset is out of the range");

        if(limit < 0 || limit > objects.size() - offset)
            limit = objects.size() - offset;

        return objects.subList(offset, limit + offset);
    }

    /******************************************************************
     Convert generic type of Result to concrete types of Results
     *******************************************************************/
    /**
     * Endpoint does not support generic type.
     * Convert a list of units from type {@link Result} to type {@link QueryResultImpl}.
     * @param unitResult a list of units in {@link Result}
     * @return a list of units in {@link QueryResultImpl}
     */
    public static QueryResultImpl createQueryResultForUnits(Result<UnitImpl> unitResult) {
        QueryResultImpl queryResult = new QueryResultImpl();
        if(unitResult == null)
            return queryResult;

        queryResult.setTotalCount(unitResult.getTotalCount());
        queryResult.setOffset(unitResult.getOffset());
        for (UnitImpl unit: unitResult.getItems()) {
            queryResult.addUnit(unit);
        }
        return queryResult;
    }

    /**
     * Endpoint does not support generic type.
     * Convert a list of units from type {@link Result} to type {@link QueryResultImpl}.
     * @param measurementResult a list of units in {@link Result}
     * @return a list of units in {@link QueryResultImpl}
     */
    public static QueryResultImpl createQueryResultForMeasurements(Result<MeasurementImpl> measurementResult) {
        QueryResultImpl queryResult = new QueryResultImpl();
        if(measurementResult == null)
            return queryResult;

        queryResult.setTotalCount(measurementResult.getTotalCount());
        queryResult.setOffset(measurementResult.getOffset());
        for (MeasurementImpl measurement: measurementResult.getItems()) {
            queryResult.addMeasurement(measurement);
        }
        return queryResult;
    }

    /********************************************
     Gson serialize and deserialize methods
     *********************************************/

    /**
     * Serialize the object to json string.
     * @param object object to be serialized.
     * @param <T> type of the object
     * @return json string
     */
    public static <T> String toJsonString(T object) {
        return createGson().toJson(object);
    }

    /**
     * Deserialize the json string to an object
     * @param jsonInString json string
     * @param type Class type of the object
     * @param <T> type of the object
     * @return an object
     * @throws JsonSyntaxException an exception will be thrown if the Gson.fromJson throws exception.
     * @see Gson#fromJson(JsonElement, Class)
     */
    public static <T> T fromJsonString(String jsonInString, Class<T> type) throws JsonSyntaxException {
        return createGson().fromJson(jsonInString, type);
    }

    public static Gson createGson() {
        GsonBuilder builder = new GsonBuilder();
        return builder.excludeFieldsWithoutExposeAnnotation().create();
    }

}
