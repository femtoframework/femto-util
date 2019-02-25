package org.femtoframework.util.i18n.resources;

import org.femtoframework.io.IOUtil;
import org.femtoframework.lang.reflect.Reflection;
import org.femtoframework.util.CollectionUtil;
import org.femtoframework.util.MessageProperties;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;

/**
 * 消息资源适配器
 * 从Classpath中找到第一个指定的配置文件
 *
 * @author fengyun
 * @version 1.00 2004-7-23 11:40:14
 */
public class PropertiesResources
    extends AbstractReloadableResources
{

    private URL url;

    protected Properties resources = CollectionUtil.EMPTY_PROPERTIES;

    /**
     * 构造
     *
     * @param name
     * @param locale
     */
    public PropertiesResources(String name, Locale locale)
    {
        super(name, locale);
        init();
    }

    /**
     * 返回所有的key
     *
     * @return 所有的key
     */
    public Set<String> keySet()
    {
        check();
        Set<Object> origSet = resources.keySet();
        HashSet<String> set = new HashSet<>(origSet.size());
        origSet.forEach( k -> set.add(String.valueOf(k)));
        return set;
    }

    /**
     * 是否存在资源
     *
     * @param key 资源名称
     * @return
     */
    public boolean hasResource(String key)
    {
        check();
        return resources.containsKey(key);
    }

    /**
     * 以字符串的形式返回key对应的资源
     *
     * @param key key
     */
    public String getString(String key)
    {
        check();
        return resources.getProperty(key);
    }

    /**
     * 重新装载
     */
    public void reload()
    {
        init();
    }

    /**
     * 判断指定key的资源是否更新
     *
     * @return
     */
    public boolean isModified(String key)
    {
        return isModified();
    }

    /**
     * Return the real bundle name
     * example:
     * base name--> org.femtoframework.util.message.resources.message
     * bundle 1 --> org.femtoframework.util.message.resources.message_zh_CN
     * bundle 2 --> org.femtoframework.util.message.resources.message_ja_JP
     * bundle 3 --> org.femtoframework.util.message.resources.message_en_US
     */
    protected String getBundleName(String baseName, Locale locale)
    {
        if (locale == null) {
            return baseName;
        }

        StringBuilder bundleName = new StringBuilder(baseName);
        String localeSuffix = locale.toString();
        bundleName.append('_').append(localeSuffix);
        return bundleName.toString();
    }

    /**
     * 根据返回实际的URL
     *
     * @return
     */
    protected URL getResource()
    {
        String baseName = getName();
        Locale locale = getLocale();
        boolean isUrl = baseName.indexOf(':') > 0;
        boolean isFile = baseName.indexOf('/') >= 0;
        if (!isUrl && isFile) {
            File file = new File(baseName);
            URL url;
            try {
                url = file.toURI().toURL();
                baseName = url.toExternalForm();
            }
            catch (MalformedURLException e) {
            }
            isUrl = true;
        }
        String bundleName = getBundleName(baseName, locale);
        String resName = (isUrl ? bundleName : bundleName.replace('.', '/')) + ".properties";

        return doGetResource(isUrl, resName);
    }

    protected URL doGetResource(boolean isUrl, final String resName)
    {
        URL resource = null;
        if (isUrl) {
            try {
                resource = new URL(resName);
            }
            catch (MalformedURLException e) {
                //
            }
        }
        else {
            final ClassLoader loader = Reflection.getClassLoader();
            resource = (URL) AccessController.doPrivileged((PrivilegedAction) () -> loader.getResource(resName));
        }
        if (resource == null) {
            //Load From File
            File file = new File(resName);
            if (file.exists()) {
                try {
                    resource = file.toURI().toURL();
                }
                catch (Exception e) {
                    //
                }
            }
        }

        return resource;
    }

    /**
     * Create a new MessageResourceBundle instance
     *
     * @see java.util.PropertyResourceBundle
     */
    protected void init()
    {
        URL resource = getResource();
        Charset charset = StandardCharsets.UTF_8;

        if (resource != null) {
            //本地文件的才可以自动装载
            if ("file".equalsIgnoreCase(resource.getProtocol())) {
                setReloadable(true);
            }
            else {
                setReloadable(false);
            }

            InputStream input = null;
            URLConnection conn = null;
            try {
                conn = resource.openConnection();
                conn.setUseCaches(false);
                conn.setDoInput(true);
                input = conn.getInputStream();
                this.url = resource;
                this.resources = createProperties(input, charset);
            }
            catch (Exception e) {
                throw new MissingResourceException("Can't create resource bundle:" + resource.toExternalForm(),
                    resource.toExternalForm(), charset.toString());
            }
            finally {
                IOUtil.close(input);
            }
        }
        else {
            throw new MissingResourceException("Can't create resource bundle:" + getName(),
                getName(), charset.toString());
        }
    }

    protected Properties createProperties()
    {
        return new MessageProperties();
    }


    protected Properties createProperties(InputStream input, Charset charset)
        throws IOException
    {
        MessageProperties properties = (MessageProperties) createProperties();
        properties.load(input, charset);
        return properties;
    }

    /**
     * 返回最后更新时间
     *
     * @return 最后更新时间
     */
    public long lastModified()
    {
        if (url == null) {
            return -1;
        }

        return lastModified(url);
    }

    protected long lastModified(URL url)
    {
        URLConnection conn = null;
        try {
            conn = url.openConnection();
            conn.connect();
            return conn.getLastModified();
        }
        catch (IOException e) {
            return -1;
        }
        finally {
            if (conn != null) {
                if (conn instanceof HttpURLConnection) {
                    ((HttpURLConnection) conn).disconnect();
                }
            }
        }
    }

}
