package org.femtoframework.util.convert.time;

import org.femtoframework.util.DataType;
import org.femtoframework.util.convert.AbstractConverter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.Temporal;

/**
 * Since Java8
 *
 * Duration converter
 *
 * Supports conversion
 *
 * -----------------------------------
 * From            |       To
 * -----------------------------------
 * String          |    Temporal#parse
 * Temporal        |    Long,int
 * Temporal        |    String
 * -----------------------------------
 */
public class TemporalConverter <T extends Temporal> extends AbstractConverter<T> {

    private DataType dataType;

    public TemporalConverter(DataType type) {
        super(type);
        this.dataType = type;
    }

    /**
     * Convert the object to expected type，returns <code>default value</code> if is not convertible.
     *
     * @param obj      Object, The object could not be <code>null</code>
     * @param defValue Default Value
     * @return Converted object or default value
     */
    @Override
    protected T doConvert(Object obj, T defValue) {
        if (obj instanceof CharSequence) {
            CharSequence seq = (CharSequence)obj;
            try {
                switch (dataType) {
                    case INSTANT:
                        return (T) Instant.parse(seq);
                    case LOCAL_DATE:
                        return (T) LocalDate.parse(seq);
                    case LOCAL_TIME:
                        return (T) LocalTime.parse(seq);
                    case LOCAL_DATE_TIME:
                        return (T) LocalDateTime.parse(seq);
                }
            }
            catch(DateTimeParseException dtpe) {
                return defValue;
            }
        }
        return defValue;
    }
}
