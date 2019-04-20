package org.femtoframework.text.csv;

import org.femtoframework.io.IOUtil;
import org.femtoframework.util.StringUtil;

import java.io.IOException;
import java.io.Writer;

/**
 * CSV输出
 */
public class CsvWriter
    implements CsvConstants
{

    private char fieldDelim = COMMA;
    private char[] lineDelim = CRLF;

    private Writer writer;
    private boolean written = false;

    public CsvWriter(Writer writer)
    {
        this.writer = writer;
    }

    public Writer getWriter()
    {
        return this.writer;
    }

    public void setFieldDelimiter(char ch)
    {
        fieldDelim = ch;
    }

    /**
     * 设置行分隔符
     *
     * @param lineDelim
     */
    public void setLineDelimiter(String lineDelim)
    {
        this.lineDelim = lineDelim.toCharArray();
    }

    /**
     * 输出一个字段
     *
     * @param field
     * @throws IOException
     */
    public void writeField(String field) throws IOException
    {
        if (written) {
            writer.write(fieldDelim);
            written = false;
        }

        writer.write(escape(field));
        written = true;
    }

    public void endBlock() throws IOException
    {
        writer.write(lineDelim);
        written = false;
    }

    public void writeLine(String[] strs) throws IOException
    {
        int sz = strs.length;
        for (int i = 0; i < sz; i++) {
            writeField(strs[i]);
        }
        endBlock();
    }

    public void flush() throws IOException
    {
        writer.flush();
    }

    public void close() throws IOException
    {
        IOUtil.close(writer);
    }

    private String escape(String word)
    {
        if (StringUtil.isInvalid(word)) {
            return "";
        }

        int len = word.length();

        /*
         * Look for any "bad" characters, Escape and
         *  quote the entire string if necessary.
         */
        boolean needQuoting = false;
        for (int i = 0; i < len; i++) {
            char c = word.charAt(i);
            if (c == '"' || c == '\\' || c == CR || c == LF) {
                // need to escape them and then quote the whole string
                StringBuilder sb = new StringBuilder(len + 3);
                sb.append('"');
                sb.append(word.substring(0, i));
                for (int j = i; j < len; j++) {
                    char cc = word.charAt(j);
                    if ((cc == '"') || (cc == '\\')) {
                        sb.append('\\');    // Escape the character
                    }
                    sb.append(cc);
                }
                sb.append('"');
                return sb.toString();
            }
            else if (c < 0x20 || c >= 0x7f || fieldDelim == c) {
                // These characters cause the string to be quoted
                needQuoting = true;
            }
        }

        if (needQuoting) {
            StringBuilder sb = new StringBuilder(len + 2);
            sb.append('"').append(word).append('"');
            return sb.toString();
        }
        else {
            return word;
        }
    }

}
