package org.femtoframework.util.convert;

import org.femtoframework.util.DataTypes;
import org.femtoframework.util.DataUtil;

/**
 * Convert Object to float[]
 *
 * @author fengyun
 * @version 1.00 Oct 11, 2003 11:50:29 AM
 */
public class FloatsConverter extends AbstractConverter<float[]>
{

    public FloatsConverter() {
        super(DataTypes.TYPE_FLOATS);
    }

    /**
     * Convert the object to expected type，returns <code>default value</code> if is not convertible.
     *
     * @param obj      Object, The object could not be <code>null</code>
     * @param defValue Default Value
     * @return Converted object or default value
     */
    @Override
    protected float[] doConvert(Object obj, float[] defValue) {
        return DataUtil.getFloats(obj, defValue);
    }
}