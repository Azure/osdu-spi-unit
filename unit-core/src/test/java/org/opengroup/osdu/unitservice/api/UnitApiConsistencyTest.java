package org.opengroup.osdu.unitservice.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.opengroup.osdu.unitservice.interfaces.*;
import org.opengroup.osdu.unitservice.model.*;
import org.opengroup.osdu.unitservice.model.extended.ConversionResultImpl;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * This class is to detect any inconsistency between the API class/interface and implementation class
 * in terms of getter methods.
 * 1) Make sure that the API class/interface expose the right set of getter methods defined in the implementation class.
 * 2) Make sure that google endpoints serialization won't serialize more properties than the API class/interface defined.
 */
public class UnitApiConsistencyTest {
    @Test
    public void testJsonSerializationConsistency() {
        //<API class/interface, implementation class>
        Map<Class, Class> checkedClasses = new HashMap<>();
        checkedClasses.put(Unit.class, UnitImpl.class);
        checkedClasses.put(UnitEssence.class, UnitEssenceImpl.class);
        checkedClasses.put(ABCD.class, ABCDImpl.class);
        checkedClasses.put(ScaleOffset.class, ScaleOffsetImpl.class);
        checkedClasses.put(ConversionResult.class, ConversionResultImpl.class);
        checkedClasses.put(UnitDeprecationInfo.class, UnitDeprecationInfoImpl.class);

        checkedClasses.put(Measurement.class, MeasurementImpl.class);
        checkedClasses.put(MeasurementEssence.class, MeasurementEssenceImpl.class);
        checkedClasses.put(MeasurementDeprecationInfo.class, MeasurementDeprecationInfoImpl.class);

        checkedClasses.put(UnitSystem.class, UnitSystemImpl.class);
        checkedClasses.put(UnitAssignment.class, UnitAssignmentImpl.class);

        checkedClasses.put(UnitMap.class, UnitMapImpl.class);
        checkedClasses.put(UnitMapItem.class, UnitMapItemImpl.class);

        checkedClasses.put(MeasurementMap.class, MeasurementMapImpl.class);
        checkedClasses.put(MeasurementMapItem.class, MeasurementMapItemImpl.class);

        checkedClasses.put(MapState.class, MapStateImpl.class);

        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<Class, Class> entry: checkedClasses.entrySet()) {
            Class apiClass = entry.getKey();
            Class implClass = entry.getValue();

            List<Method> apiGetters = getGetters(apiClass);
            List<Method> implGetters = getGetters(implClass);
            List<Method> nonAPIGetters = getNonAPIGetters(apiGetters, implGetters);
            for(Method method : nonAPIGetters) {
                try {
                    Annotation annotation = method.getAnnotation(JsonIgnore.class);

                    if(annotation == null) {
                        stringBuilder.append("\n" + getErrorMessage(method.getName(), apiClass.getName(), implClass.getName()));
                    }
                }
                catch (Exception ex) {
                    stringBuilder.append("\n" + getErrorMessage(method.getName(), apiClass.getName(), implClass.getName()) + "\n");
                }
            }
        }

        if(stringBuilder.length() > 0) {
            fail(stringBuilder.toString());
        }
    }

    private static String getErrorMessage(String methodName, String apiClassName, String implClassName) {
        String message = "Method '" + methodName + "' in class '" + implClassName + "' is not defined in API class/interface " + apiClassName;
        message += ". It should be either adding the method to the API class/interface " + apiClassName + " or annotating the method as '@JsonIgnore' in class " + implClassName;
        return message;
    }

    private static boolean isGetter(Method method){
        if(!method.getName().startsWith("get"))      return false;
        if(method.getParameterTypes().length != 0)   return false;
        if(void.class.equals(method.getReturnType())) return false;
        return true;
    }

    private static List<Method> getGetters(Class cls) {
        List<Method> getters = new ArrayList<>();
        for(Method method : cls.getMethods()) {
            if(isGetter(method))
                getters.add(method);
        }
        return getters;
    }

    private static List<Method> getNonAPIGetters(List<Method> apiGetters, List<Method> implGetters) {
        List<Method> nonAPIGetters = new ArrayList<>();
        for(Method method : implGetters) {
            if(method.getName().equals("getClass"))
                continue;

            boolean definedInInterface = false;
            for (Method apiMethod : apiGetters) {
                if(apiMethod.getName().equals(method.getName())) {
                    definedInInterface = true;
                    break;
                }
            }
            if(!definedInInterface)
                nonAPIGetters.add(method);
        }

        return nonAPIGetters;
    }
}
