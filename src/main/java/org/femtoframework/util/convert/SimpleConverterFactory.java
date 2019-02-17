package org.femtoframework.util.convert;

import java.util.HashMap;
import java.util.Map;

import org.femtoframework.implement.ImplementUtil;
import org.femtoframework.util.convert.time.TemporalAmountConverter;
import org.femtoframework.util.convert.time.TemporalConverter;

import static org.femtoframework.util.DataTypes.*;

/**
 * Simple Converter Factory
 *
 * @author fengyun
 * @version 1.00 2005-2-7 19:02:04
 */
public class SimpleConverterFactory implements ConverterFactory
{
    private Map<String, DataConverter> converters = new HashMap<>(64);

    public SimpleConverterFactory() {
        DataConverter converter = new StringConverter();
        put(TYPE_STRING, converter);
        put("java.lang.String", converter);

        converter = new IntegerConverter();
        put(TYPE_INT, converter);
        put(TYPE_INTEGER, converter);
        put("java.lang.Integer", converter);

        converter = new BooleanConverter();
        put(TYPE_BOOLEAN, converter);
        put("java.lang.Boolean", converter);

        converter = new CharConverter();
        put(TYPE_CHAR, converter);
        put(TYPE_CHARACTER, converter);
        put("java.lang.Character", converter);

        converter = new ByteConverter();
        put(TYPE_BYTE, converter);
        put("java.lang.Byte", converter);

        converter = new ShortConverter();
        put(TYPE_SHORT, converter);
        put("java.lang.Short", converter);

        converter = new LongConverter();
        put(TYPE_LONG, converter);
        put("java.lang.Long", converter);

        converter = new FloatConverter();
        put(TYPE_FLOAT, converter);
        put("java.lang.Float", converter);

        converter = new DoubleConverter();
        put(TYPE_DOUBLE, converter);
        put("java.lang.Double", converter);

        //DATE OR TIME
        converter = new DateConverter();
        put(TYPE_DATE, converter);
        put("java.util.Date", converter);

        converter = new TimeConverter();
        put(TYPE_TIME, converter);
        put("java.sql.Time", converter);

        converter = new TimestampConverter();
        put(TYPE_TIMESTAMP, converter);
        put("java.sql.Timestamp", converter);

        // Arrays
        converter = new BytesConverter();
        put(TYPE_BYTES, converter);
        put(TYPE_BYTE_JAVA_ARRAY, converter);
        put("[B", converter);

        converter = new CharsConverter();
        put(TYPE_CHARS, converter);
        put(TYPE_CHAR_JAVA_ARRAY, converter);
        put("[C", converter);

        converter = new ShortsConverter();
        put(TYPE_SHORTS, converter);
        put(TYPE_SHORT_JAVA_ARRAY, converter);
        put("[S", converter);

        converter = new IntsConverter();
        put(TYPE_INTS, converter);
        put(TYPE_INT_JAVA_ARRAY, converter);
        put("[I", converter);

        converter = new LongsConverter();
        put(TYPE_LONGS, converter);
        put(TYPE_LONG_JAVA_ARRAY, converter);
        put("[J", converter);

        converter = new FloatsConverter();
        put(TYPE_FLOATS, converter);
        put(TYPE_FLOAT_JAVA_ARRAY, converter);
        put("[F", converter);

        converter = new DoublesConverter();
        put(TYPE_DOUBLES, converter);
        put(TYPE_DOUBLE_JAVA_ARRAY, converter);
        put("[D", converter);

        converter = new BooleansConverter();
        put(TYPE_BOOLEANS, converter);
        put(TYPE_BOOLEAN_JAVA_ARRAY, converter);
        put("[Z", converter);

        converter = new StringsConverter();
        put(TYPE_STRINGS, converter);
        put(TYPE_STRING_JAVA_ARRAY, converter);
        put("[Ljava.lang.String;", converter);

        converter = new ObjectConverter();
        put(TYPE_OBJECT, converter);
        put("java.lang.Object", converter);

        converter = new ObjectsConverter();
        put(TYPE_OBJECTS, converter);
        put(TYPE_OBJECT_JAVA_ARRAY, converter);
        put("[Ljava.lang.Object;", converter);

        //inetaddress
        converter = new InetAddressConverter();
        put(TYPE_INET_ADDRESS, converter);
        put("java.net.InetAddress", converter);

        //Collections
        converter = new ListConverter();
        put(TYPE_LIST, converter);
        put("java.net.List", converter);
        put("java.net.ArrayList", converter);

        converter = new MapConverter();
        put(TYPE_MAP, converter);
        put("java.net.Map", converter);
        put("java.net.HashMap", converter);

        converter = new SetConverter();
        put(TYPE_SET, converter);
        put("java.util.Set", converter);
        put("java.util.HashSet", converter);

        converter = new ParametersConverter();
        put(TYPE_PARAMETERS, converter);
        put("org.femtoframework.parameters.Parameters", converter);
        put("org.femtoframework.parameters.ParametersMap", converter);
        
        
        //Time
        converter = new TemporalAmountConverter(TYPE_PERIOD);
        put(TYPE_PERIOD, converter);
        put("java.time.Period", converter);

        converter = new TemporalAmountConverter(TYPE_DURATION);
        put(TYPE_DURATION, converter);
        put("java.time.Duration", converter);

        converter = new TemporalConverter(TYPE_INSTANT);
        put(TYPE_INSTANT, converter);
        put("java.time.Instant", converter);

        converter = new TemporalConverter(TYPE_LOCAL_DATE);
        put(TYPE_LOCAL_DATE, converter);
        put("java.time.LocalDate", converter);

        converter = new TemporalConverter(TYPE_LOCAL_TIME);
        put(TYPE_LOCAL_TIME, converter);
        put("java.time.LocalTime", converter);

        converter = new TemporalConverter(TYPE_LOCAL_DATE_TIME);
        put(TYPE_LOCAL_DATE_TIME, converter);
        put("java.time.LocalDateTime", converter);
    }

    private void put(String name, DataConverter converter) {
        String key = name;
        if (!name.contains(".")) {
            key = name.toLowerCase();
        }
        converters.put(key, converter);
    }

    /**
     * Return DataConverter by type
     *
     * @param type Type
     * @param search try to find new converter?
     * @return
     */
    public <T> DataConverter<T> getConverter(String type, boolean search)
    {
        if (type == null) {
            throw new IllegalArgumentException("Null type");
        }
        String ignoreCaseType = type;
        int len = type.length();
        if (len > 2 && type.indexOf('.') < 0) {
            //基础类型
            ignoreCaseType = type.toLowerCase();
        }
        DataConverter converter = converters.get(ignoreCaseType);
        if (converter == null && search) {
            converter = converters.get(ignoreCaseType);
            if (converter == null) {
                synchronized (converters) {
                    try {
                        converter = ImplementUtil.getInstance(type, DataConverter.class);
                        converters.put(ignoreCaseType, converter);
                    }
                    catch (Exception ex) {
                    }
                }
            }
        }
        return converter;
    }
}
