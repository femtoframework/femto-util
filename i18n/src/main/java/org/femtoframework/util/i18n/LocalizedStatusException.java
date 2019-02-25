package org.femtoframework.util.i18n;

import org.femtoframework.util.CharUtil;
import org.femtoframework.util.status.StatusException;

import java.util.Locale;

/**
 * LocalizedStatusException
 *
 * @author fengyun
 * @version 1.00 2004-10-20 17:13:52
 */
public class LocalizedStatusException extends StatusException implements LocalizedThrowable {

    /**
     * Resource Bundle 的资源
     */
    private String resourcesURI = null;
    private Object[] arguments = null;
    /**
     * 默认的Locale
     */
    private Locale defaultLocale = Locale.getDefault();

    /**
     * 构造
     *
     * @param status [int] 状态码 <code>message == ""</code>
     */
    public LocalizedStatusException(int status)
    {
        super(status, String.valueOf(status));
    }

    /**
     * 构造
     *
     * @param status [int] 状态码 <code>message == ""</code>
     */
    public LocalizedStatusException(int status, Object... arg)
    {
        this(status, String.valueOf(status), arg);
    }

    /**
     * 构造
     *
     * @param code    状态码
     * @param message 消息
     * @param args    参数
     */
    public LocalizedStatusException(int code, String message, Object... args)
    {
        this(code, message, (Throwable)null, args);
    }

    /**
     * 构造
     *
     * @param code    状态码
     * @param message 消息
     * @param args    参数
     */
    public LocalizedStatusException(int code, String message, Throwable cause, Object... args)
    {
        super(code, message, cause);
        this.arguments = args;
    }

    /**
     * 返回状态消息所属的资源集合名称<br>
     * 格式说明:如果对应的资源文件路径为 org/femtoframework/util/i18n/error_zh_CN.properties的话<br>
     * 相应的资源URI为：org.femtoframework.util.i18n.error
     *
     * @return 资源集合路径
     */
    @Override
    public String getResourcesURI() {
        return resourcesURI;
    }

    /**
     * 设置状态消息所属的资源集合名称<br>
     * <p/>
     * 格式说明:如果对应的资源文件路径为 org/femtoframework/util/i18n/error_zh_CN.properties的话<br>
     * 相应的资源URI为：org.femtoframework.util.i18n.error
     *
     * @param resourcesURI 资源URI
     */
    protected void setResourcesURI(String resourcesURI) {
        this.resourcesURI = resourcesURI;
    }

    /**
     * 返回状态相关的参数
     *
     * @return 状态相关的参数
     */
    @Override
    public Object[] getArguments() {
        return arguments;
    }

    /**
     * 根据MessageLocale返回完整的本地化消息，包括NextStatus的消息
     *
     * @param locale MessageLocale
     */
    public StringBuilder getLocalizedMessage(StringBuilder sb, Locale locale)
    {
        String message = super.getMessage();
        if (message != null) {
            if (resourcesURI != null) {
                Messages mr = MessagesUtil.getMessages(resourcesURI, locale);
                if (mr != null) {
                    String newMessage = mr.getMessage(message, arguments);
                    if (newMessage != null) {
                        message = newMessage;
                    }
                }
            }
            sb.append(message);
        }
        return sb;
    }

    /**
     * 根据MessageLocale返回完整的本地化消息，包括NextStatus的消息
     *
     * @param locale MessageLocale
     */
    @Override
    public String getFullMessage(Locale locale) {
        StringBuilder sb = new StringBuilder();
        getLocalizedMessage(sb, locale);

        Throwable cause = getCause();
        if (cause != null) {
            sb.append(CharUtil.LINE_SEPARATOR_CHARS);
            if (cause instanceof LocalizedThrowable) {
                ((LocalizedThrowable)cause).getLocalizedMessage(sb, locale);
            }
            else {
                String message = cause.getMessage();
                if (message != null) {
                    sb.append(message);
                }
            }
        }

        return sb.toString();
    }

    /**
     * 获取下一个状态信息
     *
     * @return 下一个状态信息
     */
    @Override
    public LocalizedThrowable getLocalizedCause() {
        Throwable cause = getCause();
        if (cause != null) {
            if (cause instanceof LocalizedThrowable) {
                return (LocalizedThrowable)cause;
            }
        }
        return null;
    }

    /**
     * 返回默认的MessageLocale
     *
     * @return Locale
     */
    @Override
    public Locale getDefaultLocale() {
        return defaultLocale;
    }

    /**
     * 返回默认的MessageLocale
     *
     * @param locale 默认的Locale
     */
    @Override
    public void setDefaultLocale(Locale locale) {
        this.defaultLocale = locale;
    }
}
