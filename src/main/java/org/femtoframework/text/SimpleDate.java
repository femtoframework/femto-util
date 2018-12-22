package org.femtoframework.text;


import org.femtoframework.util.DataUtil;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Year + Month + Day only
 *
 * @author fengyun
 * @version Dec 25, 2008 5:36:15 PM
 */
public class SimpleDate implements Externalizable, Comparable {
    private int year;
    private int month;
    private int day;

    private transient Date date;
    private transient long time;

    private final static Calendar calendar = Calendar.getInstance();

    public static final int YEAR = Calendar.YEAR;
    public static final int MONTH = Calendar.MONTH;
    public static final int DAY = Calendar.DAY_OF_MONTH;

    public SimpleDate(int year, int month, int day) {
        setYear(year);
        setMonth(month);
        setDay(day);
    }

    public SimpleDate(Calendar calendar) {
        setFields(calendar);
    }

    public SimpleDate() {
        this(new Date());
    }

    public SimpleDate(Date date) {
        synchronized (calendar) {
            calendar.setTime(date);
            setFields(calendar);
        }
    }

    private void setFields(Calendar calendar) {
        setYear(calendar.get(Calendar.YEAR));
        setMonth(calendar.get(Calendar.MONTH) + 1);
        setDay(calendar.get(Calendar.DAY_OF_MONTH));
    }

    public SimpleDate(long time) {
        this(new Date(time));
    }

    public int getYear() {
        return year;
    }

    protected void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    protected void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    protected void setDay(int day) {
        this.day = day;
    }

    /**
     * Add years or months or days
     * Minus means moving back
     *
     * @param years  Years
     * @param months Months
     * @param days   Days
     * @return
     */
    public SimpleDate add(int years, int months, int days) {
        synchronized (calendar) {
            copyTo(calendar);
            calendar.add(YEAR, years);
            calendar.add(MONTH, months);
            calendar.add(DAY, days);
            return new SimpleDate(calendar);
        }
    }

    /**
     * Move specific field type with specific value
     * For example, add 1 day, You can use Calendar.DAYS, 1
     *
     * @param field
     * @param value
     * @return
     */
    public SimpleDate next(int field, int value) {
        return next0(field, value);
    }

    public SimpleDate prev(int field, int value) {
        return next0(field, -value);
    }

    private SimpleDate next0(int field, int value) {
        synchronized (calendar) {
            copyTo(calendar);
            calendar.add(field, value);
            return new SimpleDate(calendar);
        }
    }


    public void copyTo(Calendar calendar) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
    }

    public Calendar toCalendar() {
        Calendar calendar = Calendar.getInstance();
        copyTo(calendar);
        return calendar;
    }

    private transient int hashCode = 0;

    public int hashCode() {
        if (hashCode == 0) {
            hashCode = year + month + day;
        }
        return hashCode;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof SimpleDate) {
            SimpleDate date = (SimpleDate)obj;
            return year == date.year && month == date.month && day == date.day;
        }
        return false;
    }

    public int compareTo(SimpleDate date) {
        if (date == null) {
            return -1;
        }
        if (year > date.year) {
            return 1;
        }
        else if (year < date.year) {
            return -1;
        }

        if (month > date.month) {
            return 1;
        }
        else if (month < date.month) {
            return -1;
        }
        return day - date.day;
    }

    public boolean before(SimpleDate date) {
        return compareTo(date) < 0;
    }

    public boolean after(SimpleDate date) {
        return compareTo(date) > 0;
    }

    public int compareTo(Object obj) {
        if (obj == null) {
            return -1;
        }

        if (obj instanceof SimpleDate) {
            return compareTo((SimpleDate)obj);
        }
        else if (obj instanceof Date) {
            return getDate().compareTo((Date)obj);
        }
        return -1;
    }

    public Date getDate() {
        if (date == null) {
            synchronized (calendar) {
                calendar.set(year, month - 1, day, 0, 0, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                date = calendar.getTime();
            }
        }
        return date;
    }

    public long getTime() {
        if (time == 0) {
            time = getDate().getTime();
            // clear millisecond.
            time = (time / 1000) * 1000;
        }
        return time;
    }

    /**
     * Parsing （yyyy-MM-dd）string to SimpleDate
     *
     * @param str yyyy-MM-dd
     */
    public static SimpleDate parse(String str) {
        int len = str.length();
        if (!(len == 10 || len == 8 || len == 6)) {
            throw new IllegalArgumentException("The date must be like yyyy-MM-dd");
        }

        if (len == 10) {
            return parse1(str, true);
        }
        else if (len == 6) {
            return parse0(str, false);
        }
        else if (str.indexOf('-') > 0) {
            return parse1(str, false);
        }
        else {
            return parse0(str, true);
        }
    }

    private static SimpleDate parse0(String str, boolean longYear) {
        int off = longYear ? 4 : 2;
        String s = str.substring(0, off);
        int year = DataUtil.getInt(s, -1);
        if (year == -1) {
            throw new IllegalArgumentException("Invalid year:" + s);
        }
        year = longYear(year, longYear);

        s = str.substring(off, (off += 2));
        int month = DataUtil.getInt(s, -1);
        if (month == -1) {
            throw new IllegalArgumentException("Invalid month:" + s);
        }
        s = str.substring(off, off + 2);
        int day = DataUtil.getInt(s, -1);
        if (day == -1) {
            throw new IllegalArgumentException("Invalid day:" + s);
        }
        return new SimpleDate(year, month, day);
    }

    private static int longYear(int year, boolean longYear) {
        if (!longYear) {
            if (year <= 30) {
                return 2000 + year;
            }
            else {
                return 1900 + year;
            }
        }
        return year;
    }

    private static SimpleDate parse1(String str, boolean longYear) {
        int off = longYear ? 4 : 2;
        String s = str.substring(0, off++);
        int year = DataUtil.getInt(s, -1);
        if (year == -1) {
            throw new IllegalArgumentException("Invalid year:" + s);
        }
        year = longYear(year, longYear);

        s = str.substring(off, (off += 2));
        int month = DataUtil.getInt(s, -1);
        if (month == -1) {
            throw new IllegalArgumentException("Invalid month:" + s);
        }
        s = str.substring(++off, off + 2);
        int day = DataUtil.getInt(s, -1);
        if (day == -1) {
            throw new IllegalArgumentException("Invalid day:" + s);
        }
        return new SimpleDate(year, month, day);
    }

    /**
     * Parse string to SimpleDate
     *
     * @param str Formatted date
     * @param pattern Given pattern
     */
    public static SimpleDate parse(String str, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return parse(str, dateFormat);
    }

    /**
     * Parse string to SimpleDate
     *
     * @param str Formatted date
     * @param dateFormat Given DateFormat
     */
    public static SimpleDate parse(String str, DateFormat dateFormat) {
        try {
            Date date = dateFormat.parse(str);
            return new SimpleDate(date);
        }
        catch (Exception ex) {
            throw new IllegalArgumentException("Invalid parameters:" + str);
        }
    }

    public String toString(String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return toString(dateFormat);
    }

    public String toString(DateFormat dateFormat) {
        return dateFormat.format(getDate());
    }

    private transient char sep = '-';
    private transient String str;

    public StringBuilder format(StringBuilder sb, char sep) {
        sb.append(year);
        if (sep != 0) {
            sb.append(sep);
        }

        if (month < 10) {
            sb.append('0');
        }

        sb.append(month);
        if (sep != 0) {
            sb.append(sep);
        }

        if (day < 10) {
            sb.append('0');
        }
        sb.append(day);
        return sb;
    }

    public String toString(char sep) {
        if (str == null || this.sep != sep) {
            StringBuilder sb = new StringBuilder(10);
            format(sb, sep);
            this.sep = sep;
            this.str = sb.toString();
        }
        return str;
    }

    public String toString() {
        return toString('-');
    }

    public static void main(String[] args) {
        System.out.println(SimpleDate.parse("2003-06-23"));
        System.out.println(SimpleDate.parse("03-06-23"));
        System.out.println(SimpleDate.parse("20030623"));
        System.out.println(SimpleDate.parse("970623"));
    }

    @Override
    public void writeExternal(ObjectOutput oos) throws IOException {
        oos.writeInt(year);
        oos.writeInt(month);
        oos.writeInt(day);
    }

    @Override
    public void readExternal(ObjectInput ois) throws IOException, ClassNotFoundException {
        year = ois.readInt();
        month = ois.readInt();
        day = ois.readInt();
    }
}
