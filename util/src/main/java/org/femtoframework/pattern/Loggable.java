package org.femtoframework.pattern;

import org.slf4j.Logger;

/**
 * Indicates a object should have Logger
 */
public interface Loggable {

    /**
     * Logger
     *
     * @return Logger
     */
    Logger getLogger();


    /**
     * Sets Logger
     *
     * @param logger Logger
     */
    void setLogger(Logger logger);
}
