package org.femtoframework.util;

import org.femtoframework.lang.Octet;
import org.femtoframework.util.crypto.Hex;

/**
 * Character related Utilities
 *
 * @author Sheldon Shao
 */
public interface CharUtil {

    String LINE_SEPARATOR = System.getProperty("line.separator", "\n");
    byte[] LINE_SEPARATOR_BYTES = LINE_SEPARATOR.getBytes();
    char[] LINE_SEPARATOR_CHARS = LINE_SEPARATOR.toCharArray();

    String CRLF = "\r\n";
    byte[] CRLF_BYTES = CRLF.getBytes();
    char[] CRLF_CHARS = CRLF.toCharArray();

    /**
     * Determine whether a character is a hexadecimal character.
     *
     * @return true if the char is betweeen '0' and '9', 'a' and 'f'
     *         or 'A' and 'F', false otherwise
     */
    static boolean isHex(int c)
    {
        return (Octet.isDigit(c) ||
                (c >= 'a' && c <= 'f') ||
                (c >= 'A' && c <= 'F'));
    }

    static char toUnicodeChar(String str, int begin)
    {
        if (str == null || (str.length() - begin) < 6) {
            throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
        }

        int i = begin;

        char c = str.charAt(i++);
        if (c != '\\') {
            throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
        }
        c = str.charAt(i++);

        if (c != 'u' && c != 'U') {
            throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
        }

        int value = 0;
        for (; i < begin + 6; i++) {
            c = str.charAt(i);
            if (c >= '0' && c <= '9') {
                value = (value << 4) + c - '0';
            }
            else if (c >= 'a' && c <= 'f') {
                value = (value << 4) + 10 + c - 'a';
            }
            else if (c >= 'A' && c <= 'F') {
                value = (value << 4) + 10 + c - 'A';
            }
            else {
                throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
            }
        }
        return (char)value;
    }

    /**
     * Convert "\\uxxxx" to unicode char
     */
    static char toUnicodeChar(String str)
    {
        return toUnicodeChar(str, 0);
    }

    /**
     * Convert char to "\\uxxxx"
     */
    static String toUnicodeStr(char c)
    {
        StringBuilder sb = new StringBuilder(6);
        sb = toUnicodeStr(sb, c);
        return sb.toString();
    }

    static StringBuilder toUnicodeStr(StringBuilder sb, char c)
    {
        sb.append('\\');
        sb.append('u');
        sb.append(toHex((c >> 12) & 0xF));
        sb.append(toHex((c >> 8) & 0xF));
        sb.append(toHex((c >> 4) & 0xF));
        sb.append(toHex(c & 0xF));
        return sb;
    }

    /**
     * Convert a nibble to a hex character
     *
     * @param nibble the nibble to convert.
     */
    static char toHex(int nibble)
    {
        return Hex.HEX_CHARS[(nibble & 0xF)];
    }
}
