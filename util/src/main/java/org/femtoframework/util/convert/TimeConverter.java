package org.femtoframework.util.convert;

import java.sql.Time;

import org.femtoframework.util.DataTypes;
import org.femtoframework.util.DataUtil;

/**
 * Convert object to time
 *
 * @author fengyun
 * @version 1.00 Aug 28, 2003 9:23:06 PM
 */
public class TimeConverter extends AbstractConverter<java.sql.Time>
{

    public TimeConverter() {
        super(DataTypes.TYPE_TIME);
    }

    /**
     * Convert the object to expected type，returns <code>default value</code> if is not convertible.
     *
     * @param obj      Object, The object could not be <code>null</code>
     * @param defValue Default Value
     * @return Converted object or default value
     */
    @Override
    protected Time doConvert(Object obj, Time defValue) {
        return DataUtil.getTime(obj, defValue);
    }
}