package org.femtoframework.util.convert;

/**
 * Convert object between different types
 *
 *
 * @author fengyun
 * @version 1.00 Aug 28, 2009 9:08:28 PM
 */
public interface DataConverter<T>
{
    /**
     * Target type
     *
     * @return Target Type
     */
    String getType();

    /**
     * Returns whether the object is convertible by this converter
     *
     * @param obj The object to convert
     * @return Convertible or not
     */
    default boolean isConvertible(Object obj) {
        Object o = convert(obj, null);
        return o != null;
    }

    /**
     * Convert the object to expected type，returns <code>null</code> if is not convertible.
     *
     * @param obj Object
     * @return
     */
    default T convert(Object obj) {
        return convert(obj, null);
    }

    /**
     * Convert the object to expected type，returns <code>default value</code> if is not convertible.
     *
     * @param obj      Object
     * @param defValue Default Value
     * @return Converted object or default value
     */
    T convert(Object obj, T defValue);
}
