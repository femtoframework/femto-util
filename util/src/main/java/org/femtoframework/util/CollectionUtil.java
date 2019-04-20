package org.femtoframework.util;

import org.femtoframework.parameters.Parameters;
import org.femtoframework.parameters.ParametersMap;

import java.util.*;

import static java.util.Collections.emptyIterator;

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

    Parameters EMPTY_PARAMETERS = new ParametersMap(Collections.emptyMap());

    /**
     * Return Empty Parameters
     *
     * @param <V>
     * @return Empty Parameters
     */
    static <V> Parameters<V> emptyParameters() {
        return (Parameters<V>)EMPTY_PARAMETERS;
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

    /**
     * 判断数组时候有效
     *
     * @param coll
     */
    static boolean isValid(Collection coll)
    {
        return coll != null && !coll.isEmpty();
    }

    /**
     * 判断数组时候无效
     *
     * @param coll
     */
    static boolean isInvalid(Collection coll)
    {
        return coll == null || coll.isEmpty();
    }

    /**
     * 判断数组时候有效
     *
     * @param map
     */
    static boolean isValid(Map map)
    {
        return map != null && !map.isEmpty();
    }

    /**
     * 判断数组时候无效
     *
     * @param map
     */
    static boolean isInvalid(Map map)
    {
        return map == null || map.isEmpty();
    }

    /**
     * 如果map为<code>null</code>，返回<code>EMPTY_ITERATOR</code>
     *
     * @param map
     */
    static <K> Iterator<K> keys(Map<K, ?> map)
    {
        if (isValid(map)) {
            return map.keySet().iterator();
        }
        else {
            return emptyIterator();
        }
    }

    /**
     * 如果map为<code>null</code>，返回<code>EMPTY_ITERATOR</code>
     *
     * @param map
     */
    static <K> Set<K> keySet(Map<K, ?> map)
    {
        if (isValid(map)) {
            return map.keySet();
        }
        else {
            return Collections.emptySet();
        }
    }

    /**
     * 如果map为<code>null</code>，返回<code>EMPTY_ITERATOR</code>
     *
     * @param map
     */
    static <V> Iterator<V> values(Map<?, V> map)
    {
        if (isValid(map)) {
            return map.values().iterator();
        }
        else {
            return emptyIterator();
        }
    }

    /**
     * 如果map为<code>null</code>，返回<code>EMPTY_ITERATOR</code>
     *
     * @param map
     */
    static <V> Collection<V> valueSet(Map<?, V> map)
    {
        if (isValid(map)) {
            return map.values();
        }
        else {
            return Collections.emptySet();
        }
    }

    /**
     * 如果map为<code>null</code>，返回<code>EMPTY_ITERATOR</code>
     *
     * @param map
     */
    static <K, V> Set<Map.Entry<K, V>> entrySet(Map<K, V> map)
    {
        if (isValid(map)) {
            return map.entrySet();
        }
        else {
            return Collections.emptySet();
        }
    }
}
