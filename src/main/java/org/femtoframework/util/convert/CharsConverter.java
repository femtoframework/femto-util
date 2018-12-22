package org.femtoframework.util.convert;

import org.femtoframework.util.DataTypes;
import org.femtoframework.util.DataUtil;

/**
 * Convert object to chars
 *
 * @author fengyun
 * @version 1.00 Aug 28, 2009 10:01:45 PM
 */
public class CharsConverter extends AbstractConverter<char[]>
{
    public CharsConverter() {
        super(DataTypes.TYPE_CHARS);
    }


    /**
     * Convert the object to expected type，returns <code>default value</code> if is not convertible.
     *
     * @param obj      Object, The object could not be <code>null</code>
     * @param defValue Default Value
     * @return Converted object or default value
     */
    protected char[] doConvert(Object obj, char[] defValue)
    {
        return DataUtil.getChars(obj, defValue);
    }
}