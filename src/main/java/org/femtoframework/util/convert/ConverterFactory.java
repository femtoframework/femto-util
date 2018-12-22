package org.femtoframework.util.convert;

import org.femtoframework.annotation.ImplementedBy;

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
     * @return
     */
    <T> DataConverter<T> getConverter(String type);

    /**
     * Return DataConverter by expectedType
     *
     * @param expectedType Expected Type class
     * @return
     */
    default <T> DataConverter<T> getConverter(Class<T> expectedType) {
        return getConverter(expectedType.getName());
    }
}
