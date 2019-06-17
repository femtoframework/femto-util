package org.femtoframework.util;

/**
 * Object Util
 */
public interface ObjectUtil {

    /**
     * Empty
     */
    Object[] EMTPY_ARRAY = new Object[0];

    /**
     * Returns a default value if the object passed is null.
     *
     * @param object       the object to test
     * @param defaultValue the default value to return
     * @return object if it is not null, defaultValue otherwise
     */
    static Object defaultIfNull(Object object, Object defaultValue) {
        return (object != null ? object : defaultValue);
    }

    /**
     * Compares two objects for equality, where either one or both
     * objects may be <code>null</code>.
     *
     * @param object1 the first object
     * @param object2 the second object
     * @return <code>true</code> if the values of both objects are the same
     */
    static boolean equals(Object object1, Object object2) {
        return object1 == object2 || !((object1 == null) || (object2 == null)) && object1.equals(object2);
    }

    /**
     * 取对象的hashCode
     *
     * @param obj
     * @return 对象的hashCode
     */
    static int hashCode(Object obj) {
        if (obj == null) {
            return 0;
        }
        return obj.hashCode();
    }

    /**
     * Gets the toString that would be produced by Object if a class did not
     * override toString itself. Null will return null.
     *
     * @param object the object to create a toString for, may be null
     * @return the default toString text, or null if null passed in
     */
    static String identityToString(Object object) {
        if (object == null) {
            return null;
        }
        return object.getClass().getName() +
                '@' + Integer.toHexString(System.identityHashCode(object));
    }
}
