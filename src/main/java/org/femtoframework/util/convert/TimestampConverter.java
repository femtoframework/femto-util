package org.femtoframework.util.convert;

import java.sql.Timestamp;

import org.femtoframework.util.DataTypes;
import org.femtoframework.util.DataUtil;

/**
 * Convert object to Timestamp
 *
 * @author fengyun
 * @version 1.00 Aug 28, 2009 9:23:14 PM
 */
public class TimestampConverter extends AbstractConverter<Timestamp>
{
    public TimestampConverter() {
        super(DataTypes.TYPE_TIMESTAMP);
    }

    /**
     * Convert the object to expected type，returns <code>default value</code> if is not convertible.
     *
     * @param obj      Object, The object could not be <code>null</code>
     * @param defValue Default Value
     * @return Converted object or default value
     */
    @Override
    protected Timestamp doConvert(Object obj, Timestamp defValue) {
        return DataUtil.getTimestamp(obj, defValue);
    }
}