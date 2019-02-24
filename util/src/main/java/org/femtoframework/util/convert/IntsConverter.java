package org.femtoframework.util.convert;

import org.femtoframework.util.DataTypes;
import org.femtoframework.util.DataUtil;

/**
 * Convert object to int[]
 *
 * @author fengyun
 * @version 1.00 Aug 28, 2003 10:06:07 PM
 */
public class IntsConverter extends AbstractConverter<int[]>
{

    public IntsConverter() {
        super(DataTypes.TYPE_INTS);
    }

    /**
     * Convert the object to expected type，returns <code>default value</code> if is not convertible.
     *
     * @param obj      Object, The object could not be <code>null</code>
     * @param defValue Default Value
     * @return Converted object or default value
     */
    @Override
    protected int[] doConvert(Object obj, int[] defValue) {
        return DataUtil.getInts(obj, defValue);
    }
}
