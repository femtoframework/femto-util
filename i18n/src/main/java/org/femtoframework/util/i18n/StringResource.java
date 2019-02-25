package org.femtoframework.util.i18n;

import org.femtoframework.util.StringUtil;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple class for handle string resource
 * It holds bytes and chars of the resource.
 * It will faster when format string than ever.
 */
class StringResource
{
    private String resource;
    private char[] chars;
    private List charChunks;

    /**
     * The resource is simple means no argument will support.
     */
    private boolean simple = true;

    public StringResource(String resource)
    {
        this.resource = resource;
        parse();
    }

    public String formatString(Object... args)
    {
        StringBuilder sb = new StringBuilder();
        return format(sb, args).toString();
    }

 /**
     * 格式化成<code>StringBuffer</code>
     *
     * @param sb   [StringBuffer]
     * @param args Object[] 参数
     * @return [StringBuffer] 结果
     */
    public StringBuilder format(StringBuilder sb, Object... args)
    {
        if (simple || args == null || args.length == 0) {
            sb.append(resource);
            return sb;
        }

        int size = charChunks.size();
        int argSize = args.length;

        for (int i = 0; i < size; i++) {
            Object object = charChunks.get(i);
            if (object instanceof Chunk) {
                Chunk chunk = (Chunk)object;
                sb.append(chars, chunk.offset, chunk.length);
            }
            else { //Argument
                int index = ((Argument)object).index;
                if (index < argSize && args[index] != null) {
                    sb.append(args[index]);
                }
            }
        }
        return sb;
    }

    /**
     * 格式化成<code>StringBuffer</code>
     *
     * @param out  Writer
     * @param args Object[] 参数
     */
    public void writeTo(Writer out, Object... args)
        throws IOException
    {
        if (simple || args == null || args.length == 0) {
            out.write(resource);
        }

        int size = charChunks.size();
        int argSize = args.length;

        for (int i = 0; i < size; i++) {
            Object object = charChunks.get(i);
            if (object instanceof Chunk) {
                Chunk chunk = (Chunk)object;
                out.write(chars, chunk.offset, chunk.length);
            }
            else { //Argument
                int index = ((Argument)object).index;
                if (index < argSize && args[index] != null) {
                    out.write(String.valueOf(args[index]));
                }
            }
        }
    }

    public boolean isSimple()
    {
        return simple;
    }

    /**
     * Parse the string seach "{ argument }"
     */
    private synchronized void parse()
    {
        if (resource == null) {
            return;
        }
        if (resource.length() == 0) {
            return;
        }

        chars = resource.toCharArray();
        parse0(chars);
    }

    public String toString()
    {
        return resource;
    }

    private void parse0(char[] chars)
    {
        int length = chars.length;

        int start = 0;
        int end = 0;
        int b = 0;
        int next = MATCH_LEFT_BRACKET;
        int number = -1;

        for (int i = 0; i < length; i++) {
            b = chars[i];
            if (next == MATCH_LEFT_BRACKET) {
                if (b == '{') {
                    end = i;
                    number = -1;
                    next = MATCH_DIGIT;
                }
            }
            else if (next == MATCH_DIGIT) {
                if (Character.isDigit(b)) {
                    if (number == -1) {
                        number = 0;
                    }
                    number = number * 10 + (b - '0');
                }
                else if (Character.isWhitespace(b)) {
                    if (number >= 0) {
                        next = MATCH_RIGHT_BRACKET;
                    }
                }
                else if (b == '{') {
                    //start here
                    number = -1;
                    end = i;
                }
                else if (b == '}') {
                    if (number >= 0) {
                        matched(start, end, number);
                        start = i + 1;
                    }
                    next = MATCH_LEFT_BRACKET;
                }
                else {
                    next = MATCH_LEFT_BRACKET;
                    number = -1;
                }
            }
            else { //find '}'
                if (b == '}') {
                    matched(start, end, number);
                    start = i + 1;
                }
                else if (b == '{') {
                    next = MATCH_DIGIT;
                    number = -1;
                    end = i;
                }
                else if (!Character.isWhitespace(b)) {
                    next = MATCH_LEFT_BRACKET;
                    number = -1;
                }
            }
        }

        if (start == 0) {
            simple = true;
        }
        else {
            simple = false;
            if (start < length) {
                Chunk chunk = new Chunk(start, length - start);
                charChunks.add(chunk);
            }
        }
    }

    private void matched(int chunkStart, int chunkEnd, int argument)
    {
        Chunk chunk = new Chunk(chunkStart, chunkEnd - chunkStart);
        Argument arg = new Argument(argument);
        if (charChunks == null) {
            charChunks = new ArrayList();
        }
        charChunks.add(chunk);
        charChunks.add(arg);
    }

    private static final int MATCH_LEFT_BRACKET = 1;
    private static final int MATCH_DIGIT = 2;
    private static final int MATCH_RIGHT_BRACKET = 3;

    public int hashCode()
    {
        if (resource != null) {
            return resource.hashCode();
        }
        return 0;
    }

    public boolean equals(Object obj)
    {
        if (obj instanceof StringResource) {
            StringResource res = (StringResource)obj;
            return (resource == null && res.resource == null) ||
                    (resource != null && resource.equals(res.resource));
        }
        else if (obj instanceof String) {
            return StringUtil.equals(resource, (String)obj);
        }
        return false;

    }

    private static class Chunk
    {
        int offset;
        int length;

        Chunk(int offset, int length)
        {
            this.offset = offset;
            this.length = length;
        }
    }

    private static class Argument
    {
        int index;

        Argument(int index)
        {
            this.index = index;
        }
    }
}
