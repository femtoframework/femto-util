package org.femtoframework.util.convert;

import org.femtoframework.util.DataTypes;
import org.femtoframework.util.DataUtil;

/**
 * Convert object to integer
 *
 * @author fengyun
 * @version 1.00 Aug 28, 2008 9:13:47 PM
 */
public class IntegerConverter extends AbstractConverter<Integer>
{
    public IntegerConverter() {
        super(DataTypes.TYPE_INT, 0);
    }

    /**
     * Convert the object to expected type，returns <code>default value</code> if is not convertible.
     *
     * @param obj      Object, The object could not be <code>null</code>
     * @param defValue Default Value
     * @return Converted object or default value
     */
    @Override
    protected Integer doConvert(Object obj, Integer defValue) {
        return DataUtil.getInt(obj, defValue);
    }
}
