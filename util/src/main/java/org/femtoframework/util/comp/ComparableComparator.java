package org.femtoframework.util.comp;

/**
 * A Comparator that compares Comparable objects.
 * Throws ClassCastExceptions if the objects are not
 * Comparable, or if either is null.
 * <p/>
 * Throws ClassCastException if the compareTo of both
 * objects do not provide an inverse result of each other
 * as per the Comparable javadoc.  This Comparator is useful, for example,
 * for enforcing the natural order in custom implementations
 * of SortedSet and SortedMap.
 *
 * @author bayard@generationjava.com
 * @since Commons Collections 2.0
 */
public class ComparableComparator<T extends Comparable>
        extends AbstractComparator<T>
{
    private static ComparableComparator instance =
            new ComparableComparator();

    /**
     * Return a shared instance of a ComparableComparator.  Developers are
     * encouraged to use the comparator returned from this method instead of
     * constructing a new instance to reduce allocation and GC overhead when
     * multiple comparable comparators may be used in the same VM.
     */
    public static <T extends Comparable> ComparableComparator<T> getInstance()
    {
        return (ComparableComparator<T>)instance;
    }

    private Object readResolve()
    {
        return instance;
    }

    /**
     * 实际比较函数
     *
     * @param obj1 对象1
     * @param obj2 对象2
     */
    protected int doCompare(T obj1, T obj2)
    {
//        if (obj1 instanceof Comparable) {
//            if (obj2 instanceof Comparable) {
        int result1 = obj1.compareTo(obj2);
        int result2 = obj2.compareTo(obj1);

        // enforce comparable contract
        if (result1 == 0 && result2 == 0) {
            return 0;
        }
        else if (result1 < 0 && result2 > 0) {
            return result1;
        }
        else if (result1 > 0 && result2 < 0) {
            return result1;
        }
        else {
            // results inconsistent
            throw new ClassCastException("obj1 not comparable to obj2");
        }
//            }
//            else {
//                // obj2 wasn't comparable
//                throw new ClassCastException("The first argument of this method was not a Comparable: " +
//                                             obj2.getClass().getName());
//            }
//        }
//        else if (obj2 instanceof Comparable) {
//            // obj1 wasn't comparable
//            throw new ClassCastException("The second argument of this method was not a Comparable: " +
//                                         obj1.getClass().getName());
//        }
//        else {
//            // neither were comparable
//            throw new ClassCastException("Both arguments of this method were not Comparables: " +
//                                         obj1.getClass().getName() + " and " + obj2.getClass().getName());
//        }
    }
}

