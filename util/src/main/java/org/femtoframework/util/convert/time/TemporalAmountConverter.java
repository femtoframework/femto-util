package org.femtoframework.util.convert.time;

import org.femtoframework.util.DataType;
import org.femtoframework.util.convert.AbstractConverter;

import java.time.Duration;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;

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
 * String          |    Duration#parse
 * int(s),long(ms) |    Duration
 * Duration        |    Long,int
 * Duration        |    String
 * -----------------------------------
 * String          |    Period#parse
 * int,long(days)  |    Period
 * Period          |    Long
 * Period          |    String
 * -----------------------------------
 */
public class TemporalAmountConverter<TA extends TemporalAmount> extends AbstractConverter<TA> {

    private DataType dataType;

    public TemporalAmountConverter(DataType type) {
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
    protected TA doConvert(Object obj, TA defValue) {
        if (obj instanceof CharSequence) {
            try {
                switch (dataType) {
                    case PERIOD:
                        return (TA) Period.parse((CharSequence) obj);
                    case DURATION:
                        return (TA) Duration.parse((CharSequence) obj);
                }
            }
            catch (DateTimeParseException dtpe) {
                return defValue;
            }
        }
        else if (obj instanceof Long) {
            switch (dataType) {
                case PERIOD: //Days
                    return (TA) Period.ofDays(((Long)obj).intValue());
                case DURATION://Milliseconds
                    return (TA) Duration.ofMillis((Long)obj);
            }
        }
        else if (obj instanceof Integer) {
            switch (dataType) {
                case PERIOD: //Days
                    return (TA) Period.ofDays((Integer)obj);
                case DURATION: //Seconds
                    return (TA) Duration.of((Integer)obj, ChronoUnit.SECONDS);
            }
        }
        return defValue;
    }
}
