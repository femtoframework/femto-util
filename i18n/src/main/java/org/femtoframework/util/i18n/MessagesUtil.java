package org.femtoframework.util.i18n;

import org.femtoframework.util.i18n.resources.Resources;
import org.femtoframework.util.i18n.resources.ResourcesUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 消息集合工具类
 *
 * @author fengyun
 * @version 1.00 2004-8-13 23:54:58
 */
public class MessagesUtil
{
    //双键，其中的值有可能是Messages也有可能是Map，为了节约空间
    private static final Map<String, Map<Locale, Messages>> cache = new HashMap<>();

    /**
     * 根据消息集合名称返回消息集合
     *
     * @param name 名称
     * @return 消息集合
     */
    public static Messages getMessages(String name)
    {
        return getMessages(name, Locale.getDefault());
    }

    /**
     * 根据消息集合名称和MessageLocale返回消息集合
     *
     * @param name   名称
     * @param locale MessageLocale
     * @return 消息集合
     */
    public static Messages getMessages(String name, Locale locale)
    {
        Map<Locale, Messages> obj = cache.computeIfAbsent(name, k -> new HashMap<>());
        Messages messages = obj.get(locale);
        if (messages == null) {
            Resources resources = ResourcesUtil.getResources(name, locale);
            if (resources != null) {
                messages = new SimpleMessages(resources);
                obj.put(locale, messages);
            }
        }
        return messages;
    }

    /**
     * 返回消息
     *
     * @param name 资源名称
     * @param key  资源表达式
     */
    public static String getMessage(String name, String key)
    {
        return getMessage(name, Locale.getDefault(), key);
    }

    /**
     * 返回消息
     *
     * @param name   消息集合名称
     * @param locale MessageLocale
     * @param key    key
     * @param args   参数
     */
    public static String getMessage(String name, Locale locale, String key, Object... args)
    {
        Messages messages = getMessages(name, locale);
        if (messages != null) {
            return messages.getMessage(key, args);
        }
        return null;
    }

    /**
     * 返回消息
     *
     * @param name 消息集合名称
     * @param key  key
     * @param args 参数
     */
    public static String getMessage(String name, String key, Object... args)
    {
        return getMessage(name, Locale.getDefault(), key, args);
    }

}
