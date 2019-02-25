package org.femtoframework.util.i18n;

import java.io.IOException;
import java.io.Writer;

/**
 * 抽象消息集合
 *
 * @author fengyun
 * @version 1.00 2004-8-13 21:59:48
 */
public abstract class AbstractMessages
    implements Messages
{
    /**
     * 返回对象
     *
     * @param key key
     * @return
     */
    protected abstract Object doGetObject(String key);

    /**
     * Returns a text message after parametric replacement of the specified
     * parameter placeholders.  A null string result will be returned by
     * this method if no resource bundle has been configured.
     *
     * @param key  The message key to look up
     * @param args An array of replacement parameters for placeholders
     */
    public String getMessage(String key, Object args[])
    {
        String str = null;
        Object object = doGetObject(key);
        if (object != null) {
            if (object instanceof StringResource) {
                str = ((StringResource)object).formatString(args);
            }
            else {
                str = String.valueOf(object);
            }
        }
        return str;
    }

    /**
     * Returns a text message after parametric replacement of the specified
     * parameter placeholders.  A null string result will be returned by
     * this method if no resource bundle has been configured.
     *
     * @param sb   StringBuffer to append
     * @param key  The message key to look up
     * @param args An array of replacement parameters for placeholders
     */
    public void appendMessage(StringBuilder sb, String key, Object args[])
    {
        Object object = doGetObject(key);
        if (object != null) {
            if (object instanceof StringResource) {
                ((StringResource)object).format(sb, args);
            }
            else {
                sb.append(object);
            }
        }
    }

    /**
     * Returns a text message after parametric replacement of the specified
     * parameter placeholders.  A null string result will be returned by
     * this method if no resource bundle has been configured.
     *
     * @param out  Writer to write
     * @param key  The message key to look up
     * @param args An array of replacement parameters for placeholders
     */
    public void writeMessage(Writer out, String key, Object args[])
        throws IOException
    {
        Object object = doGetObject(key);
        if (object != null) {
            if (object instanceof StringResource) {
                ((StringResource)object).writeTo(out, args);
            }
            else if (object instanceof char[]) {
                out.write((char[])object);
            }
            else {
                out.write(String.valueOf(object));
            }
        }
    }
}
