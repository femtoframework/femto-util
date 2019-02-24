package org.femtoframework.pattern;

/**
 * Interface for Environment
 */
public interface Environment {

    String name();

    int ordinal();

    /**
     * Is it a dev environment
     *
     * @return Dev
     */
    boolean isDev();

    /**
     * Is it a qa environment
     *
     * @return For QA testing or E2E
     */
    boolean isQa();

    /**
     * Is it a production environment
     *
     * @return Production
     */
    boolean isProduction();
}
