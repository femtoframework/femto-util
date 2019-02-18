package org.femtoframework.util;

import org.femtoframework.bean.NamedBean;

public enum DataType implements NamedBean {

    UNKNOWN("unknown"),

    INT("int"),

    BYTE("byte"),

    CHAR("char"),

    STRING("string"),

    INTEGER("integer"),

    BOOLEAN("boolean"),

    CHARACTER("character"),

    LONG("long"),

    FLOAT("float"),

    DOUBLE("double"),

    SHORT("short"),

    DATE("date"),

    TIME("time"),

    TIMESTAMP("timestamp"),

    INTS("ints"),

    BYTES("bytes"),

    CHARS("chars"),

    SHORTS("shorts"),

    LONGS("longs"),

    DOUBLES("doubles"),

    STRINGS("strings"),

    BOOLEANS("booleans"),

    OBJECT("object"),

    OBJECTS("objects"),

    LIST("list"),

    MAP("map"),

    SET("set"),

    PROPERTIES("properties"),

    PARAMETERS("parameters"),

    INET_ADDRESS("inet_address"),

    DURATION("duration"),

    INSTANT("instant"),

    LOCAL_DATE("local_date"),

    LOCAL_TIME("local_time"),

    LOCAL_DATE_TIME("local_date_time"),

    MONTH_DAY("month_day"),

    OFFSET_DATE_TIME("offset_date_time"),

    OFFSET_TIME("offset_time"),

    PERIOD("period"),

    YEAR("year"),

    YEAR_MONTH("year_month"),

    ZONED_DATE_TIME("zoned_date_time"),

    ZONE_OFFSET("zone_offset");

    private String name;

    DataType(String name) {
        this.name = name;
    }

    /**
     * Name of the object
     *
     * @return Name of the object
     */
    @Override
    public String getName() {
        return name;
    }
}
