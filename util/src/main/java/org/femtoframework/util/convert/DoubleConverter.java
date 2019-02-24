package org.femtoframework.util.convert;

import org.femtoframework.util.DataTypes;
import org.femtoframework.util.DataUtil;

/**
 * Convert object to double
 *
 * @author fengyun
 * @version 1.00 Aug 28, 2009 9:20:37 PM
 */
public class DoubleConverter extends AbstractConverter<Double>
{

    public DoubleConverter() {
        super(DataTypes.TYPE_DOUBLE, 0.0d);
    }

    /**
     * Convert the object to expected type，returns <code>default value</code> if is not convertible.
     *
     * @param obj      Object, The object could not be <code>null</code>
     * @param defValue Default Value
     * @return Converted object or default value
     */
    protected Double doConvert(Object obj, Double defValue)
    {
        return DataUtil.getDouble(obj, defValue != null ? defValue : getDefaultValue());
    }
}