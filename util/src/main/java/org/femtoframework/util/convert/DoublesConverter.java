package org.femtoframework.util.convert;

import org.femtoframework.util.DataTypes;
import org.femtoframework.util.DataUtil;

/**
 * @author fengyun
 * @version 1.00 Oct 11, 2003 11:50:48 AM
 */
public class DoublesConverter extends AbstractConverter<double[]>
{
    public DoublesConverter() {
        super(DataTypes.TYPE_DOUBLES);
    }

    /**
     * Convert the object to expected type，returns <code>default value</code> if is not convertible.
     *
     * @param obj      Object, The object could not be <code>null</code>
     * @param defValue Default Value
     * @return Converted object or default value
     */
    protected double[] doConvert(Object obj, double[] defValue)
    {
        return DataUtil.getDoubles(obj, defValue);
    }
}
