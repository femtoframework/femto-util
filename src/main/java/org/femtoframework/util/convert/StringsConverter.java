package org.femtoframework.util.convert;

import org.femtoframework.util.DataTypes;
import org.femtoframework.util.DataUtil;

/**
 * Convert object to String[]
 *
 * @author fengyun
 * @version 1.00 Aug 28, 2003 10:02:56 PM
 */
public class StringsConverter extends AbstractConverter<String[]>
{
    public StringsConverter() {
        super(DataTypes.TYPE_STRINGS);
    }

    /**
     * Convert the object to expected type，returns <code>default value</code> if is not convertible.
     *
     * @param obj      Object, The object could not be <code>null</code>
     * @param defValue Default Value
     * @return Converted object or default value
     */
    @Override
    protected String[] doConvert(Object obj, String[] defValue) {
        return DataUtil.getStrings(obj, defValue);
    }
}