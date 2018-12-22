package org.femtoframework.util.convert;

import org.femtoframework.util.DataTypes;
import org.femtoframework.util.DataUtil;

/**
 * Convert object to short[]
 *
 * @author fengyun
 * @version 1.00 Oct 11, 2003 11:49:51 AM
 */
public class ShortsConverter extends AbstractConverter<short[]>
{
    public ShortsConverter() {
        super(DataTypes.TYPE_SHORTS);
    }

    /**
     * Convert the object to expected type，returns <code>default value</code> if is not convertible.
     *
     * @param obj      Object, The object could not be <code>null</code>
     * @param defValue Default Value
     * @return Converted object or default value
     */
    @Override
    protected short[] doConvert(Object obj, short[] defValue) {
        return DataUtil.getShorts(obj, defValue);
    }
}