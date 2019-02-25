package org.femtoframework.util.i18n;

import java.io.IOException;
import java.io.Writer;
import java.util.Locale;
import java.util.Set;

/**
 * 消息集合接口
 *
 * @author fengyun
 * @version 1.00 2005-2-8 21:55:55
 */
public interface Messages
{
    /**
     * 消息集合名称，跟资源集合名称相同
     */
    String getName();

    /**
     * 消息集合所在的Locale
     */
    Locale getLocale();

    /**
     * 返回所有的key
     *
     * @return 所有的key
     */
    Set<String> keySet();

    /**
     * 是否存在消息
     *
     * @param key 消息名称
     */
    boolean hasMessage(String key);

    /**
     * Returns a text message after parametric replacement of the specified
     * parameter placeholders.  A null string result will be returned by
     * this method if no resource bundle has been configured.
     *
     * @param key  The message key to look up
     * @param args An array of replacement parameters for placeholders
     */
    String getMessage(String key, Object... args);

    /**
     * Returns a text message after parametric replacement of the specified
     * parameter placeholders.  A null string result will be returned by
     * this method if no resource bundle has been configured.
     *
     * @param sb   StringBuffer to append
     * @param key  The message key to look up
     * @param args An array of replacement parameters for placeholders
     */
    void appendMessage(StringBuilder sb, String key, Object... args);

    /**
     * Returns a text message after parametric replacement of the specified
     * parameter placeholders.  A null string result will be returned by
     * this method if no resource bundle has been configured.
     *
     * @param out  Writer to write
     * @param key  The message key to look up
     * @param args An array of replacement parameters for placeholders
     */
    void writeMessage(Writer out, String key, Object... args)
        throws IOException;

}
