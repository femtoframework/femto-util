package org.femtoframework.text.size;


import org.femtoframework.text.FloatFormat;
import org.femtoframework.util.DataUtil;
import org.femtoframework.util.MessageProperties;
import org.femtoframework.util.i18n.LocaleUtil;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;

/**
 * 文件大小或者邮件大小的格式化
 *
 * @author fengyun
 * @version 2:18:39 PM
 */

public class SizeFormat
{
    public static final int K = 1024;
    public static final int M = K * K;
    public static final int G = K * M;
    public static final long T = (long)K * G;

    private static final String RESOURCE = "org/femtoframework/text/size/size.properties";

    private static final String[] DEFAULT_SUFFIX = new String[]{
            "Byte", "Bytes", "KB", "MB", "GB", "TB"
    };

    private static HashMap<Locale, String[]> locale2Suffix = new HashMap<>();

    static {
        MessageProperties props = null;
        try {
            props = new MessageProperties(RESOURCE);

            Enumeration en = props.keys();
            while (en.hasMoreElements()) {
                String localeName = (String)en.nextElement();
                String[] suffix = DataUtil.toStrings(props.getProperty(localeName), ',');
                if (suffix != null && suffix.length == 6) {
                    Locale locale = LocaleUtil.getLocale(localeName);
                    locale2Suffix.put(locale, suffix);
                }
            }
        }
        catch (Exception e) {
            //Ignore
        }
    }
    /*
    private static String[] getSuffix(String locale)
    {
        return getSuffix(MessageLocale.getLocaleByName(locale));
    }
    */

    private static String[] getSuffix(Locale locale)
    {
        String[] suffix = (String[])locale2Suffix.get(locale);
        if (suffix == null) {
            suffix = DEFAULT_SUFFIX;
        }
        return suffix;
    }

    public static String format(int size)
    {
        return format(size, Locale.getDefault());
    }

    public static String format(int size, String locale)
    {
        return format(size, LocaleUtil.getLocale(locale));
    }

    public static String format(int size, Locale locale)
    {
        StringBuilder sb = new StringBuilder();
        return format(sb, size, locale).toString();
    }

    public static StringBuilder format(StringBuilder sb, int size)
    {
        return format(sb, size, Locale.getDefault());
    }

    public static StringBuilder format(StringBuilder sb, int size, String locale)
    {
        return format(sb, size, LocaleUtil.getLocale(locale));
    }

    public static StringBuilder format(StringBuilder sb, int size, Locale locale)
    {
        if (size < 0) {
            sb.append('-');
            size = -size;
        }

        String[] suffix = getSuffix(locale);
        if (size < K) {
            sb.append(size).append(' ').append(size > 1 ? suffix[1] : suffix[0]);
        }
        else if (size < M) {
            sb.append(size / K).append(' ').append(suffix[2]);
        }
        else if (size < G) {
            int s = size / M;
            int m = size % M;
            if (m > 100 * K && s < 100) {
                int fraction = s > 10 ? 1 : 2;
                FloatFormat.format(sb, (float)s, fraction);
            }
            else {
                sb.append(s);
            }
            sb.append(' ').append(suffix[3]);
        }
        else {
            int s = size / G;
            int m = size % G;
            if (m > 100 * M && s < 100) {
                int fraction = s > 10 ? 1 : 2;
                FloatFormat.format(sb, (double)s, fraction);
            }
            else {
                sb.append(s);
            }
            sb.append(' ').append(suffix[4]);
        }

        return sb;
    }

    public static String format(long size)
    {
        return format(size, Locale.getDefault());
    }

    public static String format(long size, String locale)
    {
        return format(size, LocaleUtil.getLocale(locale));
    }

    public static String format(long size, Locale locale)
    {
        StringBuilder sb = new StringBuilder();
        return format(sb, size, locale).toString();
    }

    public static StringBuilder format(StringBuilder sb, long size)
    {
        return format(sb, size, Locale.getDefault());
    }

    public static StringBuilder format(StringBuilder sb, long size, String locale)
    {
        return format(sb, size, LocaleUtil.getLocale(locale));
    }

    public static StringBuilder format(StringBuilder sb, long size, Locale locale)
    {
        if (size < 0) {
            sb.append('-');
            size = -size;
        }

        if (size < G) {
            return format(sb, (int)size, locale);
        }
        else {
            String[] suffix = getSuffix(locale);
            if (size < T) {
                long s = size / G;
                long m = size % G;
                if (m > 100 * M && s < 100) {
                    int fraction = s > 10 ? 1 : 2;
                    FloatFormat.format(sb, (double)size / G, fraction);
                }
                else {
                    sb.append(s);
                }
                sb.append(' ').append(suffix[4]);
            }
            else {
                long s = size / T;
                long m = size % T;
                if (m > 100l * G && s < 100) {
                    int fraction = s > 10 ? 1 : 2;
                    FloatFormat.format(sb, (double)size / T, fraction);
                }
                else {
                    sb.append(s);
                }
                sb.append(' ').append(suffix[5]);
            }
            return sb;
        }
    }

    /**
     * 解析大小
     * <p/>
     * 支持格式：<br>
     * 1G 4M 5K 6<br>
     * 1GB 4MB 5KB 6B<br>
     * <p/>
     * 数字结果应该小于2G
     *
     * @return 返回字节数 如果str为<code>null</code> 返回-1 如果str为"" 返回 0 如果解析错误返回-1
     */
    public static long parseToLong(String str)
    {
        if (str == null) {
            return -1;
        }
        else if (str.length() == 0) {
            return 0;
        }


        long size = 0;
        char ch = str.charAt(str.length() - 1);
        if (Character.isDigit(ch)) {
            size = parseLong(str);
        }
        else {
            if (str.endsWith("Byte")) {
                str = str.substring(0, str.length() - 4);
                if (str.length() == 0) {
                    return 1;
                }
                return parseLong(str);
            }
            if (str.endsWith("Bytes")) {
                str = str.substring(0, str.length() - 5);
                if (str.length() == 0) {
                    return 1;
                }
                return parseLong(str);
            }

            if (ch == 'B' || ch == 'b') {
                str = str.substring(0, str.length() - 1);
                if (str.length() == 0) {
                    return 1;
                }
                ch = str.charAt(str.length() - 1);
            }

            if (ch == 'k' || ch == 'K' || ch == 'm' || ch == 'M'
                    || ch == 'g' || ch == 'G' || ch == 't' || ch == 'T') {
                double d = 0.0d;
                if (str.length() == 0) {
                    d = 1;
                }
                else {
                    str = str.substring(0, str.length() - 1);
                    d = parseDouble(str);
//                    text = DataUtil.getInt(str, -1);
                    if (d == -1.0f) {
                        //Parsing error;
                        return -1;
                    }
                }

                switch (ch) {
                    case 'k':
                    case 'K':
                        d *= K;
                        break;
                    case 'm':
                    case 'M':
                        d *= M;
                        break;
                    case 'g':
                    case 'G':
                        d *= G;
                    case 't':
                    case 'T':
                        d *= T;
                }
                size = (long)d;
            }
            else {
                size = parseLong(str);
            }
        }
        return size;
    }

    /**
     * 解析大小
     * <p/>
     * 支持格式：<br>
     * 1G 4M 5K 6<br>
     * 1GB 4MB 5KB 6B<br>
     * <p/>
     * 数字结果应该小于2G
     *
     * @return 返回字节数 如果str为<code>null</code> 返回-1 如果str为"" 返回 0 如果解析错误返回-1
     */
    public static int parse(String str)
    {
        return (int)parseToLong(str);
    }

    private static long parseLong(String str)
    {
        str = str.trim();
        if (str.indexOf('.') >= 0) {
            return (long)DataUtil.getDouble(str, -1.0d);
        }
        else {
            return DataUtil.getLong(str, -1);
        }
    }

    private static double parseDouble(String str)
    {
        str = str.trim();
        return DataUtil.getDouble(str, -1.0d);
    }

    public static void main(String[] args)
    {
        int size = parse(args[0]);
        System.out.println("Size==" + size);
        System.out.println("Format==" + format(size));
    }
}