package org.femtoframework.util.convert;

import org.femtoframework.util.DataTypes;
import org.femtoframework.util.DataUtil;

/**
 * Convert object to byte
 *
 * @author fengyun
 * @version 1.00 Aug 28, 2009 9:18:22 PM
 */
public class ByteConverter extends AbstractConverter<Byte>
{
    public ByteConverter() {
        super(DataTypes.TYPE_BYTE, (byte)0);
    }

    /**
     * Convert the object to expected type，returns <code>default value</code> if is not convertible.
     *
     * @param obj      Object, The object could not be <code>null</code>
     * @param defValue Default Value
     * @return Converted object or default value
     */
    protected Byte doConvert(Object obj, Byte defValue)
    {
        return DataUtil.getByte(obj, defValue);
    }
}
