package org.femtoframework.util.convert;

import org.femtoframework.util.DataTypes;
import org.femtoframework.util.DataUtil;

/**
 * Convert object to char
 *
 * @author fengyun
 * @version 1.00 Aug 28, 2009 9:16:46 PM
 * @version 1.00 Aug 28, 2009 9:16:46 PM
 */
public class CharConverter extends AbstractConverter<Character>
{

    public CharConverter() {
        super(DataTypes.TYPE_CHAR);
    }

    /**
     * Convert the object to expected type，returns <code>default value</code> if is not convertible.
     *
     * @param obj      Object, The object could not be <code>null</code>
     * @param defValue Default Value
     * @return Converted object or default value
     */
    protected Character doConvert(Object obj, Character defValue)
    {
        return DataUtil.getChar(obj, defValue != null ? defValue : getDefaultValue());
    }
}
