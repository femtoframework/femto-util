package org.femtoframework.util.convert;


import org.femtoframework.util.DataTypes;

/**
 * Do we need this?
 *
 * @author fengyun
 * @version 1.00 Aug 28, 2008 9:09:15 PM
 */
public class ObjectConverter extends AbstractConverter<Object>
{
    public ObjectConverter() {
        super(DataTypes.TYPE_OBJECT);
    }

    /**
     * Convert the object to expected type，returns <code>default value</code> if is not convertible.
     *
     * @param obj      Object, The object could not be <code>null</code>
     * @param defValue Default Value
     * @return Converted object or default value
     */
    @Override
    protected Object doConvert(Object obj, Object defValue) {
        return obj == null ? defValue : obj;
    }
}
