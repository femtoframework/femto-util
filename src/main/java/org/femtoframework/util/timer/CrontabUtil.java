package org.femtoframework.util.timer;


import org.femtoframework.util.DataUtil;
import org.femtoframework.util.StringUtil;

/**
 * Crontab Util
 *
 * @author fengyun
 * @version 1.00 Jun 9, 2003 2:44:56 PM
 */
public class CrontabUtil
{
    public static final String ALL = CronEntry.ALL;

    /**
     * Parses a token and fills the array of booleans that represents this
     * CrontabEntryBean
     *
     * @param token      String to parser usually smth like [ * , 2-3 , 2,3,4 ,4/5 ]
     * @param bool       this array is the most efficient way to compare entries
     * @param beginInOne says if the array begins in 0 or in 1
     */
    public static void parseToken(String token, boolean[] bool,
                                  boolean beginInOne)
    {
        if (StringUtil.isInvalid(token)) {
            return;
        }

        // This line initializes all the array of booleans instead of doing so
        // in the CrontabEntryBean Constructor.
        // for (int i = 0; i < bool.length ; i++) bool[i]=false;
        if (ALL.equals(token)) {
            for (int i = 0; i < bool.length; i++) {
                bool[i] = true;
            }
            return;
        }

        int index = token.indexOf(',');
        if (index > 0) {
            String[] tokens = DataUtil.toStrings(token, ',');
            for (String token1 : tokens) {
                parseToken(token1, bool, beginInOne);
            }
            return;
        }

        index = token.indexOf('/');
        if (index >= 0) {
            String range = token.substring(0, index);
            int connect = range.indexOf('-');
            int begin = 0;
            if (connect > 0) {
                begin = DataUtil.getInt(range.substring(0, connect), 0);
            }
            int end = DataUtil.getInt(range.substring(connect + 1), bool.length);
            if (end > bool.length) {
                end = bool.length;
            }
            if (beginInOne) {
                begin--;
                end--;
            }
            String str = token.substring(index + 1);
            int each = DataUtil.getInt(str, -1);
            if (each == -1) {
                throw new CronParseException("Invalid each:" + str);
            }
            int p = begin;
            while (true) {
                bool[p] = true;
                p = p + each;
                if (begin <= end && p >= end) {
                    break;
                }
                if (p >= bool.length) {
                    p = p - bool.length;
                }
                if (p < begin && p > end) {
                    break;
                }
            }
            return;
        }

        index = token.indexOf('-');
        if (index > 0) {
            String str = token.substring(0, index);
            int start = DataUtil.getInt(str, -1);
            if (start == -1) {
                throw new CronParseException("Invalid start:" + str);
            }
            str = token.substring(index + 1);
            int end = DataUtil.getInt(str, -1);
            if (end == -1) {
                throw new CronParseException("Invalid end:" + str);
            }

            if (beginInOne) {
                start--;
                end--;
            }
            for (int i = start; i <= end; i++) {
                bool[i] = true;
            }
            return;
        }

        int iValue = DataUtil.getInt(token, -1);
        if (iValue == -1) {
            throw new CronParseException("Invalid value:" + token);
        }
        if (beginInOne) {
            iValue--;
        }
        if (iValue < 0 || iValue >= bool.length) {
            throw new CronParseException("Invalid value:" + token);
        }
        bool[iValue] = true;
    }

    /**
     * 判断指定的值是不是忙足条件
     *
     * @param token [ * , 2-4 , 2,3,4,5 , 3/5]
     * @param value 对应的数字
     * @return
     */
    public static boolean isMatch(String token, int value)
    {
        if (ALL.equals(token)) {
            return true;
        }

        int index = token.indexOf(',');
        if (index > 0) {
            String[] tokens = DataUtil.toStrings(token, ',');
            for (String token1 : tokens) {
                if (isMatch(token1, value)) {
                    return true;
                }
            }
            return false;
        }

        index = token.indexOf('-');
        if (index > 0) {
            String str = token.substring(0, index);
            int start = DataUtil.getInt(str, -1);
            if (start == -1) {
                throw new CronParseException("Invalid start:" + str);
            }
            str = token.substring(index + 1);
            int end = DataUtil.getInt(str, -1);
            if (end == -1) {
                throw new CronParseException("Invalid end:" + str);
            }

            return value >= start && value <= end;
        }

        index = token.indexOf('/');
        if (index > 0) {
            String str = token.substring(index + 1);
            int each = DataUtil.getInt(str, -1);
            if (each == -1) {
                throw new CronParseException("Invalid each:" + str);
            }

            return value % each == 0;
        }
        else {
            int iValue = DataUtil.getInt(token, -1);
            if (iValue == -1) {
                throw new CronParseException("Invalid value:" + token);
            }
            return value == iValue;
        }
    }


}
