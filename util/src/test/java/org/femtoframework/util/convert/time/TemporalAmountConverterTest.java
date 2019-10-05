package org.femtoframework.util.convert.time;

import org.femtoframework.util.DataType;
import org.junit.Test;

import java.time.Duration;
import java.time.temporal.TemporalUnit;

import static java.time.temporal.ChronoUnit.MILLIS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.Assert.*;

public class TemporalAmountConverterTest {

    @Test
    public void doConvert() {

        TemporalAmountConverter<Duration> converter = new TemporalAmountConverter<>(DataType.DURATION);

        Duration duration = converter.convert("100ns");
        assertEquals(duration.getNano(), 100l);

        duration = converter.convert("100ms");
        assertEquals(duration.getNano(), 100000000l);
    }
}