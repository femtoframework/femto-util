package org.femtoframework.util.i18n.resources;

/**
 * 需要资源URI的接口注入
 *
 * @author fengyun
 * @version 1.00 2006-7-27 10:48:57
 */
public interface ResourcesUriAware
{
    /**
     * 设置资源路径
     *
     * @param resourcesUri 资源路径
     */
    void setResourcesUri(String resourcesUri);
    
    /**
     * 返回资源URI
     *
     *
     * @return 资源URI
     */
    String getResourcesUri();
}
