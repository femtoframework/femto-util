package org.femtoframework.util.convert;


import org.femtoframework.util.DataTypes;
import org.femtoframework.util.DataUtil;

/**
 * Convert to boolean[]
 *
 * @author fengyun
 * @version 1.00 Mar 25, 2009 6:00:48 PM
 */
public class BooleansConverter extends AbstractConverter<boolean[]>
{
    public BooleansConverter() {
        super(DataTypes.TYPE_BOOLEANS);
    }

    /**
     * Convert the object to expected type，returns <code>default value</code> if is not convertible.
     *
     * @param obj      Object, The object could not be <code>null</code>
     * @param defValue Default Value
     * @return Converted object or default value
     */
    protected boolean[] doConvert(Object obj, boolean[] defValue)
    {
        return DataUtil.getBooleans(obj, defValue);
    }
}