package org.femtoframework.util.convert;

import org.femtoframework.util.DataUtil;

import static org.femtoframework.util.DataTypes.TYPE_STRING;

/**
 * Convert object to string
 *
 * @author fengyun
 * @version 1.00 Aug 28, 2003 9:09:47 PM
 */
public class StringConverter extends AbstractConverter<String>
{
    public StringConverter() {
        super(TYPE_STRING);
    }

    /**
     * Convert the object to expected type，returns <code>default value</code> if is not convertible.
     *
     * @param obj      Object, The object could not be <code>null</code>
     * @param defValue Default Value
     * @return Converted object or default value
     */
    @Override
    protected String doConvert(Object obj, String defValue) {
        return DataUtil.getString(obj, defValue);
    }
}
