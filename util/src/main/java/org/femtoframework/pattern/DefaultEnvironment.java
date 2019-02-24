package org.femtoframework.pattern;

/**
 * Default Environment definition
 */
public enum DefaultEnvironment implements Environment {
    DEV, QA, PRODUCTION;

    /**
     * Is it a dev environment
     *
     * @return Dev
     */
    @Override
    public boolean isDev() {
        return this == DEV;
    }

    /**
     * Is it a qa environment
     *
     * @return For QA testing or E2E
     */
    @Override
    public boolean isQa() {
        return this == QA;
    }

    /**
     * Is it a production environment
     *
     * @return Production
     */
    @Override
    public boolean isProduction() {
        return this == PRODUCTION;
    }
}
