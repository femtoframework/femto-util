package org.femtoframework.pattern.ext;

import org.femtoframework.bean.NamedBean;

import org.femtoframework.bean.annotation.Coined;
import org.femtoframework.bean.annotation.Ignore;
import org.femtoframework.bean.annotation.Property;
import org.femtoframework.pattern.Factory;

import java.util.*;

/**
 * Base Factory
 *
 * @author Sheldon Shao
 * @version 1.0
 */
@Coined
public class BaseFactory<B> implements Factory<B> {

    @Ignore
    protected Map<String, B> map = new LinkedHashMap<>();

    protected void add(String name, B object) {
        map.put(name, object);
    }

    protected void add(NamedBean bean) {
        add(bean.getName(), (B)bean);
    }


    protected BaseFactory() {
    }

    /**
     * Return all names
     *
     * @return all names
     */
    @Override
    @Property(writable = false)
    public Set<String> getNames() {
        return map.keySet();
    }

    /**
     * Return object by given name
     *
     * @param name Name
     * @return object in this factory
     */
    @Override
    public B get(String name) {
        return map.get(name);
    }

    /**
     * Delete the object by given name
     *
     * @param name Name
     * @return Deleted object
     */
    @Override
    public B delete(String name) {
        return map.remove(name);
    }

    /**
     * Returns an iterator over elements of kind {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<B> iterator() {
        return map.values().iterator();
    }

    /**
     * Return all objects
     *
     * @return Objects
     */
    @Property(writable = false)
    public Collection<B> getObjects() {
        return map.values();
    }

}
