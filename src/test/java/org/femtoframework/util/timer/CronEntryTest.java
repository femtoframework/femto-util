package org.femtoframework.util.timer;

import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertTrue;

/**
 * @author rEneX
 * @version 1.00 2004-5-20 14:28:59
 */
public class CronEntryTest {
    private Calendar cal = Calendar.getInstance();

    @Test
    public void testNextRunningTime0() {
        CronEntry entry = new CronEntry();
        entry.setDayOfMonth(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
        entry.setSecond("0");
        entry.setMinute("*");
        long now = System.currentTimeMillis();
        long next = entry.getNextRunningTime();
        long dif = next - now;
        System.out.print(now);
        System.out.print("|");
        System.out.print(next);
        System.out.print("|");
        System.out.println(dif);
        assertTrue(dif > 0 && dif < 60 * 1000);
    }

    @Test
    public void testNextRunningTime1() {
        CronEntry entry = new CronEntry();
        entry.setDayOfWeek(String.valueOf(cal.get(Calendar.DAY_OF_WEEK) - 1));
        entry.setSecond("0");
        entry.setMinute("*");
        long now = System.currentTimeMillis();
        long next = entry.getNextRunningTime();
        long dif = next - now;
        System.out.print(now);
        System.out.print("|");
        System.out.print(next);
        System.out.print("|");
        System.out.println(dif);
        assertTrue(dif > 0 && dif < 60 * 1000);
    }

    @Test
    public void testNextRunningTime2() throws Exception {
        CronEntry entry = new CronEntry();
        int nextDay = cal.get(Calendar.DAY_OF_MONTH) + 1;
        entry.setDayOfMonth(String.valueOf(nextDay)); //明天开始
        entry.setMinute("50");
        long next = entry.getNextRunningTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(next);
        Assert.assertEquals(calendar.get(Calendar.DAY_OF_MONTH), nextDay);
        Assert.assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 0);
        Assert.assertEquals(calendar.get(Calendar.MINUTE), 50);
    }

    @Test
    public void testNextRunningTime3() throws Exception {
        CronEntry entry = new CronEntry();
        int nextDay = cal.get(Calendar.DAY_OF_MONTH) + 1;
        entry.setDayOfMonth(String.valueOf(nextDay)); //明天开始
        entry.setHour("8");
        entry.setMinute("50");
        long next = entry.getNextRunningTime();
        System.out.println(new Date(next));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(next);
        Assert.assertEquals(calendar.get(Calendar.DAY_OF_MONTH), nextDay);
        Assert.assertEquals(calendar.get(Calendar.MONTH), cal.get(Calendar.MONTH));
        Assert.assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 8);
        Assert.assertEquals(calendar.get(Calendar.MINUTE), 50);
    }

    @Test
    public void testNextRunningTime4() throws Exception {
        CronEntry entry = new CronEntry();
        int nextDay = cal.get(Calendar.DAY_OF_MONTH) + 1;
        entry.setDayOfMonth(String.valueOf(nextDay)); //明天开始
        entry.setMinute("50");
        long next = entry.getNextRunningTime();
        System.out.println(new Date(next));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(next);
        Assert.assertEquals(calendar.get(Calendar.DAY_OF_MONTH), nextDay);
        Assert.assertEquals(calendar.get(Calendar.MONTH), cal.get(Calendar.MONTH));
        Assert.assertEquals(calendar.get(Calendar.MINUTE), 50);
    }

    @Test
    public void testNextRunnintTime5() throws Exception {
        CronEntry entry = new CronEntry();
        entry.setHour("0");
        entry.setMinute("2");
        entry.setSecond("0");
        long next = entry.getNextRunningTime();
        System.out.println(new Date(next));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(next);
        Assert.assertEquals(calendar.get(Calendar.SECOND), 0);
        Assert.assertEquals(calendar.get(Calendar.MINUTE), 2);
        Assert.assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 0);
        Assert.assertEquals(calendar.get(Calendar.DAY_OF_MONTH),
            cal.get(Calendar.DAY_OF_MONTH) + 1);

    }

    @Test
    public void testNextRunningTime6() throws Exception {
        CronEntry entry = CronEntry.parse("0 60/10 * * *");
        long now = System.currentTimeMillis();
        long next = entry.getNextRunningTime(now);
        System.out.println(now + "|" + next + "|" + (next - now));
        assertTrue(next > now);
    }

    @Test
    public void testNextRunningTime7() throws Exception {
        CronEntry entry = CronEntry.parse("0,20,40 * * * *");
        long now = System.currentTimeMillis();
        long next = entry.getNextRunningTime(now);
        System.out.println(new Date(now) + "|" + new Date(next) + "|" + (next - now));
        assertTrue(next > now);
        now = next + 1;
        next = entry.nextRunningTime(now);
        System.out.println(new Date(now) + "|" + new Date(next) + "|" + (next - now));
        assertTrue(next > now);
        assertTrue(next - now >= 19000);
        now = next + 1;
        next = entry.nextRunningTime(now);
        System.out.println(new Date(now) + "|" + new Date(next) + "|" + (next - now));
        assertTrue(next > now);
        assertTrue(next - now >= 19000);
    }

    @Test
    public void test31days() throws Exception {
        CronEntry entry = CronEntry.parse("0 0 0 31");
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        long next = entry.getNextRunningTime(cal.getTimeInMillis());
        System.out.println("next time=" + new Date(next));
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        System.out.println("should be=" + cal.getTime());
        long dif = next - cal.getTimeInMillis();
        assertTrue(dif > 0 && dif < 60 * 1000);
    }

    @Test
    public void test() throws Exception {
        CronEntry entry = new CronEntry();
        entry.setMinute("0,5,10,15,20,25,30,35,40,45,50,55");
        entry.setHour("*");
        entry.setDayOfMonth("21");
        cal.set(Calendar.DAY_OF_MONTH, 1);
        long next = entry.getNextRunningTime(cal.getTimeInMillis());
        System.out.println("next time=" + new Date(next));
    }
}