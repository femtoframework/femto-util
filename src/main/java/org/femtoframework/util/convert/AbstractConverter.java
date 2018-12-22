package org.femtoframework.util.convert;

/**
 * Abstract Converter
 *
 * @author fengyun
 * @version 1.00 Aug 28, 2009 9:09:15 PM
 */
public abstract class AbstractConverter<T>
    implements DataConverter<T>
{
    private String type;
    private T defaultValue;

    /**
     * Target type
     *
     * @return Target Type
     */
    public String getType() {
        return type;
    }

    protected T getDefaultValue() {
        return defaultValue;
    }

    protected void setDefaultValue(T value) {
        this.defaultValue = value;
    }

    protected AbstractConverter(String type) {
        this.type = type;
    }

    protected AbstractConverter(String type, T defaultValue) {
        this(type);
        setDefaultValue(defaultValue);
    }

    /**
     * Convert the object to expected type，returns <code>null</code> if is not convertible.
     *
     * @param obj Object
     * @return
     */
    public T convert(Object obj) {
        return convert(obj, defaultValue);
    }

    /**
     * Convert the object to expected type，returns <code>default value</code> if is not convertible.
     *
     * @param obj      Object
     * @param defValue Default Value
     * @return Converted object or default value
     */
    public T convert(Object obj, T defValue)
    {
        if (obj == null) {
            return defValue;
        }
        return doConvert(obj, defValue);
    }

    /**
     * Convert the object to expected type，returns <code>default value</code> if is not convertible.
     *
     * @param obj      Object, The object could not be <code>null</code>
     * @param defValue Default Value
     * @return Converted object or default value
     */
    protected abstract T doConvert(Object obj, T defValue);
}
