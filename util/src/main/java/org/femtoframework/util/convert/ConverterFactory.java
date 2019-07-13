package org.femtoframework.util.convert;

import org.femtoframework.annotation.ImplementedBy;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Converter factory
 *
 * @author fengyun
 * @version 1.00 2009-02-7 19:01:11
 */
@ImplementedBy("org.femtoframework.util.convert.SimpleConverterFactory")
public interface ConverterFactory
{
    /**
     * Return DataConverter by type
     *
     * @param type Type
     * @return DataConverter
     */
    default <T> DataConverter<T> getConverter(String type) {
        return getConverter(type, true);
    }

    /**
     * Return DataConverter by type
     *
     * @param type Type
     * @param search try to find new converter?
     * @return DataConverter
     */
    <T> DataConverter<T> getConverter(String type, boolean search);

    /**
     * Return DataConverter by expectedType
     *
     * @param expectedType Expected Type class
     * @return DataConverter
     */
    default <T> DataConverter<T> getConverter(Class<T> expectedType) {
        return getConverter(expectedType.getName());
    }

    /**
     * Return DataConverter by expectedType
     *
     * @param expectedType Expected Type class
     * @return DataConverter
     */
    default <T> DataConverter<T> getConverter(Type expectedType) {
        if (expectedType instanceof Class) {
            return getConverter((Class<T>)expectedType);
        }
        else if (expectedType instanceof ParameterizedType) {
            return getConverter(((ParameterizedType)expectedType).getRawType());
        }
        else {
            return getConverter(expectedType.getTypeName());
        }
    }
}
