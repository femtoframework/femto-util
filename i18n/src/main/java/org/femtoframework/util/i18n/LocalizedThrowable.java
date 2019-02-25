package org.femtoframework.util.i18n;


import java.util.Locale;

/**
 * 本地化的异常<br>
 * 可以根据Locale来选取不同的状态消息<br>
 *
 * @author fengyun
 */
public interface LocalizedThrowable {
    /**
     * 返回状态消息所属的资源集合名称<br>
     * 格式说明:如果对应的资源文件路径为 org/femtoframework/util/i18n/error_zh_CN.properties的话<br>
     * 相应的资源URI为：org.femtoframework.util.i18n.error
     *
     * @return 资源集合路径
     */
    String getResourcesURI();

//    /**
//     * 设置状态消息所属的资源集合名称<br>
//     * <p/>
//     * 格式说明:如果对应的资源文件路径为 org/femtoframework/util/i18n/error_zh_CN.properties的话<br>
//     * 相应的资源URI为：org.femtoframework.util.i18n.error
//     *
//     * @param resourceURI 资源URI
//     */
//    void setResourcesURI(String resourceURI);

    /**
     * 返回状态相关的参数
     *
     * @return 状态相关的参数
     */
    Object[] getArguments();

    /**
     * 返回原始的消息
     *
     * @return 消息
     */
    String getMessage();

    /**
     * 返回本地化的消息
     */
    default String getLocalizedMessage() {
        return getLocalizedMessage(getDefaultLocale());
    }

    /**
     * 根据MessageLocale返回消息
     *
     * @param locale Locale
     */
    default String getLocalizedMessage(Locale locale) {
        StringBuilder sb = new StringBuilder();
        getLocalizedMessage(sb, locale);
        return sb.toString();
    }

    /**
     * 根据MessageLocale返回完整的本地化消息，包括NextStatus的消息
     *
     * @param locale MessageLocale
     */
    StringBuilder getLocalizedMessage(StringBuilder sb, Locale locale);

    /**
     * 返回完整的本地化消息，包括NextStatus的消息
     *
     * @return 消息
     */
    default String getFullMessage() {
        return getFullMessage(getDefaultLocale());
    }

    /**
     * 根据MessageLocale返回完整的本地化消息，包括NextStatus的消息
     *
     * @param locale MessageLocale
     */
    String getFullMessage(Locale locale);

    /**
     * 获取下一个状态信息
     *
     * @return 下一个状态信息
     */
    LocalizedThrowable getLocalizedCause();

    /**
     * 返回默认的MessageLocale
     *
     * @return Locale
     */
    Locale getDefaultLocale();

    /**
     * 返回默认的MessageLocale
     *
     * @param locale 默认的Locale
     */
    void setDefaultLocale(Locale locale);
}
