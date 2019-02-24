package org.femtoframework.util.convert;

import org.femtoframework.util.DataTypes;
import org.femtoframework.util.DataUtil;

/**
 * Convert object to bytes
 *
 * @author fengyun
 * @version 1.00 Aug 28, 2009 10:00:52 PM
 */
public class BytesConverter extends AbstractConverter<byte[]>
{

    public BytesConverter() {
        super(DataTypes.TYPE_BYTES);
    }

    /**
     * Convert the object to expected type，returns <code>default value</code> if is not convertible.
     *
     * @param obj      Object, The object could not be <code>null</code>
     * @param defValue Default Value
     * @return Converted object or default value
     */
    protected byte[] doConvert(Object obj, byte[] defValue)
    {
        return DataUtil.getBytes(obj, defValue);
    }
}