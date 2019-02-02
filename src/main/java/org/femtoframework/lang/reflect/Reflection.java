package org.femtoframework.lang.reflect;

import org.femtoframework.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;


/**
 * Reflection Utilities
 *
 * @author fengyun
 */
public class Reflection
{
    /**
     * Empty class array
     */
    public static Class[] EMPTY_CLASS_ARRAY = new Class[0];

    /**
     * Array suffix
     */
    public static String ARRAY_SUFFIX = "[]";

    /**
     * Primitive classes for reflection of constructors.
     */
    private static HashMap<String, Class<?>> primitiveClasses;
    private static HashMap<String, Class<?>> preDefinedClasses;
    private static Set<String> arrayClasses = new HashSet<String>();

    static {
        primitiveClasses = new HashMap<>(64);
        primitiveClasses.put(Boolean.TYPE.toString(), Boolean.TYPE);
        primitiveClasses.put(Character.TYPE.toString(), Character.TYPE);
        primitiveClasses.put(Byte.TYPE.toString(), Byte.TYPE);
        primitiveClasses.put(Short.TYPE.toString(), Short.TYPE);
        primitiveClasses.put(Integer.TYPE.toString(), Integer.TYPE);
        primitiveClasses.put(Long.TYPE.toString(), Long.TYPE);
        primitiveClasses.put(Float.TYPE.toString(), Float.TYPE);
        primitiveClasses.put(Double.TYPE.toString(), Double.TYPE);

        primitiveClasses.put("[Z", boolean[].class);
        primitiveClasses.put("boolean[]", boolean[].class);
        primitiveClasses.put("booleans", boolean[].class);
        arrayClasses.add("booleans");

        primitiveClasses.put("[B", byte[].class);
        primitiveClasses.put("byte[]", byte[].class);
        primitiveClasses.put("bytes", byte[].class);
        arrayClasses.add("bytes");


        primitiveClasses.put("[C", char[].class);
        primitiveClasses.put("char[]", char[].class);
        primitiveClasses.put("chars", char[].class);
        arrayClasses.add("chars");

        primitiveClasses.put("[S", short[].class);
        primitiveClasses.put("short[]", short[].class);
        primitiveClasses.put("shorts", short[].class);
        arrayClasses.add("shorts");

        primitiveClasses.put("[I", int[].class);
        primitiveClasses.put("int[]", int[].class);
        primitiveClasses.put("ints", int[].class);

        primitiveClasses.put("[J", long[].class);
        primitiveClasses.put("long[]", long[].class);
        primitiveClasses.put("longs", long[].class);
        arrayClasses.add("longs");

        primitiveClasses.put("[F", float[].class);
        primitiveClasses.put("float[]", float[].class);
        primitiveClasses.put("floats", long[].class);
        arrayClasses.add("floats");

        primitiveClasses.put("[D", double[].class);
        primitiveClasses.put("double[]", double[].class);
        primitiveClasses.put("doubles", double[].class);
        arrayClasses.add("doubles");

        preDefinedClasses = new HashMap<>(64);
        preDefinedClasses.put("Object", Object.class);
        preDefinedClasses.put("Object[]", Object[].class);
        preDefinedClasses.put("Objects", Object[].class);
        preDefinedClasses.put("objects", Object[].class);
        arrayClasses.add("objects");

        preDefinedClasses.put("String", String.class);
        preDefinedClasses.put("String[]", String[].class);
        preDefinedClasses.put("Strings", String[].class);
        preDefinedClasses.put("strings", String[].class);
        arrayClasses.add("strings");

        preDefinedClasses.put("void", Void.TYPE);

        preDefinedClasses.putAll(primitiveClasses);
    }

    /**
     * Check whether the class is an array class or not
     *
     * @param className Class Name
     */
    public static boolean isArrayClass(String className)
    {
        if (className == null) {
            throw new IllegalArgumentException("Null class name");
        }
        return className.endsWith(ARRAY_SUFFIX)
               || className.startsWith("[L")
               || className.endsWith("[]")
                || arrayClasses.contains(className);
    }

    /**
     * Load class from ThreadContextClassLoader
     *
     * @param className Class Name
     * @throws ClassNotFoundException If the class does not exist
     */
    public static Class<?> loadClass(String className) throws ClassNotFoundException
    {
        ClassLoader loader = getClassLoader();
        return loader != null ? loadClass(loader, className) : Class.forName(className);
    }

    /**
     * Uses current thread class loader if it exists, otherwise uses the system class loader
     */
    public static ClassLoader getClassLoader()
    {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = ClassLoader.getSystemClassLoader();
        }
        return loader;
    }

    /**
     * Switch ThreadContextClassLoader
     *
     * @param classLoader New class loader
     * @return the old ClassLoader in the Thread context
     */
    public static ClassLoader switchClassLoader(ClassLoader classLoader)
    {
        Thread thread = Thread.currentThread();
        ClassLoader oldLoader = thread.getContextClassLoader();
        thread.setContextClassLoader(classLoader);
        return oldLoader;
    }

    /**
     * Load class with given class loader
     *
     * @param className Class Name
     * @throws ClassNotFoundException Class Not Found Exception
     */
    public static Class loadClass(ClassLoader loader, String className)
        throws ClassNotFoundException
    {
        return loader != null ? loader.loadClass(className) : loadClass(className);
    }

    public static URL getResource(String resourceName)
    {
        return getResource(null, resourceName);
    }

    public static URL getResource(ClassLoader loader, String resourceName)
    {
        if (resourceName.startsWith("/")) {
            resourceName = resourceName.substring(1);
        }
        if (loader == null) {
            loader = getClassLoader();
        }
        return loader.getResource(resourceName);
    }

    public static Enumeration<URL> getResources(String resourceName)
        throws IOException
    {
        return getResources(null, resourceName);
    }

    public static Enumeration<URL> getResources(ClassLoader loader, String resourceName)
        throws IOException
    {
        if (resourceName.startsWith("/")) {
            resourceName = resourceName.substring(1);
        }
        if (loader == null) {
            loader = getClassLoader();
        }
        return loader.getResources(resourceName);
    }

    public static InputStream getResourceAsStream(String resourceName)
    {
        return getResourceAsStream(null, resourceName);
    }

    public static InputStream getResourceAsStream(ClassLoader loader, String resourceName)
    {
        if (resourceName.startsWith("/")) {
            resourceName = resourceName.substring(1);
        }

        if (loader == null) {
            loader = getClassLoader();
        }

        return loader.getResourceAsStream(resourceName);
    }

    /**
     * Gets an instance of a specified class.
     *
     * @param className the name of class.
     *                  the instance.
     * @throws ReflectionException if instantiation fails.
     */
    public static Object newInstance(String className)
        throws ReflectionException
    {
        Class<?> clazz = internalLoadClass(className);
        return newInstance(clazz);
    }

    /**
     * Gets an instance of a specified class.
     *
     * @param clazz the class.
     *              the instance.
     * @throws ReflectionException if instantiation fails.
     */
    public static <T> T newInstance(Class<T> clazz)
        throws ReflectionException
    {
        try {
            return clazz.newInstance();
        }
        catch (Exception x) {
            throw new ObjectCreationException("Instantiation failed for " + clazz.getName(), x);
        }
    }

    /**
     * Gets an instance of a specified class.
     * Parameters for its constructor are given as an array of objects,
     * primitive types must be wrapped with a corresponding class.
     *
     * @param clazz   the class.
     * @param params  an array containing the parameters of the constructor.
     * @param classes an array containing the parameters class of the constructor.
     *                the instance.
     * @throws ReflectionException if instantiation fails.
     */
    public static <T> T newInstance(Class<T> clazz, Object params[], Class<?> classes[])
        throws ReflectionException
    {
        try {
            return clazz.getConstructor(classes).newInstance(params);
        }
        catch (Exception x) {
            throw new ObjectCreationException("Instantiation failed for " + clazz.getName(), x);
        }
    }


    /**
     * Gets an instance of a specified class.
     * Parameters for its constructor are given as an array of objects,
     * primitive types must be wrapped with a corresponding class.
     *
     * @param className the name of class.
     * @param params    an array containing the parameters of the constructor.
     * @param classes   an array containing the parameters class of the constructor.
     *                  the instance.
     * @throws ReflectionException if instantiation fails.
     */
    public static Object newInstance(String className,
                                           Object params[],
                                           Class classes[])
        throws ReflectionException
    {
        Class clazz = internalLoadClass(className);
        return newInstance(clazz, params, classes);
    }

    /**
     * Gets an instance of a specified class.
     * Parameters for its constructor are given as an array of objects,
     * primitive types must be wrapped with a corresponding class.
     *
     * @param clazz     the class.
     * @param params    an array containing the parameters of the constructor.
     * @param signature an array containing the signature of the constructor.
     *                  the instance.
     * @throws ReflectionException if instantiation fails.
     */
    public static <T> T newInstance(Class<T> clazz, Object params[], String signature[])
        throws ReflectionException
    {
        /* Try to construct. */
        try {
            Class[] sign = getSignature(clazz, params, signature);
            return newInstance(clazz, params, sign);
        }
        catch (ReflectionException re) {
            throw re;
        }
        catch (Exception x) {
            throw new ObjectCreationException("Instantiation failed for " + clazz.getName(), x);
        }
    }

    /**
     * Gets an instance of a specified class.
     * Parameters for its constructor are given as an array of objects,
     * primitive types must be wrapped with a corresponding class.
     *
     * @param className the name of class.
     * @param params    an array containing the parameters of the constructor.
     * @param signature an array containing the signature of the constructor.
     *                  the instance.
     * @throws ReflectionException if instantiation fails.
     */
    public static Object newInstance(String className,
                                           Object params[],
                                           String signature[])
        throws ReflectionException
    {
        Class clazz = internalLoadClass(className);
        return newInstance(clazz, params, signature);
    }

    /**
     * Internal class loading
     *
     * @param className
     * @throws ReflectionException
     */
    static Class<?> internalLoadClass(String className)
        throws ReflectionException
    {
        try {
            return loadClass(className);
        }
        catch (Exception ex) {
            throw new ObjectCreationException("Loading class error:" + className, ex);
        }
    }

    /**
     * Gets the class of a primitive type.
     *
     * @param type a primitive type.
     *             the corresponding class, or null.
     */
    public static Class getPrimitiveClass(String type)
    {
        return primitiveClasses.get(type);
    }

    /**
     * Check whether the class is a primitive class
     *
     * @param className
     */
    public static boolean isPrimitiveClass(String className)
    {
        return primitiveClasses.containsKey(className);
    }

//    /**
//     * 判断是否属于基本数据类型
//     *
//     * @param clazz
//     */
//    public static boolean isPrimitiveClass(Class clazz)
//    {
//        return primitiveClasses.containsValue(clazz);
//    }

    /**
     * check for primitive and widening.  Take from the 1.4 code
     */
    public static boolean checkPrimitive(Class formal, Class arg)
    {

        if (formal.isPrimitive()) {
            if (formal == Boolean.TYPE && arg == Boolean.class) {
                return true;
            }

            if (formal == Character.TYPE && arg == Character.class) {
                return true;
            }

            if (formal == Byte.TYPE && arg == Byte.class) {
                return true;
            }

            if (formal == Short.TYPE &&
                (arg == Short.class || arg == Byte.class)) {
                return true;
            }

            if (formal == Integer.TYPE &&
                (arg == Integer.class || arg == Short.class ||
                 arg == Byte.class)) {
                return true;
            }

            if (formal == Long.TYPE &&
                (arg == Long.class || arg == Integer.class ||
                 arg == Short.class || arg == Byte.class)) {
                return true;
            }

            if (formal == Float.TYPE &&
                (arg == Float.class || arg == Long.class ||
                 arg == Integer.class || arg == Short.class ||
                 arg == Byte.class)) {
                return true;
            }

            if (formal == Double.TYPE &&
                (arg == Double.class || arg == Float.class ||
                 arg == Long.class || arg == Integer.class ||
                 arg == Short.class || arg == Byte.class)) {
                return true;
            }
        }

        return false;
    }


    /**
     * Return class by name, leverage cache and naming conversions,
     * It has primitive class and alias also
     *
     * @param className Could be primitive class, such as "int", "byte"
     */
    public static Class getClass(String className) throws ClassNotFoundException
    {
        return getClass(className, getClassLoader());
    }

    /**
     *  Return class by name, leverage cache and naming conversions,
     *  It has primitive class and alias also
     *
     * @param className Could be primitive class, such as "int", "byte"
     */
    public static Class getClass(String className, ClassLoader loader)
        throws ClassNotFoundException
    {
        if (className == null) {
            throw new IllegalArgumentException("Null class name");
        }
        if (loader == null) {
            loader = getClassLoader();
        }
        Class clazz = preDefinedClasses.get(className);
        if (clazz != null) {
            return clazz;
        }
        else if (className.endsWith(ARRAY_SUFFIX)) {
            // special handling for array class names
            int len = className.length() + 1;
            StringBuilder sb = new StringBuilder(len + 1);
            sb.append("[L").append(className);
            sb.setCharAt(sb.length() - 2, ';');
            sb.setLength(len);
            return Class.forName(sb.toString(), true, loader);
        }
        else {
            clazz = Class.forName(className, true, loader);
        }
        return clazz;
    }

    /**
     * Returns signatures by given arguments
     *
     * @param params arguments
     */
    public static Class[] getSignature(Object... params)
    {
        if (params == null) {
            return null;
        }
        else if (params.length == 0) {
            return NO_SUCH_CLASS;
        }
        Class[] classes = new Class[params.length];
        for (int i = 0; i < classes.length; i++) {
            classes[i] = params[i].getClass();
        }
        return classes;
    }

    /**
     * @param clazz
     * @param signature
     * @throws ReflectionException
     */
    public static Class[] getSignature(Class clazz, String... signature)
        throws ReflectionException
    {
        Class[] classes = null;
        try {
            classes = getSignature(clazz, null, signature);
        }
        catch (ClassNotFoundException e) {
            throw new NoSuchClassException("Signature class not found", e);
        }
        return classes;
    }

    /**
     * Gets the signature classes for parameters of a method of a class.
     *
     * @param clazz     the class.
     * @param params    an array containing the parameters of the method.
     * @param signature an array containing the signature of the method.
     *                  an array of signature classes. Note that in some cases
     *                  objects in the parameter array can be switched to the context
     *                  of a different class loader.
     * @throws ClassNotFoundException if any of the classes is not found.
     */
    public static Class[] getSignature(Class clazz,
                                             Object[] params,
                                             String... signature)
        throws ClassNotFoundException
    {
        if (signature != null) {
            ClassLoader tempLoader;
            ClassLoader loader = clazz.getClassLoader();
            Class[] sign = new Class[signature.length];
            for (int i = 0; i < signature.length; i++) {
                /* Check primitive types. */
                sign[i] = getPrimitiveClass(signature[i]);
                if (sign[i] == null) {
                    /* Not a primitive one, continue building. */
                    if (loader != null) {
                        /* Use the class loader of the target object. */
                        sign[i] = loader.loadClass(signature[i]);
                        tempLoader = sign[i].getClassLoader();
                        if (params != null && params[i] != null && (tempLoader != null)
                            && !tempLoader.equals(params[i].getClass().getClassLoader())) {
                            params[i] = switchObject(params[i], loader);
                        }
                    }
                    else {
                        /* Use the default class loader. */
                        sign[i] = loadClass(signature[i]);
                    }
                }
            }
            return sign;
        }
        else {
            return null;
        }
    }

    /**
     * Switches an object into the context of a different class loader.
     *
     * @param object an object to switch.
     * @param loader the loader of the new context.
     */
    public static Object switchObject(Object object, ClassLoader loader)
    {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ObjectOutputStream out = new ObjectOutputStream(baos);
            out.writeObject(object);
            out.flush();

            ReflectionInputStream in = new ReflectionInputStream(baos.getInputStream(), loader);
            Object result = in.readObject();
            return result;
        }
        catch (Exception x) {
            return object;
        }
    }

    /**
     * Return method by name
     *
     * @param clazz      Class
     * @param methodName Method name
     * @throws ReflectionException
     */
    public static Method getMethod(Class<?> clazz, String methodName)
        throws ReflectionException
    {
        try {
            return clazz.getMethod(methodName);
        }
        catch (Exception e) {
            throw new NoSuchMethodException("Get method failed for:" + clazz.getName()
                                            + "#" + methodName, e);
        }
    }

    /**
     * Return methodsby name
     *
     * @param clazz      class
     * @param methodName Method Name
     */
    public static Method[] getMethods(Class clazz, String methodName)
    {
        Method[] methods = clazz.getMethods();
        List<Method> list = new ArrayList<Method>();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                list.add(method);
            }
        }

        if (list.size() > 0) {
            methods = list.toArray(new Method[list.size()]);
        }
        else {
            methods = NO_SUCH_METHOD;
        }
        return methods;
    }

    public static Method[] NO_SUCH_METHOD = new Method[0];
    public static Class[] NO_SUCH_CLASS = EMPTY_CLASS_ARRAY;

    /**
     * Return method by name and signatures
     *
     * @param clazz      Class
     * @param methodName Method Name
     * @param signatures  Signatures
     * @throws ReflectionException
     */
    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... signatures)
        throws ReflectionException
    {
        try {
            return clazz.getMethod(methodName, signatures);
        }
        catch (Exception e) {
            throw new NoSuchMethodException("Get method failed for:" + clazz.getName()
                                            + "#" + methodName, e);
        }
    }

    /**
     * Return method by name and signatures
     *
     * @param clazz      Class
     * @param methodName Method Name
     * @param signatures  Signatures
     * @throws ReflectionException
     */
    public static Method getMethod(Class clazz, String methodName, String... signatures)
        throws ReflectionException
    {
        return getMethod(clazz, methodName, signatures, null);
    }

    /**
     * Return method by name and signatures
     *
     * @param clazz      Class
     * @param methodName Method Name
     * @param signatures  Signatures
     * @param params Arguments
     * @throws ReflectionException
     */
    private static Method getMethod(Class clazz,
                                    String methodName,
                                    String[] signatures,
                                    Object... params)
        throws ReflectionException
    {
        Class[] classes = null;
        try {
            classes = getSignature(clazz, params, signatures);
        }
        catch (Exception e) {
            throw new NoSuchClassException("Get method failed for:" + clazz.getName()
                                           + "#" + methodName, e);
        }
        return getMethod(clazz, methodName, classes);
    }

    /**
     * Return declared method by name and signatures
     *
     * @param clazz      Class
     * @param methodName Method Name
     * @param signatures  Signatures
     * @throws ReflectionException
     */
    public static Method getDeclaredMethod(Class clazz,
                                                 String methodName,
                                                 Class... signatures)
        throws ReflectionException
    {
        try {
            return clazz.getDeclaredMethod(methodName, signatures);
        }
        catch (Exception e) {
            throw new NoSuchMethodException("Get declared method failed for:" + clazz.getName()
                                            + "#" + methodName, e);
        }
    }

    /**
     * Return declared method by name and signatures
     *
     * @param clazz      Class
     * @param methodName Method Name
     * @param signatures  Signatures
     * @throws ReflectionException
     */
    public static Method getDeclaredMethod(Class clazz,
                                                 String methodName,
                                                 String... signatures)
        throws ReflectionException
    {
        return getDeclaredMethod(clazz, methodName, signatures, null);
    }

    /**
     * Return declared method by name and signatures
     *
     * @param clazz      Class
     * @param methodName Method Name
     * @param signatures  Signatures
     * @throws ReflectionException
     */
    private static Method getDeclaredMethod(Class clazz,
                                            String methodName,
                                            String[] signatures,
                                            Object... params)
        throws ReflectionException
    {
        Class[] classes = null;
        try {
            classes = getSignature(clazz, params, signatures);
        }
        catch (Exception e) {
            throw new NoSuchClassException("Get method failed for:" + clazz.getName()
                                           + "#" + methodName, e);
        }
        return getDeclaredMethod(clazz, methodName, classes);
    }

    /**
     * Invoke method
     *
     * @param obj    Current object
     * @param method Method
     * @throws ReflectionException
     */
    public static Object invoke(Object obj, Method method)
        throws ReflectionException
    {
        return invoke(obj, method, null);
    }

    /**
     * Invoke method
     *
     * @param obj    Current object
     * @param method Method
     * @param params Arguments
     * @throws ReflectionException
     */
    public static Object invoke(Object obj, Method method, Object... params)
        throws ReflectionException
    {
        try {
            return method.invoke(obj, params);
        }
        catch (Exception e) {
            throw new MethodInvocationException("Invoke method failed for:" + obj.getClass().getName()
                                                + "#" + method.getName(), e);
        }
    }

    /**
     * Invoke method
     *
     * @param obj    Current object
     * @param methodName Method Name
     * @throws ReflectionException
     */
    public static Object invoke(Object obj, String methodName)
        throws ReflectionException
    {
        Class clazz = obj.getClass();
        Method method = getMethod(clazz, methodName);
        return invoke(obj, method);
    }

    /**
     * Invoke method
     *
     * @param obj    Current object
     * @param signatures  Signatures
     * @param methodName Method Name
     * @throws ReflectionException
     */
    public static Object invoke(Object obj, String methodName, Class[] signatures, Object... params)
        throws ReflectionException
    {
        Class clazz = obj.getClass();
        Method method = getMethod(clazz, methodName, signatures);
        return invoke(obj, method, params);
    }

    /**
     * Invoke method
     *
     * @param obj    Current object
     * @param signatures  Signatures
     * @param methodName Method Name
     * @throws ReflectionException
     */
    public static Object invoke(Object obj, String methodName, String[] signatures, Object... params)
        throws ReflectionException
    {
        Class clazz = obj.getClass();
        Method method = getMethod(clazz, methodName, signatures, params);
        return invoke(obj, method, params);
    }
}
