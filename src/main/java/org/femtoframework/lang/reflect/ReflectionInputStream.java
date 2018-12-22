package org.femtoframework.lang.reflect;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/**
 * Uses customized class loader for serialization
 *
 * @author fengyun
 * @version Dec 24, 2009 5:15:44 PM
 */
public class ReflectionInputStream extends ObjectInputStream
{
    /**
     * The class loader of the context.
     */
    private ClassLoader loader;

    /**
     * Creates a new object stream for a context.
     *
     * @param in     the serialized input stream.
     * @param loader the class loader of the context.
     * @throws java.io.IOException on errors.
     */
    public ReflectionInputStream(InputStream in, ClassLoader loader)
        throws IOException
    {
        super(in);
        this.loader = loader;
    }

    protected Class resolveClass(ObjectStreamClass v)
        throws IOException,
        ClassNotFoundException
    {
        return loader == null ? super.resolveClass(v) : loader.loadClass(v.getName());
    }
}
