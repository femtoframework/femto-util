package org.femtoframework.util.convert;


import org.femtoframework.text.SimpleDate;
import org.femtoframework.util.DataTypes;

/**
 * Convert object to SimpleDate
 *
 * @author fengyun
 * @version 1.00 Aug 28, 2003 9:48:21 PM
 */
public class SimpleDateConverter extends AbstractConverter<SimpleDate>
{
    public SimpleDateConverter() {
        super(DataTypes.TYPE_SIMPLE_DATE);
    }

    /**
     * Convert the object to expected type，returns <code>default value</code> if is not convertible.
     *
     * @param obj      Object, The object could not be <code>null</code>
     * @param defValue Default Value
     * @return Converted object or default value
     */
    @Override
    protected SimpleDate doConvert(Object obj, SimpleDate defValue) {
        if (obj instanceof SimpleDate) {
            return (SimpleDate)obj;
        }
        else if (obj instanceof String) {
            try {
                SimpleDate.parse((String)obj);
            }
            catch (Exception e) {
                return defValue;
            }
        }
        return defValue;
    }
}
