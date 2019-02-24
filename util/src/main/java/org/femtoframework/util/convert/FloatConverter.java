package org.femtoframework.util.convert;

import org.femtoframework.util.DataTypes;
import org.femtoframework.util.DataUtil;

/**
 * Convert object to Float
 *
 * @author fengyun
 * @version 1.00 Aug 28, 2008 9:19:59 PM
 */
public class FloatConverter extends AbstractConverter<Float>
{
    public FloatConverter() {
        super(DataTypes.TYPE_FLOAT, 0.0f);
    }

    /**
     * Convert the object to expected type，returns <code>default value</code> if is not convertible.
     *
     * @param obj      Object, The object could not be <code>null</code>
     * @param defValue Default Value
     * @return Converted object or default value
     */
    protected Float doConvert(Object obj, Float defValue)
    {
        return DataUtil.getFloat(obj, defValue != null ? defValue : getDefaultValue());
    }
}