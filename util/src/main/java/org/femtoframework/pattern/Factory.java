package org.femtoframework.pattern;

import java.util.Set;

/**
 * Factory interface
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface Factory<B> extends Iterable<B> {

    /**
     * Return all names
     *
     * @return all names
     */
    Set<String> getNames();

    /**
     * Return object by given name
     *
     * @param name Name
     * @return object in this factory
     */
    B get(String name);

    /**
     * Delete the object by given name
     *
     * @param name Name
     * @return Deleted object
     */
    B delete(String name);
}
