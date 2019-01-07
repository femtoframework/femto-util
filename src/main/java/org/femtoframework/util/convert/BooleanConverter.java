package org.femtoframework.util.convert;

import org.femtoframework.util.DataTypes;
import org.femtoframework.util.DataUtil;

/**
 * Convert object to boolean
 *
 * @author fengyun
 * @version 1.00 Aug 28, 2009 9:22:00 PM
 */
public class BooleanConverter extends AbstractConverter<Boolean> {

    public BooleanConverter() {
        super(DataTypes.TYPE_BOOLEAN, Boolean.FALSE);
    }

    /**
     * Convert the object to expected type，returns <code>default value</code> if is not convertible.
     *
     * @param obj      Object, The object could not be <code>null</code>
     * @param defValue Default Value
     * @return Converted object or default value
     */
    protected Boolean doConvert(Object obj, Boolean defValue) {
        return DataUtil.getBoolean(obj, defValue != null ? defValue : getDefaultValue());
    }
}