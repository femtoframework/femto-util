package org.femtoframework.util.convert;

import java.util.Date;

import org.femtoframework.util.DataTypes;
import org.femtoframework.util.DataUtil;

/**
 * Convert object to date
 *
 * @author fengyun
 * @version 1.00 Aug 28, 2009 9:22:59 PM
 */
public class DateConverter extends AbstractConverter<Date>
{

    public DateConverter() {
        super(DataTypes.TYPE_DATE);
    }

    /**
     * Convert the object to expected type，returns <code>default value</code> if is not convertible.
     *
     * @param obj      Object, The object could not be <code>null</code>
     * @param defValue Default Value
     * @return Converted object or default value
     */
    protected Date doConvert(Object obj, Date defValue)
    {
        return DataUtil.getDate(obj, defValue);
    }
}