package org.femtoframework.util;

import org.femtoframework.lang.reflect.Reflection;
import org.femtoframework.pattern.DefaultEnvironment;
import org.femtoframework.pattern.Environment;

import java.net.InetAddress;
import java.util.Random;

public class SystemUtil {

//    /**
//     * JVM Identity
//     */
    private static String identity = null;

    /**
     * HostName
     */
    private static String hostname = null;

//    private static final char[] ENCODER =
//            "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();


    static {
        StringBuilder key = new StringBuilder();
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            String host = localhost.getHostAddress();
            hostname = localhost.getHostName();
            key.append(host);
        }
        catch (Exception e) {
            key.append(e.getMessage());
        }
        long time = System.currentTimeMillis();
        key.append(time);
        key.append(new Random(time).nextInt());
//        byte[] md5 = MD5.encrypt(key.toString());
//        StringBuilder sb = new StringBuilder(8);
//        for (int i = 0; i < md5.length;) {
//            sb.append(ENCODER[(md5[i++] & 0x30 | md5[i++] & 0x0F) % 36]);
//        }
        identity = "jvm_" + key.hashCode();
        if (hostname == null) {
            hostname = identity;
        }
        int index = hostname.indexOf('.');
        if (index > 0) {
            hostname = hostname.substring(0, index);
        }
    }

    /**
     * 返回系统的主机名
     */
    public static String getHostName() {
        return hostname;
    }


    private static String oSName = null;

    /**
     * 返回系统名称
     *
     * @return 系统名称
     */
    public static String getOSName() {
        if (oSName == null) {
            oSName = System.getProperty("os.name");
        }
        return oSName;
    }

    /**
     * 判断是否是Windows平台
     *
     * @return 是否是Windows平台
     */
    public static boolean isWindows() {
        return getOSName().contains("windows");
    }

    /**
     * 返回系统版本
     *
     * @return 系统版本
     */
    public static String getOSVersion() {
        return System.getProperty("os.version");
    }

    private static class EnvironmentWrapper implements Environment {


        private Enum anEnum;

        public EnvironmentWrapper(Enum anEnum) {
            this.anEnum = anEnum;
        }

        @Override
        public String name() {
            return anEnum.name();
        }

        @Override
        public int ordinal() {
            return anEnum.ordinal();
        }

        /**
         * Is it a dev environment
         *
         * @return Dev
         */
        @Override
        public boolean isDev() {
            return "DEV".equalsIgnoreCase(name());
        }

        /**
         * Is it a qa environment
         *
         * @return For QA testing or E2E
         */
        @Override
        public boolean isQa() {
            return "QA".equalsIgnoreCase(name());
        }

        /**
         * Is it a production environment
         *
         * @return Production
         */
        @Override
        public boolean isProduction() {
            return "PRODUCTION".equalsIgnoreCase(name());
        }
    }

    /**
     * Environment
     */
    private static Environment environment = null;

    /**
     * Returns current Environment
     *
     * @return return current Environment
     */
    public static Environment getEnvironment() {
        if (environment == null) {
            String environmentClass = System.getProperty("femto.environment.class");
            if (environmentClass == null) {
                environmentClass = DefaultEnvironment.class.getName();
            }
            String env = System.getProperty("femto.environment");
            env = StringUtil.isInvalid(env) ? System.getenv("FEMTO_ENV") : env;
            env = StringUtil.isInvalid(env) ? "dev" : env;
            try {
                Class<?> clazz = Reflection.loadClass(environmentClass);
                if (!Enum.class.isAssignableFrom(clazz)) {
                    throw new IllegalArgumentException("The environment class has to be an Enum" + environmentClass);
                }
                Enum anEnum = Enum.valueOf((Class<? extends Enum>) clazz, env.toUpperCase());
                if (anEnum instanceof Environment) {
                    environment = (Environment) anEnum;
                } else {
                    environment = new EnvironmentWrapper(anEnum);
                }
            } catch (ClassNotFoundException cnfe) {
                throw new IllegalArgumentException("The environment class doesn't exist:" + environmentClass);
            }
        }
        return environment;
    }
}
