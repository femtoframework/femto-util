package org.femtoframework.bean;

/**
 * Initializable
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface Initializable {

    /**
     * Initialize the bean
     *
     * @throws org.femtoframework.bean.exception.InitializeException
     */
    void init();
}
