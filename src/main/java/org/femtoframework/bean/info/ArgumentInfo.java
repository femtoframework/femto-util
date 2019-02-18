package org.femtoframework.bean.info;

/**
 * Argument Info
 *
 * It reuses the existing Annotation @java.inject.Named
 */
public interface ArgumentInfo extends FeatureInfo {

    /**
     * Type of this argument
     *
     * @return type of this argument
     */
    String getType();

    /**
     * Convert given value to expectedType
     *
     * @param value original value
     * @param <V> Expected Type
     * @return Converted value
     */
    <V> V toValue(Object value);
}
