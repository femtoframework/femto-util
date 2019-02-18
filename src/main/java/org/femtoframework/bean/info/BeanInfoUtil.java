package org.femtoframework.bean.info;

import org.femtoframework.implement.ImplementUtil;

/**
 * Bean Info Access Entry
 */
public class BeanInfoUtil {

    private static BeanInfoFactory beanInfoFactory = null;

    public static BeanInfoFactory getBeanInfoFactory() {
        if (beanInfoFactory == null) {
            beanInfoFactory = ImplementUtil.getInstance(BeanInfoFactory.class);
        }
        return beanInfoFactory;
    }


    /**
     * Retrieve BeanInfo by class
     */
    public static BeanInfo getBeanInfo(Class clazz) {
        return getBeanInfoFactory().getBeanInfo(clazz);
    }

    /**
     * Retrieve BeanInfo by class
     *
     * @param clazz
     * @param generate Generate the beanInfo automatically when 'generate' is true
     */
    public static BeanInfo getBeanInfo(Class clazz, boolean generate) {
        return getBeanInfoFactory().getBeanInfo(clazz, generate);
    }
}
