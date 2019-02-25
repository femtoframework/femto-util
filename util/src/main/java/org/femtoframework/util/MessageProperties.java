package org.femtoframework.util;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Properties;

/**
 * The class is a sub-class of java.util.Properties.
 * It's used to load properties with other character set
 * instead of 8859_1
 *
 * Since UTF8 or GBK characters have to been encoded.
 * It makes inconvenience for the people who are using CJKV in property files.
 *
 * @author fengyun
 * @version 1.00
 */
public class MessageProperties extends Properties
{
    /**
     * The character set used in values
     */
    protected Charset charset = null;

    /**
     * Constructer
     */
    public MessageProperties()
    {
        super();
    }

    /**
     * Constructer
     *
     * @param fileName properties file name
     */
    public MessageProperties(String fileName)
            throws IOException
    {
        this(fileName, Charset.defaultCharset());
    }

    public MessageProperties(Properties props)
    {
        super(props);
    }

    /**
     * Constructer
     *
     * @param fileName properties file name
     * @param charset  character set
     */
    public MessageProperties(String fileName, Charset charset)
            throws IOException
    {
        load1(fileName, charset);
    }

    protected void load1(String fileName, Charset charset)
            throws IOException
    {
        setCharset(charset);
        try (FileInputStream fis = new FileInputStream(fileName)){
            BufferedInputStream bis = new BufferedInputStream(fis);
            load(bis, this.charset);
        }
        catch (IOException ioe) {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream is = loader.getResourceAsStream(fileName);
            if (is != null) {
                try {
                    load(is, this.charset);
                    is.close();
                }
                catch (IOException e) {
                    throw new IOException(e.getMessage());
                }
            }
            else {
                throw new FileNotFoundException("File not found:" + fileName);
            }
        }
    }

    /**
     * Set character set
     */
    protected void setCharset(Charset charset)
    {
        if (charset == null) {
            charset = Charset.defaultCharset();
        }

        this.charset = charset;
    }

    /**
     * Return character set
     */
    public Charset getCharset()
    {
        return this.charset;
    }

    protected static final char[] KEY_VALUE_SEP
            = new char[]{'=', ':', '\t', '\r', '\n', '\f'};

    protected boolean isStrictKeyValueSep(char c)
    {
        return c == '=' || c == ':';
    }

    protected boolean isKeyValueSep(char c)
    {
        for (int i = 0; i < KEY_VALUE_SEP.length; i++) {
            if (c == KEY_VALUE_SEP[i]) {
                return true;
            }
        }
        return false;
    }

    protected static boolean isWhiteSpace(char c)
    {
        return c == ' ' || c == '\t' ||
                c == '\r' || c == '\n' ||
                c == '\f';
    }

    protected static int skipWhiteSpace(String line, int begin, int end)
    {
        int i = begin;
        for (; i < end && isWhiteSpace(line.charAt(i)); i++) {
        }
        return i;
    }

    protected static final String SPECIAL_SAVE_CHARS = " \t\r\n\f#!";

    protected String loadConvert(String str)
    {
        char c;
        int len = str.length();
        StringBuilder sb = new StringBuilder(len);

        for (int i = 0; i < len;) {
            c = str.charAt(i++);
            if (c == '\\') {
                c = str.charAt(i++);
                if (c == 'u') {
                    sb.append(CharUtil.toUnicodeChar(str, i - 2));
                    i += 4;
                }
                else {
                    if (c == 't') {
                        c = '\t';
                    }
                    else if (c == 'r') {
                        c = '\r';
                    }
                    else if (c == 'n') {
                        c = '\n';
                    }
                    else if (c == 'f') {
                        c = '\f';
                    }
                    sb.append(c);
                }
            }
            else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public void load(InputStream inStream) throws IOException
    {
        load(inStream, Charset.defaultCharset());
    }

    /*
     * Returns true if the given line is a line that must
     * be appended to the next line
     */
    protected boolean continueLine(String line)
    {
        int slashCount = 0;
        int index = line.length() - 1;
        while ((index >= 0) && (line.charAt(index--) == '\\')) {
            slashCount++;
        }
        return (slashCount % 2 == 1);
    }

    public synchronized void load(InputStream inStream, Charset charset)
            throws UnsupportedEncodingException, IOException
    {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(inStream, charset));
        while (true) {
            // Get next line
            String line = reader.readLine();
            if (line == null) {
                return;
            }
            handleLine(reader, line);
        }
    }

    public synchronized void store(OutputStream out, String header)
            throws IOException
    {
        store(out, header, null);
    }

    protected void writeHeader(BufferedWriter bw, String header)
            throws IOException
    {
        if (header != null && !header.isEmpty()) {
            bw.write('#');
            writeln(bw, header);
        }
    }

    public synchronized void store(OutputStream out, String header, Charset charset)
            throws IOException
    {
        BufferedWriter bw;
        bw = new BufferedWriter(new OutputStreamWriter(out, charset));
        writeHeader(bw, header);

        for (Enumeration e = keys(); e.hasMoreElements();) {
            String key = (String)e.nextElement();
            String val = getProperty(key);
            if (key != null && val != null) {
                key = saveConvert(key, true);
                val = saveConvert(val, false);
                bw.write(key);
                bw.write('=');
                writeln(bw, val);
            }
        }
        bw.flush();
    }

    protected static void write(BufferedWriter bw, String line)
            throws IOException
    {
        bw.write(line);
    }

    protected static void writeln(BufferedWriter bw, String line)
            throws IOException
    {
        bw.write(line);
        bw.newLine();
    }

    protected String saveConvert(String str, boolean escape)
    {
        int len = str.length();
        StringBuilder sb = new StringBuilder(len * 2);

        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            switch (c) {
                case' ':
                    if (i == 0 || escape) {
                        sb.append('\\');
                    }

                    sb.append(' ');
                    break;
                case'\\':
                    sb.append('\\');
                    sb.append('\\');
                    break;
                case'\t':
                    sb.append('\\');
                    sb.append('t');
                    break;
                case'\n':
                    sb.append('\\');
                    sb.append('n');
                    break;
                case'\r':
                    sb.append('\\');
                    sb.append('r');
                    break;
                case'\f':
                    sb.append('\\');
                    sb.append('f');
                    break;
                default:
                    if (SPECIAL_SAVE_CHARS.indexOf(c) != -1) {
                        sb.append('\\');
                    }
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    protected String handleKey(String key)
    {
        // Convert then store key and value
        return loadConvert(key);
    }

    protected Object handleValue(String value)
    {
        return loadConvert(value);
    }

    protected void handleLine(BufferedReader reader, String line)
            throws IOException
    {
        if (line.length() > 0) {
            // Continue lines that end in slashes if they are not comments
            char firstChar = line.charAt(0);
            if ((firstChar != '#') && (firstChar != '!')) {
                while (continueLine(line)) {
                    String nextLine = reader.readLine();
                    if (nextLine == null) {
                        nextLine = "";
                    }

                    String loppedLine = line.substring(0, line.length() - 1);
                    // Advance beyond whitespace on new line
                    int startIndex = skipWhiteSpace(nextLine, 0, nextLine.length());
                    nextLine = nextLine.substring(startIndex, nextLine.length());
                    line = loppedLine + nextLine;
                }

                // Find start of key
                int len = line.length();
                int keyStart = skipWhiteSpace(line, 0, len);

                // Blank lines are ignored
                if (keyStart == len) {
                    return;
                }

                // Find separation between key and value
                int separatorIndex = keyStart;
                int valueStart = separatorIndex;
                for (; separatorIndex < len; separatorIndex ++ ) {
                    char currentChar = line.charAt(separatorIndex);
                    if (currentChar == '\\') {
                        separatorIndex++;
                    }
                    else if (isKeyValueSep(currentChar)) {
                        valueStart = separatorIndex;
                        break;
                    }
                    else if (isWhiteSpace(currentChar)) {
                        valueStart = skipWhiteSpace(line, separatorIndex, len);
                        break;
                    }
                }

                // Skip over whitespace after key if any
                int valueIndex = skipWhiteSpace(line, valueStart, len);
                // Skip over one non whitespace key value separators if any
                if (valueIndex < len) {
                    if (isStrictKeyValueSep(line.charAt(valueIndex))) {
                        valueIndex++;
                    }
                }

                // Skip over white space after other separators if any
                valueIndex = skipWhiteSpace(line, valueIndex, len);

                String key = line.substring(keyStart, separatorIndex);
                String value = (separatorIndex < len) ? line.substring(valueIndex, len) : "";

                key = handleKey(key);
                Object v = handleValue(value);
                put0(key, v);
            }
        }
    }

    protected void put0(String key, Object value)
    {
        put(key, value);
    }
}