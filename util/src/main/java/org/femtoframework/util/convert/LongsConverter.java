package org.femtoframework.util.convert;

import org.femtoframework.util.DataTypes;
import org.femtoframework.util.DataUtil;

/**
 * Convert object to longs
 *
 * @author fengyun
 * @version 1.00 Aug 28, 2003 10:07:40 PM
 */
public class LongsConverter extends AbstractConverter<long[]>
{

    public LongsConverter() {
        super(DataTypes.TYPE_LONGS);
    }

    /**
     * Convert the object to expected type，returns <code>default value</code> if is not convertible.
     *
     * @param obj      Object, The object could not be <code>null</code>
     * @param defValue Default Value
     * @return Converted object or default value
     */
    @Override
    protected long[] doConvert(Object obj, long[] defValue) {
        return DataUtil.getLongs(obj, defValue);
    }
}