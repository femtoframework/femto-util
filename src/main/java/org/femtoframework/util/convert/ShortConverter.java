package org.femtoframework.util.convert;

import org.femtoframework.util.DataTypes;
import org.femtoframework.util.DataUtil;

/**
 * Convert object to short
 *
 * @author fengyun
 * @version 1.00 Aug 28, 2003 9:19:22 PM
 */
public class ShortConverter extends AbstractConverter<Short>
{
    public ShortConverter() {
        super(DataTypes.TYPE_SHORT, (short)0);
    }

    /**
     * Convert the object to expected type，returns <code>default value</code> if is not convertible.
     *
     * @param obj      Object, The object could not be <code>null</code>
     * @param defValue Default Value
     * @return Converted object or default value
     */
    @Override
    protected Short doConvert(Object obj, Short defValue) {
        return DataUtil.getShort(obj, defValue);
    }
}