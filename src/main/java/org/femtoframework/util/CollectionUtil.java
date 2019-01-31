package org.femtoframework.util;

import java.util.*;

/**
 * Collection Util which Collections doesn't have
 */
public interface CollectionUtil {
    Properties EMPTY_PROPERTIES = new EmptyProperties();

    class EmptyProperties extends Properties
    {
        public int size()
        {
            return 0;
        }

        public boolean isEmpty()
        {
            return true;
        }

        public boolean containsKey(String key)
        {
            return false;
        }

        public boolean containsValue(Object value)
        {
            return false;
        }

        public Object get(String key)
        {
            return null;
        }

        public Object put(String key, Object value)
        {
            return null;
        }

        public Object remove(String key)
        {
            return null;
        }

        public Enumeration<Object> keys()
        {
            return Collections.emptyEnumeration();
        }

        public Set<Object> keySet()
        {
            return Collections.emptySet();
        }

        public Collection<Object> values()
        {
            return Collections.emptySet();
        }

        public Set<Map.Entry<Object, Object>> entrySet()
        {
            return Collections.emptySet();
        }

        public boolean equals(Object o)
        {
            return (o instanceof Properties) && ((Properties)o).size() == 0;
        }

        public int hashCode()
        {
            return 0;
        }

        public void clear()
        {
        }
    }

    /**
     * Convert array to Set
     *
     * @param array Array
     * @param <T> Type
     * @return Set
     */
    static <T> Set<T> asSet(T[] array) {
        if (array == null) {
            return Collections.emptySet();
        }
        HashSet<T> set = new HashSet<>(array.length);
        Collections.addAll(set, array);
        return set;
    }
}
