package org.femtoframework.util.convert;

import org.femtoframework.implement.ImplementUtil;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

/**
 * Converter Util
 *
 * Java is not a type safe language. But in some use cases, for example, convert properties to java object or reflection in runtime,
 * Using default java code will throw exception when got an unexpected data, in real world, something is better to have some default value instead of breaking the code.
 * DataUtil and .convert.* are based on this idea to treat java like a type safe language.
 *
 * @author fengyun
 * @version 1.00 2009-2-7 19:03:24
 */
public class ConverterUtil
{
    private static ConverterFactory factory = ImplementUtil.getInstance(ConverterFactory.class);

    /**
     * Return DataConverter by type name
     *
     * @param type Type
     * @return
     */
    public static <T> DataConverter<T> getConverter(String type) {
        return factory.getConverter(type);
    }

    /**
     * Return DataConverter by expectedType
     *
     * @param expectedType Expected Type class
     * @return
     */
    public static <T> DataConverter<T> getConverter(Class<T> expectedType) {
        return factory.getConverter(expectedType);
    }


    /**
     * Convert the given object to type
     *
     * @param type Type, @see DataTypes.*
     * @param obj  Object
     */
    public static <T> T convertToType(Object obj, String type)
    {
        return convertToType(obj, type, null);
    }

    /**
     * Convert the given object to type
     *
     * @param type Type, @see DataTypes.*
     * @param obj  Object
     * @param defValue Default value, if it is not able to convert
     */
    public static <T> T convertToType(Object obj, String type, T defValue)
    {
        DataConverter<T> converter = getConverter(type);
        if (converter == null) {
            throw new IllegalStateException("No such converter:" + type);
        }
        return converter.convert(obj, defValue);
    }

    /**
     * Convert the given object to type
     *
     * @param type Type, @see DataTypes.*
     * @param obj  Object
     */
    public static <T> T convertToType(Object obj, Class<T> type)
    {
        return convertToType(obj, type, null);
    }

    /**
     * Convert the given object to type
     *
     * @param type Type, @see DataTypes.*
     * @param obj  Object
     */
    public static <T> T convertToType(Object obj, Class<T> type, T defValue)
    {
        DataConverter<T> converter = getConverter(type);
        if (converter == null) {
            if (type.isAssignableFrom(obj.getClass())) {
                return (T)obj;
            }
            else if (type.isArray()) {
                return (T)getArray(type.getComponentType(), obj, defValue);
            }
            return defValue;
        }
        return converter.convert(obj, defValue);
    }

    private static Object getArray0(Class componentType, Object obj, Object defValue)
    {
        if (obj.getClass().isArray()) {
            int len = Array.getLength(obj);
            Object array = Array.newInstance(componentType, len);
            for (int i = 0; i < len; i++) {
                Object value = convertToType(Array.get(obj, i), componentType);
                if (value != null) {
                    Array.set(array, i, value);
                }
            }
            return array;
        }
        else if (obj instanceof List) {
            List list = (List)obj;
            int len = list.size();
            Object array = Array.newInstance(componentType, len);
            for (int i = 0; i < len; i++) {
                Object value = convertToType(list.get(i), componentType);
                if (value != null) {
                    Array.set(array, i, value);
                }
            }
            return array;
        }
        else {
            Object value = convertToType(obj, componentType);
            if (value == null) {
                if (defValue == null) {
                    return null;
                }
                else if (defValue.getClass().isArray()) {
                    return defValue;
                }
                else {
                    value = defValue;
                }
            }
            Object array = Array.newInstance(componentType, 1);
            Array.set(array, 0, value);
            return array;
        }
    }

    public static Object getArray(Class componentType, Object obj)
    {
        if (obj == null) {
            return null;
        }
        return getArray0(componentType, obj, null);
    }

    public static Object getArray(Class<?> componentType, Object obj, Object defValue)
    {
        if (obj == null) {
            return null;
        }
        return getArray0(componentType, obj, defValue);
    }

    private static Object[] getObjectArray0(Class componentType, Object obj, Object[] defValue)
    {
        if (obj.getClass().isArray()) {
            int len = Array.getLength(obj);
            Object[] array = new Object[len];
            Object value = null;
            for (int i = 0; i < len; i++) {
                value = convertToType(Array.get(obj, i), componentType);
                if (value != null) {
                    array[i] = value;
                }
            }
            return array;
        }
        else if (obj instanceof List) {
            return ((List) obj).toArray();
        }
        else if (obj instanceof Map) {
            return ((Map)obj).entrySet().toArray();
        }
        else {
            Object value = convertToType(obj, componentType);
            if (value == null) {
                return defValue;
            }
            return new Object[]{value};
        }
    }

    public static Object[] getObjects(Class componentType, Object obj)
    {
        if (obj == null) {
            return null;
        }
        return getObjectArray0(componentType, obj, null);
    }

    public static Object[] getObjects(Class componentType, Object obj, Object[] defValue)
    {
        if (obj == null) {
            return defValue;
        }
        return getObjectArray0(componentType, obj, defValue);
    }
}
