package org.femtoframework.util.selector;

import org.femtoframework.util.DataUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Ant style selector
 *
 * @author fengyun
 * @version 1.00 May 9, 2003 3:17:19 PM
 */
public interface SelectorUtil
{
    char DEFAULT_SEPARATOR = '/';

    /**
     * Tests whether or not a given path matches the start of a given
     * pattern up to the first "**".
     * <p/>
     * This is not a general purpose test and should only be used if you
     * can live with false positives. For example, <code>pattern=**\a</code>
     * and <code>str=b</code> will yield <code>true</code>.
     *
     * @param pattern The pattern to match against. Must not be
     *                <code>null</code>.
     * @param str     The path to match, as a String. Must not be
     *                <code>null</code>.
     * @return whether or not a given path matches the start of a given
     *         pattern up to the first "**".
     */
    static boolean matchPatternStart(String pattern, String str)
    {
        return matchPatternStart(pattern, str, true);
    }

    /**
     * Tests whether or not a given path matches the start of a given
     * pattern up to the first "**".
     * <p/>
     * This is not a general purpose test and should only be used if you
     * can live with false positives. For example, <code>pattern=**\a</code>
     * and <code>str=b</code> will yield <code>true</code>.
     *
     * @param pattern         The pattern to match against. Must not be
     *                        <code>null</code>.
     * @param str             The path to match, as a String. Must not be
     *                        <code>null</code>.
     * @param isCaseSensitive Whether or not matching should be performed
     *                        case sensitively.
     * @return whether or not a given path matches the start of a given
     *         pattern up to the first "**".
     */
    static boolean matchPatternStart(String pattern,
                                            String str,
                                            boolean isCaseSensitive)
    {
        return matchPatternStart(pattern, str, isCaseSensitive, DEFAULT_SEPARATOR);
    }

    /**
     * Tests whether or not a given path matches the start of a given
     * pattern up to the first "**".
     * <p/>
     * This is not a general purpose test and should only be used if you
     * can live with false positives. For example, <code>pattern=**\a</code>
     * and <code>str=b</code> will yield <code>true</code>.
     *
     * @param pattern         The pattern to match against. Must not be
     *                        <code>null</code>.
     * @param str             The path to match, as a String. Must not be
     *                        <code>null</code>.
     * @param isCaseSensitive Whether or not matching should be performed
     *                        case sensitively.
     * @return whether or not a given path matches the start of a given
     *         pattern up to the first "**".
     */
    static boolean matchPatternStart(String pattern,
                                            String str,
                                            boolean isCaseSensitive,
                                            char sep)
    {
        // When str starts with a File.separator, pattern has to start with a
        // File.separator.
        // When pattern starts with a File.separator, str has to start with a
        // File.separator.
        if ((str != null && !str.isEmpty() && str.charAt(0) == sep)
                != (pattern != null && !pattern.isEmpty() && pattern.charAt(0) == sep)) {
            return false;
        }

        String[] patDirs = tokenizePath(pattern, sep);
        String[] strDirs = tokenizePath(str, sep);

        int patIdxStart = 0;
        int patIdxEnd = patDirs.length - 1;
        int strIdxStart = 0;
        int strIdxEnd = strDirs.length - 1;

        // up to first '**'
        while (patIdxStart <= patIdxEnd && strIdxStart <= strIdxEnd) {
            String patDir = patDirs[patIdxStart];
            if (patDir.equals("**")) {
                break;
            }
            if (!match(patDir, strDirs[strIdxStart],
                isCaseSensitive)) {
                return false;
            }
            patIdxStart++;
            strIdxStart++;
        }

        return (strIdxStart > strIdxEnd) || (patIdxStart <= patIdxEnd);
    }

    /**
     * Tests whether or not a given path matches a given pattern.
     *
     * @param pattern The pattern to match against. Must not be
     *                <code>null</code>.
     * @param str     The path to match, as a String. Must not be
     *                <code>null</code>.
     * @return <code>true</code> if the pattern matches against the string,
     *         or <code>false</code> otherwise.
     */
    static boolean matchPath(String[] pattern, String str)
    {
        return matchPath(pattern, str, true);
    }

    /**
     * Tests whether or not a given path matches a given pattern.
     *
     * @param pattern The pattern to match against. Must not be
     *                <code>null</code>.
     * @param strDirs The path to match, as a String. Must not be
     *                <code>null</code>.
     * @return <code>true</code> if the pattern matches against the string,
     *         or <code>false</code> otherwise.
     */
    static boolean matchPath(String[] pattern, String[] strDirs)
    {
        return matchPath(pattern, strDirs, true);
    }

    /**
     * Tests whether or not a given path matches a given pattern.
     *
     * @param patDirs         The pattern to match against. Must not be
     *                        <code>null</code>.
     * @param str             The path to match, as a String. Must not be
     *                        <code>null</code>.
     * @param isCaseSensitive Whether or not matching should be performed
     *                        case sensitively.
     * @return <code>true</code> if the pattern matches against the string,
     *         or <code>false</code> otherwise.
     */
    static boolean matchPath(String[] patDirs,
                                    String str,
                                    boolean isCaseSensitive)
    {
        return matchPath(patDirs, str, isCaseSensitive, '/');
    }

    /**
     * Tests whether or not a given path matches a given pattern.
     *
     * @param patDirs         The pattern to match against. Must not be
     *                        <code>null</code>.
     * @param str             The path to match, as a String. Must not be
     *                        <code>null</code>.
     * @param isCaseSensitive Whether or not matching should be performed
     *                        case sensitively.
     * @return <code>true</code> if the pattern matches against the string,
     *         or <code>false</code> otherwise.
     */
    static boolean matchPath(String[] patDirs,
                                    String str,
                                    boolean isCaseSensitive,
                                    char sep)
    {
        String[] strDirs = tokenizePath(str, sep);
        return matchPath(patDirs, strDirs, isCaseSensitive);
    }

    /**
     * Tests whether or not a given path matches a given pattern.
     *
     * @param patDirs         The pattern to match against. Must not be
     *                        <code>null</code>.
     * @param strDirs         The path to match, as a String. Must not be
     *                        <code>null</code>.
     * @param isCaseSensitive Whether or not matching should be performed
     *                        case sensitively.
     * @return <code>true</code> if the pattern matches against the string,
     *         or <code>false</code> otherwise.
     */
    static boolean matchPath(String[] patDirs,
                                    String[] strDirs,
                                    boolean isCaseSensitive)
    {
        int patIdxStart = 0;
        int patIdxEnd = patDirs.length - 1;
        int strIdxStart = 0;
        int strIdxEnd = strDirs.length - 1;

        // up to first '**'
        while (patIdxStart <= patIdxEnd && strIdxStart <= strIdxEnd) {
            String patDir = patDirs[patIdxStart];
            if (patDir.equals("**")) {
                break;
            }
            if (!match(patDir, strDirs[strIdxStart], isCaseSensitive)) {
                return false;
            }
            patIdxStart++;
            strIdxStart++;
        }
        if (strIdxStart > strIdxEnd) {
            // String is exhausted
            for (int i = patIdxStart; i <= patIdxEnd; i++) {
                if (!patDirs[i].equals("**")) {
                    return false;
                }
            }
            return true;
        }
        else {
            if (patIdxStart > patIdxEnd) {
                // String not exhausted, but pattern is. Failure.
                return false;
            }
        }

        // up to last '**'
        while (patIdxStart <= patIdxEnd && strIdxStart <= strIdxEnd) {
            String patDir = patDirs[patIdxEnd];
            if (patDir.equals("**")) {
                break;
            }
            if (!match(patDir, strDirs[strIdxEnd], isCaseSensitive)) {
                return false;
            }
            patIdxEnd--;
            strIdxEnd--;
        }
        if (strIdxStart > strIdxEnd) {
            // String is exhausted
            for (int i = patIdxStart; i <= patIdxEnd; i++) {
                if (!patDirs[i].equals("**")) {
                    return false;
                }
            }
            return true;
        }

        while (patIdxStart != patIdxEnd && strIdxStart <= strIdxEnd) {
            int patIdxTmp = -1;
            for (int i = patIdxStart + 1; i <= patIdxEnd; i++) {
                if (patDirs[i].equals("**")) {
                    patIdxTmp = i;
                    break;
                }
            }
            if (patIdxTmp == patIdxStart + 1) {
                // '**/**' situation, so skip one
                patIdxStart++;
                continue;
            }
            // Find the pattern between padIdxStart & padIdxTmp in str between
            // strIdxStart & strIdxEnd
            int patLength = (patIdxTmp - patIdxStart - 1);
            int strLength = (strIdxEnd - strIdxStart + 1);
            int foundIdx = -1;
            strLoop:
            for (int i = 0; i <= strLength - patLength; i++) {
                for (int j = 0; j < patLength; j++) {
                    String subPat = patDirs[patIdxStart + j + 1];
                    String subStr = strDirs[strIdxStart + i + j];
                    if (!match(subPat, subStr, isCaseSensitive)) {
                        continue strLoop;
                    }
                }

                foundIdx = strIdxStart + i;
                break;
            }

            if (foundIdx == -1) {
                return false;
            }

            patIdxStart = patIdxTmp;
            strIdxStart = foundIdx + patLength;
        }

        for (int i = patIdxStart; i <= patIdxEnd; i++) {
            if (!patDirs[i].equals("**")) {
                return false;
            }
        }

        return true;
    }

    /**
     * Tests whether or not a given path matches a given pattern.
     *
     * @param pattern The pattern to match against. Must not be
     *                <code>null</code>.
     * @param str     The path to match, as a String. Must not be
     *                <code>null</code>.
     * @return <code>true</code> if the pattern matches against the string,
     *         or <code>false</code> otherwise.
     */
    static boolean matchPath(String pattern, String str)
    {
        return matchPath(pattern, str, true);
    }

    /**
     * Tests whether or not a given path matches a given pattern.
     *
     * @param pattern         The pattern to match against. Must not be
     *                        <code>null</code>.
     * @param str             The path to match, as a String. Must not be
     *                        <code>null</code>.
     * @param isCaseSensitive Whether or not matching should be performed
     *                        case sensitively.
     * @return <code>true</code> if the pattern matches against the string,
     *         or <code>false</code> otherwise.
     */
    static boolean matchPath(String pattern, String str,
                                    boolean isCaseSensitive)
    {
        return matchPath(pattern, str, isCaseSensitive, DEFAULT_SEPARATOR);
    }

    /**
     * Tests whether or not a given path matches a given pattern.
     *
     * @param pattern         The pattern to match against. Must not be
     *                        <code>null</code>.
     * @param str             The path to match, as a String. Must not be
     *                        <code>null</code>.
     * @param isCaseSensitive Whether or not matching should be performed
     *                        case sensitively.
     * @return <code>true</code> if the pattern matches against the string,
     *         or <code>false</code> otherwise.
     */
    static boolean matchPath(String pattern, String str,
                                    boolean isCaseSensitive,
                                    char sep)
    {
        // When str starts with a File.separator, pattern has to start with a
        // File.separator.
        // When pattern starts with a File.separator, str has to start with a
        // File.separator.
        if ((str != null && !str.isEmpty() && str.charAt(0) == sep)
                != (pattern != null && !pattern.isEmpty() && pattern.charAt(0) == sep)) {
            return false;
        }

        String[] patDirs = tokenizePath(pattern, sep);
        return matchPath(patDirs, str, isCaseSensitive, sep);
    }

    /**
     * Tests whether or not a string matches against a pattern.
     * The pattern may contain two special characters:<br>
     * '*' means zero or more characters<br>
     * '?' means one and only one character
     *
     * @param pattern The pattern to match against.
     *                Must not be <code>null</code>.
     * @param str     The string which must be matched against the pattern.
     *                Must not be <code>null</code>.
     * @return <code>true</code> if the string matches against the pattern,
     *         or <code>false</code> otherwise.
     */
    static boolean match(String pattern, String str)
    {
        return match(pattern, str, true);
    }

    /**
     * Tests whether or not a string matches against a pattern.
     * The pattern may contain two special characters:<br>
     * '*' means zero or more characters<br>
     * '?' means one and only one character
     *
     * @param pattern         The pattern to match against.
     *                        Must not be <code>null</code>.
     * @param str             The string which must be matched against the pattern.
     *                        Must not be <code>null</code>.
     * @param isCaseSensitive Whether or not matching should be performed
     *                        case sensitively.
     * @return <code>true</code> if the string matches against the pattern,
     *         or <code>false</code> otherwise.
     */
    static boolean match(String pattern, String str, boolean isCaseSensitive)
    {
        char[] patArr = pattern.toCharArray();
        char[] strArr = str.toCharArray();
        return match(patArr, strArr, isCaseSensitive);
    }

    /**
     * Tests whether or not a string matches against a pattern.
     * The pattern may contain two special characters:<br>
     * '*' means zero or more characters<br>
     * '?' means one and only one character
     *
     * @param patArr The pattern to match against.
     *               Must not be <code>null</code>.
     * @param strArr The string which must be matched against the pattern.
     *               Must not be <code>null</code>.
     * @return <code>true</code> if the string matches against the pattern,
     *         or <code>false</code> otherwise.
     */
    static boolean match(char[] patArr, char[] strArr)
    {
        return match(patArr, strArr, true);
    }

    /**
     * Tests whether or not a string matches against a pattern.
     * The pattern may contain two special characters:<br>
     * '*' means zero or more characters<br>
     * '?' means one and only one character
     *
     * @param patArr          The pattern to match against.
     *                        Must not be <code>null</code>.
     * @param strArr          The string which must be matched against the pattern.
     *                        Must not be <code>null</code>.
     * @param isCaseSensitive Whether or not matching should be performed
     *                        case sensitively.
     * @return <code>true</code> if the string matches against the pattern,
     *         or <code>false</code> otherwise.
     */
    static boolean match(char[] patArr, char[] strArr,
                                boolean isCaseSensitive)
    {
        int patIdxStart = 0;
        int patIdxEnd = patArr.length - 1;
        int strIdxStart = 0;
        int strIdxEnd = strArr.length - 1;
        char ch;

        boolean containsStar = false;
        for (int i = 0; i < patArr.length; i++) {
            if (patArr[i] == '*') {
                containsStar = true;
                break;
            }
        }

        if (!containsStar) {
            // No '*'s, so we make a shortcut
            if (patIdxEnd != strIdxEnd) {
                return false;  // Pattern and string do not have the same size
            }
            for (int i = 0; i <= patIdxEnd; i++) {
                ch = patArr[i];
                if (ch != '?') {
                    if (isCaseSensitive && ch != strArr[i]) {
                        return false; // Character mismatch
                    }
                    if (!isCaseSensitive && Character.toUpperCase(ch) !=
                                            Character.toUpperCase(strArr[i])) {
                        return false;  // Character mismatch
                    }
                }
            }
            return true;  // String matches against pattern
        }

        if (patIdxEnd == 0) {
            return true; // Pattern contains only '*', which matches anything
        }

        // Process characters before first star
        while ((ch = patArr[patIdxStart]) != '*' && strIdxStart <= strIdxEnd) {
            if (ch != '?') {
                if (isCaseSensitive && ch != strArr[strIdxStart]) {
                    return false; // Character mismatch
                }
                if (!isCaseSensitive && Character.toUpperCase(ch) !=
                                        Character.toUpperCase(strArr[strIdxStart])) {
                    return false; // Character mismatch
                }
            }
            patIdxStart++;
            strIdxStart++;
        }
        if (strIdxStart > strIdxEnd) {
            // All characters in the string are used. Check if only '*'s are
            // left in the pattern. If so, we succeeded. Otherwise failure.
            for (int i = patIdxStart; i <= patIdxEnd; i++) {
                if (patArr[i] != '*') {
                    return false;
                }
            }
            return true;
        }

        // Process characters after last star
        while ((ch = patArr[patIdxEnd]) != '*' && strIdxStart <= strIdxEnd) {
            if (ch != '?') {
                if (isCaseSensitive && ch != strArr[strIdxEnd]) {
                    return false; // Character mismatch
                }
                if (!isCaseSensitive && Character.toUpperCase(ch) !=
                                        Character.toUpperCase(strArr[strIdxEnd])) {
                    return false; // Character mismatch
                }
            }
            patIdxEnd--;
            strIdxEnd--;
        }
        if (strIdxStart > strIdxEnd) {
            // All characters in the string are used. Check if only '*'s are
            // left in the pattern. If so, we succeeded. Otherwise failure.
            for (int i = patIdxStart; i <= patIdxEnd; i++) {
                if (patArr[i] != '*') {
                    return false;
                }
            }
            return true;
        }

        // process pattern between stars. padIdxStart and patIdxEnd point
        // always to a '*'.
        while (patIdxStart != patIdxEnd && strIdxStart <= strIdxEnd) {
            int patIdxTmp = -1;
            for (int i = patIdxStart + 1; i <= patIdxEnd; i++) {
                if (patArr[i] == '*') {
                    patIdxTmp = i;
                    break;
                }
            }
            if (patIdxTmp == patIdxStart + 1) {
                // Two stars next to each other, skip the first one.
                patIdxStart++;
                continue;
            }
            // Find the pattern between padIdxStart & padIdxTmp in str between
            // strIdxStart & strIdxEnd
            int patLength = (patIdxTmp - patIdxStart - 1);
            int strLength = (strIdxEnd - strIdxStart + 1);
            int foundIdx = -1;
            strLoop:
            for (int i = 0; i <= strLength - patLength; i++) {
                for (int j = 0; j < patLength; j++) {
                    ch = patArr[patIdxStart + j + 1];
                    if (ch != '?') {
                        if (isCaseSensitive && ch != strArr[strIdxStart + i + j]) {
                            continue strLoop;
                        }
                        if (!isCaseSensitive && Character.toUpperCase(ch) !=
                                                Character.toUpperCase(strArr[strIdxStart + i + j])) {
                            continue strLoop;
                        }
                    }
                }

                foundIdx = strIdxStart + i;
                break;
            }

            if (foundIdx == -1) {
                return false;
            }

            patIdxStart = patIdxTmp;
            strIdxStart = foundIdx + patLength;
        }

        // All characters in the string are used. Check if only '*'s are left
        // in the pattern. If so, we succeeded. Otherwise failure.
        for (int i = patIdxStart; i <= patIdxEnd; i++) {
            if (patArr[i] != '*') {
                return false;
            }
        }
        return true;
    }

    /**
     * Tests whether or not a string matches against a pattern.
     * The pattern may contain two special characters:<br>
     * '*' means zero or more characters<br>
     * '?' means one and only one character
     *
     * @param patArr          The pattern to match against.
     *                        Must not be <code>null</code>.
     * @param strArr          The string which must be matched against the pattern.
     *                        Must not be <code>null</code>.
     * @param isCaseSensitive Whether or not matching should be performed
     *                        case sensitively.
     * @return <code>true</code> if the string matches against the pattern,
     *         or <code>false</code> otherwise.
     */
    static boolean match(char[] patArr, CharSequence strArr,
                                boolean isCaseSensitive)
    {
        int patIdxStart = 0;
        int patIdxEnd = patArr.length - 1;
        int strIdxStart = 0;
        int strIdxEnd = strArr.length() - 1;
        char ch;

        boolean containsStar = false;
        for (int i = 0; i < patArr.length; i++) {
            if (patArr[i] == '*') {
                containsStar = true;
                break;
            }
        }

        if (!containsStar) {
            // No '*'s, so we make a shortcut
            if (patIdxEnd != strIdxEnd) {
                return false;  // Pattern and string do not have the same size
            }
            for (int i = 0; i <= patIdxEnd; i++) {
                ch = patArr[i];
                if (ch != '?') {
                    if (isCaseSensitive && ch != strArr.charAt(i)) {
                        return false; // Character mismatch
                    }
                    if (!isCaseSensitive && Character.toUpperCase(ch) !=
                                            Character.toUpperCase(strArr.charAt(i))) {
                        return false;  // Character mismatch
                    }
                }
            }
            return true;  // String matches against pattern
        }

        if (patIdxEnd == 0) {
            return true; // Pattern contains only '*', which matches anything
        }

        // Process characters before first star
        while ((ch = patArr[patIdxStart]) != '*' && strIdxStart <= strIdxEnd) {
            if (ch != '?') {
                if (isCaseSensitive && ch != strArr.charAt(strIdxStart)) {
                    return false; // Character mismatch
                }
                if (!isCaseSensitive && Character.toUpperCase(ch) !=
                                        Character.toUpperCase(strArr.charAt(strIdxStart))) {
                    return false; // Character mismatch
                }
            }
            patIdxStart++;
            strIdxStart++;
        }
        if (strIdxStart > strIdxEnd) {
            // All characters in the string are used. Check if only '*'s are
            // left in the pattern. If so, we succeeded. Otherwise failure.
            for (int i = patIdxStart; i <= patIdxEnd; i++) {
                if (patArr[i] != '*') {
                    return false;
                }
            }
            return true;
        }

        // Process characters after last star
        while ((ch = patArr[patIdxEnd]) != '*' && strIdxStart <= strIdxEnd) {
            if (ch != '?') {
                if (isCaseSensitive && ch != strArr.charAt(strIdxEnd)) {
                    return false; // Character mismatch
                }
                if (!isCaseSensitive && Character.toUpperCase(ch) !=
                                        Character.toUpperCase(strArr.charAt(strIdxEnd))) {
                    return false; // Character mismatch
                }
            }
            patIdxEnd--;
            strIdxEnd--;
        }
        if (strIdxStart > strIdxEnd) {
            // All characters in the string are used. Check if only '*'s are
            // left in the pattern. If so, we succeeded. Otherwise failure.
            for (int i = patIdxStart; i <= patIdxEnd; i++) {
                if (patArr[i] != '*') {
                    return false;
                }
            }
            return true;
        }

        // process pattern between stars. padIdxStart and patIdxEnd point
        // always to a '*'.
        while (patIdxStart != patIdxEnd && strIdxStart <= strIdxEnd) {
            int patIdxTmp = -1;
            for (int i = patIdxStart + 1; i <= patIdxEnd; i++) {
                if (patArr[i] == '*') {
                    patIdxTmp = i;
                    break;
                }
            }
            if (patIdxTmp == patIdxStart + 1) {
                // Two stars next to each other, skip the first one.
                patIdxStart++;
                continue;
            }
            // Find the pattern between padIdxStart & padIdxTmp in str between
            // strIdxStart & strIdxEnd
            int patLength = (patIdxTmp - patIdxStart - 1);
            int strLength = (strIdxEnd - strIdxStart + 1);
            int foundIdx = -1;
            strLoop:
            for (int i = 0; i <= strLength - patLength; i++) {
                for (int j = 0; j < patLength; j++) {
                    ch = patArr[patIdxStart + j + 1];
                    if (ch != '?') {
                        if (isCaseSensitive && ch != strArr.charAt(strIdxStart + i + j)) {
                            continue strLoop;
                        }
                        if (!isCaseSensitive && Character.toUpperCase(ch) !=
                                                Character.toUpperCase(strArr.charAt(strIdxStart + i + j))) {
                            continue strLoop;
                        }
                    }
                }

                foundIdx = strIdxStart + i;
                break;
            }

            if (foundIdx == -1) {
                return false;
            }

            patIdxStart = patIdxTmp;
            strIdxStart = foundIdx + patLength;
        }

        // All characters in the string are used. Check if only '*'s are left
        // in the pattern. If so, we succeeded. Otherwise failure.
        for (int i = patIdxStart; i <= patIdxEnd; i++) {
            if (patArr[i] != '*') {
                return false;
            }
        }
        return true;
    }

    /**
     * Breaks a path up into a List of path elements, tokenizing on
     * <code>File.separator</code>.
     *
     * @param path Path to tokenize. Must not be <code>null</code>.
     * @return a List of path elements from the tokenized path
     */
    static String[] tokenizePath(String path, char sep)
    {
        return DataUtil.toStrings(path, sep);
    }

    /**
     * Breaks a path up into a List of path elements, tokenizing on
     * <code>File.separator</code>.
     *
     * @param path Path to tokenize. Must not be <code>null</code>.
     * @return a List of path elements from the tokenized path
     */
    static String[] tokenizePath(String path)
    {
        return tokenizePath(path, DEFAULT_SEPARATOR);
    }

    /**
     * 根据模式选取
     *
     * @param pattern 模式
     * @param strs    字符串
     */
    static String[] selectPath(String pattern, Iterator<String> strs)
    {
        List<String> list = new ArrayList<>();
        while (strs.hasNext()) {
            String str = strs.next();
            if (matchPath(pattern, str)) {
                list.add(str);
            }
        }
        String[] selected = new String[list.size()];
        list.toArray(selected);
        return selected;
    }

    /**
     * 根据模式选取
     *
     * @param pattern 模式
     * @param strs    字符串
     */
    static String[] selectPath(String[] pattern, Iterator<String> strs)
    {
        List<String> list = new ArrayList<>();
        while (strs.hasNext()) {
            String str = (String)strs.next();
            if (matchPath(pattern, str)) {
                list.add(str);
            }
        }
        String[] selected = new String[list.size()];
        list.toArray(selected);
        return selected;
    }

    /**
     * 根据模式选取
     *
     * @param pattern 模式
     * @param strs    字符串
     */
    static String[] select(String pattern, Iterator<String> strs)
    {
        List<String> list = new ArrayList<>();
        while (strs.hasNext()) {
            String str = strs.next();
            if (matchPath(pattern, str)) {
                list.add(str);
            }
        }
        String[] selected = new String[list.size()];
        list.toArray(selected);
        return selected;
    }
}
