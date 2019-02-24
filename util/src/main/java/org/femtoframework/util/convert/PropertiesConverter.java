package org.femtoframework.util.convert;

import org.femtoframework.parameters.Parameters;
import org.femtoframework.util.DataType;
import java.util.*;

public class PropertiesConverter extends AbstractConverter<Properties> {

    public PropertiesConverter() {
        super(DataType.PROPERTIES);
    }

    /**
     * Convert the object to expected type，returns <code>default value</code> if is not convertible.
     *
     * @param obj      Object, The object could not be <code>null</code>
     * @param defValue Default Value
     * @return Converted object or default value
     */
    @Override
    protected Properties doConvert(Object obj, Properties defValue) {
        if (obj instanceof Properties) {
            return (Properties)obj;
        }
        else if (obj instanceof Parameters) {
            Properties properties = new Properties();
            Parameters<Object> parameters = (Parameters<Object>)obj;
            for(String key: parameters.keySet()) {
                String value = parameters.getString(key);
                if (value != null) {
                    properties.put(key, value);
                }
            }
            return properties;
        }
        else if (obj instanceof Map) {
            Properties properties = new Properties();
            Map parameters = (Map)obj;
            for(Object key: parameters.keySet()) {
                Object value = parameters.get(key);
                if (value != null) {
                    properties.put(key, value);
                }
            }
            return properties;
        }
        return defValue;
    }
}
