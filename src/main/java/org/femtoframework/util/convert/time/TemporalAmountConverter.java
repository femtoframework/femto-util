package org.femtoframework.util.convert.time;

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

    public TemporalAmountConverter(String type) {
        super(type);
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
                switch (type) {
                    case "period":
                        return (TA) Period.parse((CharSequence) obj);
                    case "duration":
                        return (TA) Duration.parse((CharSequence) obj);
                }
            }
            catch (DateTimeParseException dtpe) {
                return defValue;
            }
        }
        else if (obj instanceof Long) {
            switch (type) {
                case "period": //Days
                    return (TA) Period.ofDays(((Long)obj).intValue());
                case "duration"://Milliseconds
                    return (TA) Duration.ofMillis((Long)obj);
            }
        }
        else if (obj instanceof Integer) {
            switch (type) {
                case "period": //Days
                    return (TA) Period.ofDays((Integer)obj);
                case "duration": //Seconds
                    return (TA) Duration.of((Integer)obj, ChronoUnit.SECONDS);
            }
        }
        return defValue;
    }
}
