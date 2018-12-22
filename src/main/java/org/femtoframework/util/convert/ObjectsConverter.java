package org.femtoframework.util.convert;

import org.femtoframework.util.DataTypes;

/**
 * Convert object to Object[]
 *
 * @author fengyun
 * @version 1.00 Aug 28, 2003 9:09:15 PM
 */
public class ObjectsConverter extends AbstractConverter<Object[]>
{
    public ObjectsConverter() {
        super(DataTypes.TYPE_OBJECTS);
    }

    /**
     * Convert the object to expected type，returns <code>default value</code> if is not convertible.
     *
     * @param obj      Object, The object could not be <code>null</code>
     * @param defValue Default Value
     * @return Converted object or default value
     */
    @Override
    protected Object[] doConvert(Object obj, Object[] defValue) {
        return ConverterUtil.getObjects(Object.class, obj, defValue);
    }
}
