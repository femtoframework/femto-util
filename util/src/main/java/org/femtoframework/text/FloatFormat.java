package org.femtoframework.text;

/**
 * Format float as String in simple way
 */
public interface FloatFormat {

    /**
     * 将浮点数格式化成"XXXX.XXX"的形式
     *
     * @param value    浮点数
     * @param fraction 小数点保留的位数
     */
    static String format(double value, int fraction)
    {
        return format(value, fraction, false);
    }

    /**
     * 将浮点数格式化成"XXXX.XXX"的形式
     *
     * @param value    浮点数
     * @param fraction 小数点保留的位数
     * @param round    是否需要四舍五入
     */
    static String format(double value, int fraction, boolean round)
    {
        StringBuilder sb = new StringBuilder();
        return format(sb, value, fraction, round).toString();
    }

    /**
     * 格式化长整数
     *
     * @param value 长整数
     */
    static String format(long value, String pattern)
    {
        StringBuilder sb = new StringBuilder(65);
        format(sb, value, 65);
        boolean neg = value < 0;
        int left = pattern.length() - sb.length();
        if (left > 0) {
            if (neg) {
                sb.insert(1, pattern.substring(0, left));
            }
            else {
                sb.insert(0, pattern.substring(0, left));
            }
        }
        return sb.toString();
    }

    /**
     * 将格式化好的整数追加到<code>StringBuffer</code>上去<br>
     * 并给定长整数最长的位数，这样有效的节省了开辟的<code>char[]</code>的空间<br>
     *
     * @param sb     StringBuffer
     * @param i      长整数
     * @param length 最大位数
     */
    static StringBuilder format(StringBuilder sb, long i, int length)
    {
        boolean negative = (i < 0);
        char[] buf = new char[negative ? length + 1 : length];
        int charPos = length;

        if (negative) {
            i = -i;
        }

        do {
            int digit = (int)(i % 100);
            buf[--charPos] = RADIX_TEN_UNITS[digit];
            buf[--charPos] = RADIX_TEN_TENTHS[digit];
            i = i / 100;
        }
        while (i != 0);

        if (buf[charPos] == '0') {
            charPos++;
        }
        if (negative) {
            buf[--charPos] = '-';
        }
        sb.append(buf, charPos, length - charPos);
        return sb;
    }

    /**
     * 格式化整数
     *
     * @param value 整数
     */
    static String format(int value, String pattern)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(value);
        int left = pattern.length() - sb.length();
        if (left > 0) {
            if (value < 0) {
                sb.insert(1, pattern.substring(0, left));
            }
            else {
                sb.insert(0, pattern.substring(0, left));
            }
        }
        return sb.toString();
    }

    /**
     * 将浮点数格式化成"XXXX.XXX"的形式<br>
     * 并追加到<code>StringBuffer</code>上去<br>
     *
     * @param sb       StringBuffer
     * @param i        浮点数
     * @param fraction 小数点保留的位数
     */
    static StringBuilder format(StringBuilder sb,
                                       double i,
                                       int fraction)
    {
        return format(sb, i, fraction, false);
    }

    /**
     * 将浮点数格式化成"XXXX.XXX"的形式<br>
     * 并追加到<code>StringBuffer</code>上去<br>
     *
     * @param sb       StringBuffer
     * @param i        浮点数
     * @param fraction 小数点保留的位数
     * @param round    是否需要四舍五入
     */
    static StringBuilder format(StringBuilder sb,
                                       double i,
                                       int fraction,
                                       boolean round)
    {
        if (i < 0) {
            sb.append('-');
            i = -i;
        }

        //todo
        if (round) {
        }

        i += Math.pow(0.1d, fraction + 2);

        long l = (long)i;

        format(sb, l, 64);
        if (fraction == 0) {
            return sb;
        }

        double d = i - l;
        return format0(sb, d, fraction);
    }

    /**
     * 将浮点数格式化成"XXXX.XXX"的形式<br>
     * 并追加到<code>StringBuffer</code>上去<br>
     * 具体实现<br>
     *
     * @param sb       StringBuffer
     * @param i        浮点数
     * @param fraction 小数点保留的位数
     */
    static StringBuilder format0(StringBuilder sb,
                                         double i,
                                         int fraction)
    {
        char[] buf = new char[fraction + 2];
        buf[0] = '.';

        int charPos = 1;
        int digit;
        do {
            i = i * 100;
            digit = (int)i;
            buf[charPos++] = RADIX_TEN_TENTHS[digit];
            buf[charPos++] = RADIX_TEN_UNITS[digit];
            i = i - digit;
        }
        while (charPos <= fraction);

        sb.append(buf, 0, fraction + 1);
        return sb;
    }

    /**
     * 数据格式化的辅助数组
     */
    char[] RADIX_TEN_TENTHS = {
            '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
            '1', '1', '1', '1', '1', '1', '1', '1', '1', '1',
            '2', '2', '2', '2', '2', '2', '2', '2', '2', '2',
            '3', '3', '3', '3', '3', '3', '3', '3', '3', '3',
            '4', '4', '4', '4', '4', '4', '4', '4', '4', '4',
            '5', '5', '5', '5', '5', '5', '5', '5', '5', '5',
            '6', '6', '6', '6', '6', '6', '6', '6', '6', '6',
            '7', '7', '7', '7', '7', '7', '7', '7', '7', '7',
            '8', '8', '8', '8', '8', '8', '8', '8', '8', '8',
            '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'
    };
    /**
     * 数据格式化的辅助数组
     */
    char[] RADIX_TEN_UNITS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };

    static void main(String[] args)
    {
        System.out.println(format(4.115f, 2));
    }
}
