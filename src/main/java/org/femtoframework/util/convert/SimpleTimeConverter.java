package org.femtoframework.util.convert;

import org.femtoframework.text.SimpleTime;
import org.femtoframework.util.DataTypes;


/**
 * Convert Object to SimpleTime
 *
 * @author fengyun
 * @version 1.00 Aug 28, 2008 9:52:53 PM
 */
public class SimpleTimeConverter extends AbstractConverter<SimpleTime>
{
    public SimpleTimeConverter() {
        super(DataTypes.TYPE_SIMPLE_TIME);
    }

    /**
     * Convert the object to expected type，returns <code>default value</code> if is not convertible.
     *
     * @param obj      Object, The object could not be <code>null</code>
     * @param defValue Default Value
     * @return Converted object or default value
     */
    @Override
    protected SimpleTime doConvert(Object obj, SimpleTime defValue) {
        if (obj instanceof SimpleTime) {
            return (SimpleTime)obj;
        }
        else if (obj instanceof String) {
            try {
                return SimpleTime.parse((String)obj);
            }
            catch (Exception e) {
                return defValue;
            }
        }
        return defValue;
    }
}