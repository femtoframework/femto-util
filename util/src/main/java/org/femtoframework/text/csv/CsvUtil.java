package org.femtoframework.text.csv;

import org.femtoframework.util.DataUtil;
import org.femtoframework.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * CSV工具类
 *
 * @author fengyun
 * @version 1.00 2005-1-28 14:24:54
 */
public class CsvUtil
{

    /**
     * 解析一行成字符串数组
     * 1. 支持特殊符号的替换('\\', '\r', '\n', '\t')
     * 2. 支持分隔符和引号的替换，就是相应的'\'+相应的分隔符，如果是','是分隔符，那么用"\,"来表示
     * 3. 支持自动trim字符分隔符两边的多余空字符(' ' '\t')等
     *
     * @param line 行
     * @return
     */
    public static String[] parseLine(String line)
    {
        return parseLine(line, ',', '\"');
    }

    /**
     * 解析一行成字符串数组
     * 1. 支持特殊符号的替换('\\', '\r', '\n', '\t')
     * 2. 支持分隔符和引号的替换，就是相应的'\'+相应的分隔符，如果是','是分隔符，那么用"\,"来表示
     * 3. 支持自动trim字符分隔符两边的多余空字符(' ' '\t')等
     *
     * @param line 行
     * @param sep  分隔符
     * @return
     */
    public static String[] parseLine(String line, char sep)
    {
        return parseLine(line, sep, '\"');
    }

    public static final int STATUS_CONTENT = 0;
    public static final int STATUS_QUOTE = 1;
    public static final int STATUS_SEP = 2;

    /**
     * 解析一行成字符串数组
     * 1. 支持特殊符号的替换('\\', '\r', '\n', '\t')
     * 2. 支持分隔符和引号的替换，就是相应的'\'+相应的分隔符，如果是','是分隔符，那么用"\,"来表示
     * 3. 支持自动trim字符分隔符两边的多余空字符(' ' '\t')等
     *
     * @param line  行
     * @param sep   分隔符
     * @param quote 引号
     * @return
     */
    public static String[] parseLine(String line, char sep, char quote)
    {
        if (line == null) {
            return null;
        }
        else if (line.length() == 0) {
            return DataUtil.EMPTY_STRING_ARRAY;
        }

        List list = new ArrayList();
        int i = 0;
        int len = line.length();
        int end = len - 1;
        char b1 = 0, b2 = 0;
        int status = STATUS_QUOTE;
        StringBuffer sb = new StringBuffer();
        boolean hasQuoted = false;
        while (i < len) {
            b1 = line.charAt(i);
            switch (status) {
                case STATUS_QUOTE:
                    //忽略空格
                    for (; (b1 == ' ' || b1 == '\t') && i < end;) {
                        b1 = line.charAt(++i);
                    }
                    if (b1 == quote) { //引号，从下一个位置开始
                        i++;
                        hasQuoted = true;
                        status = STATUS_CONTENT;
                        continue; //继续循环
                    }
                    status = STATUS_CONTENT;
                    //根据内容来处理下面的字符
                case STATUS_CONTENT:
                    if (b1 == '\\') { //转意符
                        b2 = line.charAt(i + 1);
                        switch (b2) {
                            case '\\':
                                sb.append('\\');
                                break;
                            case 't':
                                sb.append('\t');
                                break;
                            case 'r':
                                sb.append('\r');
                                break;
                            case 'n':
                                sb.append('\n');
                                break;
                            case 'f':
                                sb.append('\f');
                                break;
                            default:
                                if (b2 == sep || b2 == quote) { //分隔符
                                    sb.append(b2);
                                }
                                else { //其它字符，添加'\\' 和对应的字符
                                    throw new IllegalArgumentException("Invalid '\' at:" + i + " in line:" + line);
                                }
                        }
                        //到后面的两个位置
                        i += 2;
                        continue;
                    }
                    else if (hasQuoted) {
                        if (b1 == quote) { //遇到引号，当前的字符串结束
                            list.add(sb.toString());
                            sb.setLength(0);
                            hasQuoted = false;
                            i++;
                            //期望分隔符
                            status = STATUS_SEP;
                            continue;
                        }
                        else if (b1 == sep) { //引用中的分隔符，允许
                            sb.append(sep);
                            i++;
                            continue;
                        }
                    }
                    if (b1 == sep) { //如果遇到分隔符，当前的字符串结束
                        list.add(sb.toString().trim());
                        sb.setLength(0);
                        hasQuoted = false;
                        i++;
                        //期望分隔符
                        status = STATUS_QUOTE;
                        continue;
                    }
                    sb.append(b1);
                    i++;
                    break;
                case STATUS_SEP:
                    //忽略空格
                    for (; (b1 == ' ' || b1 == '\t') && i < end;) {
                        b1 = line.charAt(++i);
                    }
                    if (b1 == sep) {
                        i++;
                    }
                    status = STATUS_QUOTE;
                    break;
            }
        }

        if (sb.length() > 0) {
            list.add(sb.toString().trim());
        }

        String[] array = new String[list.size()];
        list.toArray(array);
        return array;
    }

    /**
     * 产生一个CSV的行
     *
     * @param strs 字符串数组
     * @return
     */
    public static String toLine(String[] strs)
    {
        return toLine(strs, ',', '\"');
    }

    /**
     * 产生一个CSV的行
     *
     * @param strs 字符串数组
     * @param sep  分隔符
     * @return
     */
    public static String toLine(String[] strs, char sep)
    {
        return toLine(strs, sep, '\"');
    }

    /**
     * 产生一个CSV的行
     *
     * @param strs  字符串数组
     * @param sep   分隔符
     * @param quote
     * @return
     */
    public static String toLine(String[] strs, char sep, char quote)
    {
        if (strs == null) {
            return null;
        }
        else if (strs.length == 0) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        escape(sb, strs[0], sep, quote);
        for (int i = 1, len = strs.length; i < len; i++) {
            sb.append(sep);
            escape(sb, strs[i], sep, quote);
        }
        return sb.toString();
    }

    /**
     * @param word
     * @param sep
     * @param quote
     */
    public static void escape(StringBuffer sb, String word, char sep, final char quote)
    {
        if (StringUtil.isInvalid(word)) {
            return;
        }

        int len = word.length();
        /*
         * Look for any "bad" characters, Escape and
         *  quote the entire string if necessary.
         */
        boolean needQuoting = false;
        for (int i = 0; i < len; i++) {
            char c = word.charAt(i);
            if (c == quote || c == '\\' || c == CsvConstants.CR || c == CsvConstants.LF
                || c == '\t' || c == '\f') {
                // need to escape them and then quote the whole string
                sb.append(quote);
                sb.append(word.substring(0, i));
                for (int j = i; j < len; j++) {
                    char cc = word.charAt(j);
                    switch (cc) {
                        case '\\':
                            sb.append("\\\\");
                            break;
                        case '\t':
                            sb.append("\\t");
                            break;
                        case '\r':
                            sb.append("\\r");
                            break;
                        case '\n':
                            sb.append("\\n");
                            break;
                        case '\f':
                            sb.append("\\f");
                            break;
                        default:
                            if (cc == quote) { //引号
                                sb.append('\\').append(cc);
                            }
                            else { //其它字符，添加'\\' 和对应的字符
                                sb.append(cc);
                            }
                    }
                }
                sb.append(quote);
                return;
            }
            else if (c < 040 || c >= 0177 || sep == c || c == ' ') {
                // These characters cause the string to be quoted
                needQuoting = true;
            }
        }

        if (needQuoting) {
            sb.append(quote).append(word).append(quote);
        }
        else {
            sb.append(word);
        }
    }

}
