package org.femtoframework.util.convert;

import org.femtoframework.util.DataTypes;
import org.femtoframework.util.DataUtil;

import java.util.*;

/**
 * Convert object to map
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class MapConverter extends AbstractConverter<Map> {

    public MapConverter() {
        super(DataTypes.TYPE_MAP);
    }

    /**
     * Convert the object to expected type，returns <code>default value</code> if is not convertible.
     *
     * @param obj      Object, The object could not be <code>null</code>
     * @param defValue Default Value
     * @return Converted object or default value
     */
    @Override
    protected Map doConvert(Object obj, Map defValue) {
        return DataUtil.getMap(obj, defValue);
    }
}
