package org.femtoframework.bean;

/**
 * Initializable MBean
 *
 * Guarantee that the init method won't be executed twice
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface InitializableMBean extends Initializable {

    /**
     * Return whether it is initialized
     *
     * @return whether it is initialized
     */
    boolean isInitialized();


    /**
     * Initialized setter for internal
     *
     * @param initialized BeanPhase
     */
    void _doSetInitialized(boolean initialized);

    /**
     * Initialize the bean
     *
     * @throws org.femtoframework.bean.exception.InitializeException
     */
    default void init() {
        if (!isInitialized()) {
            _doSetInitialized(false);
            _doInit();
            _doSetInitialized(true);
        }
    }

    /**
     * Initiliaze internally
     */
    void _doInit();

}
