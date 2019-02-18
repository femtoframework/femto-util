package org.femtoframework.bean;

/**
 * Lifecycle MBean
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface LifecycleMBean extends Lifecycle, InitializableMBean {

    /**
     * Return whether it is initialized
     *
     * @return whether it is initialized
     */
    default boolean isInitialized() {
        return getBeanPhase().ordinal() >= BeanPhase.INITIALIZED.ordinal();
    }


    /**
     * Initialized setter for internal
     *
     * @param initialized BeanPhase
     */
    default void _doSetInitialized(boolean initialized) {
        if (initialized) {
            _doSetPhase(BeanPhase.INITIALIZED);
        }
        else {
            _doSetPhase(BeanPhase.INITIALIZING);
        }
    }

    /**
     * Return current phase
     *
     * @return current phase
     */
    default BeanPhase getBeanPhase() {
        BeanPhase beanPhase = _doGetPhase();
        if (beanPhase == null) {
            return BeanPhase.DISABLED;
        }
        return beanPhase;
    }

    /**
     * Implement method of getPhase
     *
     * @return BeanPhase
     */
    BeanPhase _doGetPhase();

    /**
     * Phase setter for internal
     *
     * @param phase BeanPhase
     */
    void _doSetPhase(BeanPhase phase);

    /**
     * Initialize the bean
     *
     * @throws org.femtoframework.bean.exception.InitializeException
     */
    default void initialize() {
        if (getBeanPhase().ordinal() < BeanPhase.INITIALIZING.ordinal()) {
            _doSetPhase(BeanPhase.INITIALIZING);
            _doInitialize();
            _doSetPhase(BeanPhase.INITIALIZED);
        }
    }

    /**
     * Initialize internally
     */
    default void _doInitialize() {

    }

    /**
     * Start
     *
     * @throws org.femtoframework.bean.exception.StartException
     */
    default void start() {
        if (getBeanPhase().ordinal() < BeanPhase.STARTING.ordinal() ) {
            //Make sure it has call initialized already
            initialize();

            _doSetPhase(BeanPhase.STARTING);
            _doStart();
            _doSetPhase(BeanPhase.STARTED);
        }
    }

    /**
     * Start internally
     */
    default void _doStart() {
    }

    /**
     * Stop the bean
     */
    default void stop() {
        if (getBeanPhase().ordinal() < BeanPhase.STOPPING.ordinal() ) {
            _doSetPhase(BeanPhase.STOPPING);
            _doStop();
            _doSetPhase(BeanPhase.STOPPED);
        }
    }

    /**
     * Stop internally
     */
    default void _doStop() {
    }


    /**
     * Destroy the bean
     */
    default void destroy() {
        if (getBeanPhase().ordinal() < BeanPhase.DESTROYING.ordinal() ) {

            //Stop first
            stop();

            _doSetPhase(BeanPhase.DESTROYING);
            _doDestroy();
            _doSetPhase(BeanPhase.DESTROYED);
        }
    }

    /**
     * Destroy internally
     */
    default void _doDestroy() {
    }
}
