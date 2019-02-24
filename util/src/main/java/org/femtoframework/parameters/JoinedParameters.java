package org.femtoframework.parameters;

import java.util.*;

/**
 * Joined Parameters
 *
 * @author Sheldon Shao xshao@ebay.com on 12/13/16.
 * @version 1.0
 */
public class JoinedParameters<V> extends AbstractMap<String, V> implements Parameters<V> {

    private Parameters<V> params1 = null;
    private Parameters<V> params2 = null;

    /**
     * Constructor
     *
     * @param params1 Parameters 1
     * @param params2 Parameters 2
     */
    public JoinedParameters(Parameters params1, Parameters params2)
    {
        if (params1 == null || params2 == null) {
            throw new IllegalArgumentException("One of the parameters is null:" + params1 + " " + params2);
        }
        this.params1 = params1;
        this.params2 = params2;
    }

    /**
     * Return value by key
     *
     * @param key Key
     * @return Check the value in each parameters
     */
    public V get(Object key)
    {
        V value = params1.get(key);
        if (value == null) {
            value = params2.get(key);
        }
        return value;
    }

    /**
     * Putting value always go to the first one
     *
     * @param key Key
     * @param object Value
     * @return
     */
    public V put(String key, V object)
    {
        return params1.put(key, object);
    }

    /**
     * Joined the key set
     *
     * @return Key Set
     */
    public Set<String> keySet()
    {
        Set<String> names1 = params1.keySet();
        Set<String> names2 = params2.keySet();
        HashSet<String> newSet = new HashSet<>();
        newSet.addAll(names1);
        newSet.addAll(names2);
        return newSet;
    }

    /**
     * All values
     *
     * @return values
     */
    public Collection<V> values()
    {
        Collection<V> values1 = params1.values();
        Collection<V> values2 = params2.values();
        ArrayList<V> list = new ArrayList<>();
        list.addAll(values1);
        list.addAll(values2);
        return list;
    }

    @Override
    public Set<Entry<String, V>> entrySet() {
        Set<Entry<String, V>> names1 = params1.entrySet();
        Set<Entry<String, V>> names2 = params2.entrySet();
        HashSet<Entry<String, V>> newSet = new HashSet<>();
        newSet.addAll(names1);
        newSet.addAll(names2);
        return newSet;
    }

    /**
     * Remove
     *
     * @param key key
     * @return Value
     */
    public V remove(Object key)
    {
        V obj1 = params1.remove(key);
        V obj2 = params2.remove(key);
        return obj1 != null ? obj1 : obj2;
    }

    /**
     * Return value as Parameters
     *
     * @param key Key
     * @return value as Parameters, if the value is map, the map will wrapped as a Parameters
     */
    @Override
    public Parameters<V> getParameters(String key) {
        Parameters<V> obj1 = params1.getParameters(key);
        Parameters<V> obj2 = params2.getParameters(key);
        if (obj1 == null) {
            return obj2;
        }
        else if (obj2 == null) {
            return obj1;
        }
        else {
            return new JoinedParameters<V>(obj1, obj2);
        }
    }
}
