package org.femtoframework.util.timer;

import org.femtoframework.util.ArrayUtil;
import org.femtoframework.util.DataUtil;
import org.femtoframework.util.StringUtil;

import java.util.Calendar;
import java.util.Date;

/**
 * Cron Entry
 *
 * @author fengyun
 * @version 1.00 May 23, 2003 10:49:44 AM
 */
public class CronEntry
{
    public static final String ALL = "*";
    private static final String DEFAULT_SECOND = "0";

    private int id;

    private boolean runInBusinessDay = false;

    private String second = DEFAULT_SECOND;
    private String hour = ALL;
    private String minute = ALL;
    private String month = ALL;
    private String dayOfWeek = ALL;
    private String dayOfMonth = ALL;
    private String year = ALL;

    private boolean[] bSeconds;
    private boolean[] bMinutes;
    private boolean[] bHours;
    private boolean[] bDaysOfMonth;
    private boolean[] bMonths;
    private boolean[] bDaysOfWeek;

    private long nextRunningTime = -1;

    {
        setSecond(DEFAULT_SECOND);
    }

    /**
     * 将时间重置到原始状态<BR>
     * <code>
     * String second = DEFAULT_SECOND;<BR>
     * String hour = ALL;<BR>
     * String minute = ALL;<BR>
     * String month = ALL;<BR>
     * String dayOfWeek = ALL;<BR>
     * String dayOfMonth = ALL;<BR>
     * String year = ALL;<BR>
     * </code>
     */
    public void reset()
    {
        second = DEFAULT_SECOND;
        hour = ALL;
        minute = ALL;
        month = ALL;
        dayOfWeek = ALL;
        dayOfMonth = ALL;
        year = ALL;
    }

    /**
     * Hours setter
     *
     * @param hour The hours to execute the Class,
     *             the values can take are [ * , 2-4 , 2,3,4,5 , 3/5]
     */
    public void setHour(String hour)
    {
        if (StringUtil.isInvalid(hour)) {
            hour = ALL;
        }
        this.hour = hour;
        this.bHours = new boolean[24];
        CrontabUtil.parseToken(hour, bHours, false);
    }

    /**
     * Minutes setter
     *
     * @param minute The minutes to execute the Class,
     *               the values can take are [ * , 2-4 , 2,3,4,5 , 3/5]
     */
    public void setMinute(String minute)
    {
        if (StringUtil.isInvalid(minute)) {
            minute = ALL;
        }
        this.minute = minute;
        this.bMinutes = new boolean[60];
        CrontabUtil.parseToken(minute, bMinutes, false);
    }

    /**
     * Seconds setter
     *
     * @param second The seconds to execute the Class,
     *               the values can take are [ * , 2-4 , 2,3,4,5 , 3/5]
     */
    public void setSecond(String second)
    {
        if (StringUtil.isInvalid(second)) {
            second = DEFAULT_SECOND;
        }
        this.second = second;
        this.bSeconds = new boolean[60];
        CrontabUtil.parseToken(second, bSeconds, false);
    }

    /**
     * Months setter
     *
     * @param month The Monts to execute the Class,
     *              the values can take are [ * , 2-4 , 2,3,4,5 , 3/5]
     */
    public void setMonth(String month)
    {
        if (StringUtil.isInvalid(month)) {
            month = ALL;
        }
        this.month = month;
        this.bMonths = new boolean[12];
        CrontabUtil.parseToken(month, bMonths, true);
    }

    /**
     * Days of Week
     *
     * @param dayOfWeek The days of the week
     */
    public void setDayOfWeek(String dayOfWeek)
    {
        if (StringUtil.isInvalid(dayOfWeek)) {
            dayOfWeek = ALL;
        }
        this.dayOfWeek = dayOfWeek;
        this.bDaysOfWeek = new boolean[7];
        CrontabUtil.parseToken(dayOfWeek, bDaysOfWeek, false);
    }

    /**
     * Days of Month setter
     *
     * @param dayOfMonth The days of the month
     */
    public void setDayOfMonth(String dayOfMonth)
    {
        if (StringUtil.isInvalid(dayOfMonth)) {
            dayOfMonth = ALL;
        }
        this.dayOfMonth = dayOfMonth;
        this.bDaysOfMonth = new boolean[31];
        CrontabUtil.parseToken(dayOfMonth, bDaysOfMonth, true);
    }

    /**
     * Years Setter
     *
     * @param year to be executed this task
     */
    public void setYear(String year)
    {
        if (StringUtil.isInvalid(year)) {
            year = ALL;
        }
        this.year = year;
    }

    /**
     * runInBusinessDays getter
     * <p/>
     * true if shouldRun only in Business Days false otherwise
     *
     * @param runInBusinessDays
     */
    public void setBusinessDay(boolean runInBusinessDays)
    {
        this.runInBusinessDay = runInBusinessDays;
    }

    /**
     * Hours getter
     *
     * @return the hours of this CrontabBean
     */
    public String getHour()
    {
        return hour;
    }

    /**
     * Minutes getter
     *
     * @return the minutes of this CrontabBean
     */
    public String getMinute()
    {
        return minute;
    }

    /**
     * Minutes getter
     *
     * @return the minutes of this CrontabBean
     */
    public String getSecond()
    {
        return second;
    }

    /**
     * Months getter
     *
     * @return the months of this CrontabBean
     */
    public String getMonth()
    {
        return month;
    }

    /**
     * Days of week getter
     *
     * @return the Days of week of this CrontabBean
     */
    public String getDayOfWeek()
    {
        return dayOfWeek;
    }

    /**
     * Days of Month getter
     *
     * @return the Id of this CrontabBean
     */
    public String getDayOfMonth()
    {
        return dayOfMonth;
    }

    /**
     * Year getter
     *
     * @return the year of this CrontabEntryBean
     */
    public String getYear()
    {
        return year;
    }

    /**
     * runOnlyInBusinessDays getter
     *
     * @return true if shouldRun only in Business Days false otherwise
     */
    public boolean getBusinessDay()
    {
        return runInBusinessDay;
    }

    /**
     * 返回对应的标识
     *
     * @return
     */
    public int getId()
    {
        return id;
    }

    /**
     * 设置标识
     *
     * @param id
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * 返回下一次任务执行的时间
     *
     * @return
     */
    public long getNextRunningTime()
    {
        long now = System.currentTimeMillis();
        return getNextRunningTime(now);
    }

    /**
     * 返回下一次任务执行的时间
     *
     * @return
     */
    public long getNextRunningTime(long now)
    {
        if (nextRunningTime == -1) {
            nextRunningTime = nextRunningTime(now);
        }
        return nextRunningTime;
    }

    /**
     * 返回下一次任务执行的时间
     *
     * @return
     */
    public long nextRunningTime()
    {
        return nextRunningTime(System.currentTimeMillis());
    }

    /**
     * 返回下一次任务执行的时间
     *
     * @return
     */
    public long nextRunningTime(long now)
    {
        Calendar next = next(now);
        if (next != null) {
            return nextRunningTime = next.getTime().getTime();
        }
        return -1;
    }

    /**
     * This method builds a Date from a CrontabEntryBean. launching the same
     * method with now as parameter
     *
     * @return Date
     */
    public Calendar next()
    {
        return next(System.currentTimeMillis());
    }

    /**
     * This method builds a Date from a CrontabEntryBean. launching the same
     * method with now as parameter
     *
     * @return Date
     */
    public Calendar next(long now)
    {
        if (Math.abs(nextRunningTime - now) < 1000) {
            now = Math.max(nextRunningTime, now) + 1000;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(now);
        return buildCalendar(cal);
    }

    /**
     * This method builds a Date from a CrontabEntryBean and from a starting
     * Date
     *
     * @return Date
     */
    public Calendar buildCalendar(Calendar after)
    {
        int second = after.get(Calendar.SECOND);
        second = getNextIndex(bSeconds, second);
        if (second == -1) {
            second = getNextIndex(bSeconds, 0);
            after.add(Calendar.MINUTE, 1);
        }

        int nowMinute = after.get(Calendar.MINUTE);
        int minute = getNextIndex(bMinutes, nowMinute);
        if (minute == -1 || minute > nowMinute) {
            if (minute == -1) {
                after.add(Calendar.HOUR_OF_DAY, 1);
                minute = getNextIndex(bMinutes, 0);
            }
            second = getNextIndex(bSeconds, 0);
// Update by fengyun 这行会导致 "0 60/10 * * *"算出一个过去的时间
//            minute = getNextIndex(bMinutes, 0);
        }

        int nowHour = after.get(Calendar.HOUR_OF_DAY);
        int hour = getNextIndex(bHours, nowHour);
        if (hour == -1 || hour > nowHour) {
            if (hour == -1) {
                after.add(Calendar.DAY_OF_MONTH, 1);
                hour = getNextIndex(bHours, 0);
            }
            second = getNextIndex(bSeconds, 0);
            minute = getNextIndex(bMinutes, 0);
        }

        int today = after.get(Calendar.DAY_OF_MONTH);
        int dayOfMonth = getNextIndex(bDaysOfMonth, today - 1);

        if (dayOfMonth == -1 || dayOfMonth >= today) {
            if (dayOfMonth == -1) {
                after.add(Calendar.MONTH, 1);
                dayOfMonth = getNextIndex(bDaysOfMonth, 0);
            }
            second = getNextIndex(bSeconds, 0);
            minute = getNextIndex(bMinutes, 0);
            hour = getNextIndex(bHours, 0);
        }

        boolean dayMatchRealDate = false;
        while (!dayMatchRealDate) {
            if (checkDayValidInMonth(dayOfMonth + 1, after.get(Calendar.MONTH),
                after.get(Calendar.YEAR))) {
                dayMatchRealDate = true;
            }
            else {
                after.add(Calendar.MONTH, 1);
            }
        }

        int nowMonth = after.get(Calendar.MONTH);
        int month = getNextIndex(bMonths, nowMonth);
        if (month == -1 || month > nowMonth) {
            if (month == -1) {
                after.add(Calendar.YEAR, 1);
                month = getNextIndex(bMonths, 0);
            }
            second = getNextIndex(bSeconds, 0);
            minute = getNextIndex(bMinutes, 0);
            hour = getNextIndex(bHours, 0);
            dayOfMonth = getNextIndex(bDaysOfMonth, 0);
        }

//        Calendar calendar = getTime(second, minute, hour, dayOfMonth + 1,
//            month, after.get(Calendar.YEAR));
        after.set(after.get(Calendar.YEAR), month, dayOfMonth + 1, hour, minute, second);

        if (bDaysOfWeek == null || bDaysOfWeek[after.get(Calendar.DAY_OF_WEEK) - 1]) {
            return after;
        }
        else {
            after.add(Calendar.DAY_OF_YEAR, 1);
            return buildCalendar(after);
        }
    }

    /**This method builds a Date from a CrontabEntryBean and from a starting
     * Date
     * @return Date builded with those parameters
     * @param seconds int the seconds of this time
     * @param minutes int the minutes of this time
     * @param hour int the hour of this time
     * @param dayOfMonth int the dayOfMonth of this time
     * @param month int the month of this time
     * @param year int the year of this time
     */
//    private Calendar getTime(int seconds,
//                             int minutes,
//                             int hour,
//                             int dayOfMonth,
//                             int month,
//                             int year)
//    {
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(year, month, dayOfMonth, hour, minutes, seconds);
//        return calendar;
//    }

    /**
     * This method says wich is next index of this array
     *
     * @param array the list of booleans to check
     * @param start int the id where starts the search
     * @return index int
     */
    private int getNextIndex(boolean[] array, int start)
    {
        if (array == null) {
            return start;
        }

        int len = array.length;
        for (int i = start; i < len; i++) {
            if (array[i]) {
                return i;
            }
        }
        return -1;
    }

    /**
     * This says if this month has this day or not, basically this problem
     * occurrs with 31 days in months with less days.
     *
     * @param day   int the day so see if exists or not
     * @param month int the month to see it has this day or not.
     * @param year  to see if valid ... to work with 366 days years and February
     *              :-)
     */
    private boolean checkDayValidInMonth(int day, int month, int year)
    {
        try {
            Calendar cl = Calendar.getInstance();
            cl.setLenient(false);
            cl.set(Calendar.DAY_OF_MONTH, day);
            cl.set(Calendar.MONTH, month);
            cl.set(Calendar.YEAR, year);
            cl.getTime();
        }
        catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    /**
     * ??????Entry
     *
     * @param crontab
     * @return
     */
    public static CronEntry parse(String crontab)
    {
        String[] entries = DataUtil.toStrings(crontab, ' ');
        if (ArrayUtil.isInvalid(entries)) {
            throw new CronParseException("Invalid crontab entry:" + crontab);
        }
        int len = entries.length;
        CronEntry entry = new CronEntry();
        switch (len) {
            case 2:
                entry.setHour(entries[1]);
            case 1:
                entry.setMinute(entries[0]);
                break;
            case 6:
                entry.setDayOfWeek(entries[5]);
            case 5:
                entry.setMonth(entries[4]);
            case 4:
                entry.setDayOfMonth(entries[3]);
            case 3:
                entry.setHour(entries[2]);
                entry.setMinute(entries[1]);
                entry.setSecond(entries[0]);
                break;
            default:
                throw new CronParseException("Invalid crontab entry:" + crontab);
        }
        return entry;
    }

    public static void main(String[] args)
    {
        CronEntry entry = new CronEntry();
        entry.setSecond("60/5");
        entry.setMinute("*");
//        entry.setHours(args[1]);
//        entry.setDaysOfMonth(args[2]);
//        entry.setMonths(args[3]);
//        entry.setDaysOfWeek(args[4]);
        long now = System.currentTimeMillis();
        while (true) {
            long next = entry.nextRunningTime(now);
            if (next <= now) {
                System.out.println(entry.next().getTime());
                System.out.println("now= " + now);
                System.out.println("next= " + next);

                if (Math.abs(next - now) < 1000) {
                    now = Math.max(next, now) + 1000;
                    System.out.println("Now==" + now);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Date(now));
                    System.out.println("Second=" + cal.get(Calendar.SECOND));
                    continue;
                }
            }
            else {
                try {
                    Thread.sleep(next - now);
                }
                catch(InterruptedException ie) {
                    //
                }
            }
            now = System.currentTimeMillis();
        }
    }


    public String toString()
    {
        final StringBuilder buf = new StringBuilder();
        buf.append("CronEntry");
        buf.append("{");
        buf.append(" ").append(second);
        buf.append(" ").append(minute);
        buf.append(" ").append(hour);
        buf.append(" ").append(dayOfMonth);
        buf.append(" ").append(month);
        buf.append(" ").append(year);
        buf.append('}');
        return buf.toString();
    }
}
