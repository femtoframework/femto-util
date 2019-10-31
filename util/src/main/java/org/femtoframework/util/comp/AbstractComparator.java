package org.femtoframework.util.comp;

import java.io.Serializable;
import java.util.Comparator;

/**
 * 抽象比较器
 *
 * @author fengyun
 * @version Jan 22, 2003 8:38:58 PM
 */
public abstract class AbstractComparator<T>
        implements Comparator<T>, Serializable
{
    /**
     * Implements the compare operator.
     * Compares its two arguments for order. Returns a negative integer, zero,
     * or a positive integer as the first argument is less than, equal to,
     * or greater than the second.
     *
     * @param obj1 object 1 to compare
     * @param obj2 object 2 ro compare to.
     * @return int returns -1, 0, 1 if the first argument is less than,
     *         equal to, or greater than the second.
     * @throws java.lang.ClassCastException if object types are unknown
     */
    public int compare(T obj1, T obj2)
    {
        int status = 0;
        if (obj1 == null && obj2 == null) {
            status = 0;
        }
        else if (obj1 == null) {
            status = -1;
        }
        else if (obj2 == null) {
            status = 1;
        }
        else {
            status = doCompare(obj1, obj2);
        }
        return status;
    }

    /**
     * 实际比较函数
     *
     * @param obj1 对象1
     * @param obj2 对象2
     */
    protected abstract int doCompare(T obj1, T obj2);
}
