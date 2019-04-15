package org.femtoframework.install;

import java.io.File;

public interface Installation {

    String PRODUCT_HOME = "product_home";

    String BIN = "bin";
    String LIB = "lib";
    String VAR = "var";
    String CONF = "conf";
    String TEMP = "temp";

    /**
     * Return the home installation directory
     *
     * @return Installation Directory
     */
    static String getHome() {
        return System.getProperty(PRODUCT_HOME);
    }

    /**
     * Return the sub directory
     *
     * @param subDir Sub Dir
     * @return Dir
     */
    static File getDir(String subDir) {
        return new File(getHome(), subDir);
    }
}
