package org.femtoframework.parameters;


import java.util.*;

import org.femtoframework.util.DataUtil;
import org.femtoframework.util.convert.ConverterUtil;

/**
 * Parameters to simplify the data accessing
 *
 * The original published version is https://github.com/eBay/Winder/blob/master/winder-core/src/main/java/org/ebayopensource/common/util/Parameters.java
 *
 *
 * Modifid by Sheldon Shao(xshao) on 12/08/2018
 * Created by xshao on 9/16/16.
 */
public interface Parameters<V> extends Map<String, V> {

    String DEFAULT_STRING = null;
    int DEFAULT_INT = 0;
    boolean DEFAULT_BOOLEAN = false;
    long DEFAULT_LONG = 0L;
    double DEFAULT_DOUBLE = 0.0d;

    String[] DEFAULT_STRING_ARRAY = DataUtil.EMPTY_STRING_ARRAY;

    int[] DEFAULT_INT_ARRAY = DataUtil.EMPTY_INT_ARRAY;

    default String getString(String key) {
        return getString(key, DEFAULT_STRING);
    }

    default String getString(String key, String defValue) {
        return DataUtil.getString(get(key), defValue);
    }

    default boolean getBoolean(String key) {
        return getBoolean(key, DEFAULT_BOOLEAN);
    }

    default boolean getBoolean(String key, boolean defValue) {
        return DataUtil.getBoolean(key, defValue);
    }

    default int getInt(String key) {
        return getInt(key, DEFAULT_INT);
    }

    default int getInt(String key, int defValue) {
        return DataUtil.getInt(get(key), defValue);
    }

    default double getDouble(String key) {
        return getDouble(key, DEFAULT_DOUBLE);
    }

    default double getDouble(String key, double defValue) {
        return DataUtil.getDouble(get(key), defValue);
    }

    default long getLong(String key) {
        return getLong(key, DEFAULT_LONG);
    }

    default long getLong(String key, long defValue) {
        return DataUtil.getLong(get(key), defValue);
    }

    default V get(String key, V defValue) {
        V value = get(key);
        return value == null ? defValue : value;
    }

    default int[] getInts(String key) {
        return getInts(key, DEFAULT_INT_ARRAY);
    }

    default int[] getInts(String key, int[] defValue) {
        return DataUtil.getInts(get(key), defValue);
    }

    default Object[] getObjects(String key) {
        return getObjects(key, null);
    }

    default Object[] getObjects(String key, Object[] defValue) {
        Object obj = get(key);
        return ConverterUtil.getObjects(Object.class, obj, defValue);
    }

    default String[] getStrings(String key) {
        return getStrings(key, DEFAULT_STRING_ARRAY);
    }

    default String[] getStrings(String key, String[] defValue) {
        return DataUtil.getStrings(get(key), defValue);
    }

    /**
     * Return value as Parameters
     *
     * @param key Key
     * @return value as Parameters, if the value is map, the map will wrapped as a Parameters
     */
    default Parameters<V> getParameters(String key) {
        Object value = get(key);
        if (value instanceof Parameters) {
            return (Parameters)value;
        }
        else if (value instanceof Map) {
            return new ParametersMap<>((Map)value);
        }
        return null;
    }

    /**
     * Parsing the String value to List&lt;String&gt;.
     * The string will be split by ",".
     *
     * @param key Key
     * @return A list type of value
     */
    default List<String> getStringList(String key) {
        Object value = get(key);
        return DataUtil.getStringList(value);
    }

    /**
     * Convert value as List as possible
     *
     * @param key Key
     * @return A list type of values
     */
    default <T> List<T> getList(String key) {
        return getList(key, null);
    }

    /**
     * Convert value as List as possible
     *
     * @param key Key
     * @return A list type of values
     */
    default <T> List<T> getList(String key, List defValue) {
        Object obj = get(key);
        return DataUtil.getList(obj, defValue);
    }


    /**
     * Convert value as Set as possible
     *
     * @param key Key
     * @return A set of values
     */
    default <T> Set<T> getSet(String key) {
        return getSet(key, Collections.emptySet());
    }

    /**
     * Convert value as Set as possible
     *
     * @param key Key
     * @return A set of values
     */
    default <T> Set<T> getSet(String key, Set<T> defValue) {
        Object obj = get(key);
        return DataUtil.getSet(obj, defValue);
    }

    /**
     * Convert parameter as Date
     *
     * @param key Key
     * @return convert long or Date as Date
     */
    default Date getDate(String key) {
        return getDate(key, null);
    }


    /**
     * Convert parameter as Date
     *
     * @param key Key
     * @return convert long or Date as Date
     */
    default Date getDate(String key, Date defaultValue) {
        Object obj = get(key);
        return DataUtil.getDate(obj, defaultValue);
    }

    /**
     * Convert string or int as Enum
     *
     * @param key Key
     * @return Convert string or int as Enum
     */
    default <T extends Enum> T getEnum(Class<T> clazz, String key) {
        return getEnum(clazz, key, null);
    }

    /**
     * Convert string or int as Enum
     *
     * @param key Key
     * @return Convert string or int as Enum
     */
    default <T extends Enum> T getEnum(Class<T> clazz, String key, T defaultValue) {
        Object obj = get(key);
        return DataUtil.getEnum(clazz, obj, defaultValue);
    }

    /**
     * To Map
     *
     * @return Convert to Map
     */
    default Map<String, V> toMap() {
        return this;
    }

    /**
     * Static method to convert Map to Parameters
     *
     * @param value Map
     * @param <V> Expected Value type
     * @return Parameters
     */
    static <V> Parameters<V> toParameters(Map value) {
        if (value instanceof Parameters) {
            return (Parameters)value;
        }
        else {
            return new ParametersMap<>(value);
        }
    }
}
