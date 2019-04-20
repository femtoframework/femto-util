package org.femtoframework.text.csv;

import org.femtoframework.io.IOUtil;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * CsvReader
 *
 * @author fengyun
 * @version 1.00 2004-7-29 17:06:58
 */
public class CsvReader implements CsvConstants {
    private Reader reader;

    private static String END_OF_LINE = "_END_OF_LINE_";


    private char fieldDelim = COMMA;
    private char lineDelim = LF;

    /**
     * 是否是新的一行开始
     */
    private boolean newLine = false;

    /**
     * 构造
     *
     * @param reader
     */
    public CsvReader(Reader reader) {
        this.reader = reader;
    }

    /**
     * 设置字段分隔符
     *
     * @param ch
     */
    public void setFieldDelimiter(char ch) {
        fieldDelim = ch;
    }

    /**
     * 设置行分隔
     *
     * @param ch
     */
    public void setLineDelimiter(char ch) {
        lineDelim = ch;
    }

    /**
     * 读取一行
     *
     * @return
     * @throws IOException
     */
    public String[] readLine() throws IOException {
        List<String> list = new ArrayList<>();
        String str;

        while (true) {
            str = readField();
            if (str == null || str.equals(END_OF_LINE)) {
                break;
            }
            list.add(str);
        }

        if (list.isEmpty()) {
            return null;
        }

        String[] fields = new String[list.size()];
        list.toArray(fields);
        return fields;
    }

    /**
     * 读取字段
     *
     * @return
     * @throws IOException
     */
    private String readField() throws IOException {
        if (this.newLine) {
            this.newLine = false;
            return END_OF_LINE;
        }

        int ch = this.reader.read();

        if (ch == -1) {
            return null;
        }
        else if (ch == CR) {
            ch = reader.read();
        }

        StringBuilder sb = null;
        boolean quoted = false;
        if (ch == '"') {
            quoted = true;
            sb = new StringBuilder();
        }
        else if (ch == lineDelim) {
            newLine = true;
            return "";
        }
        else if (ch == fieldDelim) {
            return "";
        }
        else {
            sb = new StringBuilder();
            sb.append((char) ch);
        }

        int last = -1;
        while ((ch = this.reader.read()) != -1) {
            if (quoted) {
                if (ch == '"') {
                    if (last == '"' || last == '\\') {
                        // forget about this quote and move on
                        last = -1;
                        sb.append('"');
                        continue;
                    }
                    last = '"';
                    continue;
                }
                else if (ch == '\\') {
                    if (last == '\\') {
                        sb.append('\\');
                        last = -1;
                        continue;
                    }
                    else {
                        last = '\\';
                    }
                    continue;
                }
                else if (ch == fieldDelim) {
                    if (last == '"') {
                        break;
                    }
                }
                else if (ch == CR) {
                    continue;
                }
                else if (ch == lineDelim) {
                    if (last == '"') {
                        newLine = true;
                        break;
                    }
                }
                last = ch;
            }
            else {
                if (ch == lineDelim) {
                    this.newLine = true;
                    break;
                }
                else if (ch == CR) {
                    //忽略
                    continue;
                }
                else if (ch == fieldDelim) {
                    break;
                }
            }

            sb.append((char) ch);
        }

        return sb.toString();
    }

    public void close() throws IOException {
        IOUtil.close(reader);
    }

}
