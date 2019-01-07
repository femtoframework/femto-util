package org.femtoframework.util.convert;

import java.util.HashMap;
import java.util.Map;

import org.femtoframework.implement.ImplementUtil;
import org.femtoframework.util.DataUtil;

/**
 * Simple Converter Factory
 *
 * @author fengyun
 * @version 1.00 2005-2-7 19:02:04
 */
public enum SimpleConverterFactory
    implements ConverterFactory
{
    INSTANCE;

    private Map<String, DataConverter> converters = new HashMap<>(64);

    private SimpleConverterFactory() {
        DataConverter converter = new StringConverter();
        put(DataUtil.TYPE_STRING, converter);
        put("java.lang.String", converter);

        converter = new IntegerConverter();
        put(DataUtil.TYPE_INT, converter);
        put(DataUtil.TYPE_INTEGER, converter);
        put("java.lang.Integer", converter);

        converter = new BooleanConverter();
        put(DataUtil.TYPE_BOOLEAN, converter);
        put("java.lang.Boolean", converter);

        converter = new CharConverter();
        put(DataUtil.TYPE_CHAR, converter);
        put(DataUtil.TYPE_CHARACTER, converter);
        put("java.lang.Character", converter);

        converter = new ByteConverter();
        put(DataUtil.TYPE_BYTE, converter);
        put("java.lang.Byte", converter);

        converter = new ShortConverter();
        put(DataUtil.TYPE_SHORT, converter);
        put("java.lang.Short", converter);

        converter = new LongConverter();
        put(DataUtil.TYPE_LONG, converter);
        put("java.lang.Long", converter);

        converter = new FloatConverter();
        put(DataUtil.TYPE_FLOAT, converter);
        put("java.lang.Float", converter);

        converter = new DoubleConverter();
        put(DataUtil.TYPE_DOUBLE, converter);
        put("java.lang.Double", converter);

        //DATE OR TIME
        converter = new DateConverter();
        put(DataUtil.TYPE_DATE, converter);
        put("java.util.Date", converter);

        converter = new TimeConverter();
        put(DataUtil.TYPE_TIME, converter);
        put("java.sql.Time", converter);

        converter = new TimestampConverter();
        put(DataUtil.TYPE_TIMESTAMP, converter);
        put("java.sql.Timestamp", converter);

        converter = new SimpleDateConverter();
        put(DataUtil.TYPE_SIMPLE_DATE, converter);
        put("org.bolango.text.SimpleDate", converter);

        converter = new SimpleTimeConverter();
        put(DataUtil.TYPE_SIMPLE_TIME, converter);
        put("org.bolango.text.SimpleTime", converter);

        // Arrays
        converter = new BytesConverter();
        put(DataUtil.TYPE_BYTES, converter);
        put(DataUtil.TYPE_BYTE_JAVA_ARRAY, converter);
        put("[B", converter);

        converter = new CharsConverter();
        put(DataUtil.TYPE_CHARS, converter);
        put(DataUtil.TYPE_CHAR_JAVA_ARRAY, converter);
        put("[C", converter);

        converter = new ShortsConverter();
        put(DataUtil.TYPE_SHORTS, converter);
        put(DataUtil.TYPE_SHORT_JAVA_ARRAY, converter);
        put("[S", converter);

        converter = new IntsConverter();
        put(DataUtil.TYPE_INTS, converter);
        put(DataUtil.TYPE_INT_JAVA_ARRAY, converter);
        put("[I", converter);

        converter = new LongsConverter();
        put(DataUtil.TYPE_LONGS, converter);
        put(DataUtil.TYPE_LONG_JAVA_ARRAY, converter);
        put("[J", converter);

        converter = new FloatsConverter();
        put(DataUtil.TYPE_FLOATS, converter);
        put(DataUtil.TYPE_FLOAT_JAVA_ARRAY, converter);
        put("[F", converter);

        converter = new DoublesConverter();
        put(DataUtil.TYPE_DOUBLES, converter);
        put(DataUtil.TYPE_DOUBLE_JAVA_ARRAY, converter);
        put("[D", converter);

        converter = new BooleansConverter();
        put(DataUtil.TYPE_BOOLEANS, converter);
        put(DataUtil.TYPE_BOOLEAN_JAVA_ARRAY, converter);
        put("[Z", converter);

        converter = new StringsConverter();
        put(DataUtil.TYPE_STRINGS, converter);
        put(DataUtil.TYPE_STRING_JAVA_ARRAY, converter);
        put("[Ljava.lang.String;", converter);

        converter = new ObjectConverter();
        put(DataUtil.TYPE_OBJECT, converter);
        put("java.lang.Object", converter);

        converter = new ObjectsConverter();
        put(DataUtil.TYPE_OBJECTS, converter);
        put(DataUtil.TYPE_OBJECT_JAVA_ARRAY, converter);
        put("[Ljava.lang.Object;", converter);

        //inetaddress
        converter = new InetAddressConverter();
        put(DataUtil.TYPE_INET_ADDRESS, converter);
        put("java.net.InetAddress", converter);

        //Collections
        converter = new ListConverter();
        put(DataUtil.TYPE_LIST, converter);
        put("java.net.List", converter);
        put("java.net.ArrayList", converter);

        converter = new MapConverter();
        put(DataUtil.TYPE_MAP, converter);
        put("java.net.Map", converter);
        put("java.net.HashMap", converter);

        converter = new SetConverter();
        put(DataUtil.TYPE_SET, converter);
        put("java.util.Set", converter);
        put("java.util.HashSet", converter);

        converter = new ParametersConverter();
        put(DataUtil.TYPE_PARAMETERS, converter);
        put("org.femtoframework.parameters.Parameters", converter);
        put("org.femtoframework.parameters.ParametersMap", converter);

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
