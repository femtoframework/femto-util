package org.femtoframework.util;


import java.io.Serializable;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;


public class SortedList<V> extends AbstractList<V>
        implements Cloneable, Serializable, SortedCollection<V>
{
    protected transient V data[];

    protected int size;

    protected Comparator<V> comparator;

    private SortedList()
    {
    }

    /**
     * Constructs an empty vector with the specified initial capacity and
     * with its capacity increment equal to zero.
     *
     * @param capacity the initial capacity of the vector.
     * @throws java.lang.IllegalArgumentException
     *          if the specified initial capacity
     *          is negative
     */
    public SortedList(int capacity, Comparator<V> comparator)
    {
        if (capacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " +
                    capacity);
        }
        this.data = (V[])new Object[capacity];
        this.comparator = comparator;
    }

    /**
     * Constructs an empty vector so that its internal data array
     * has size <tt>10</tt> and its standard capacity increment is
     * zero.
     */
    public SortedList(Comparator<V> comparator)
    {
        this(10, comparator);
    }

    public boolean add(V obj)
    {
        modCount++;
        ensureCapacityHelper(size + 1);
        add0(obj);
        return true;
    }

    protected void add0(V obj)
    {
        int i = binarySearch(obj);
        if (i < 0) {
            i = -i - 1;
            int s = size - i;
            System.arraycopy(data, i, data, i + 1, s);
            size++;
        }
        data[i] = obj;
    }

    public int indexOf(Object obj)
    {
        if (obj == null) {
            return -1;
        }
        int i = binarySearch((V)obj);
        return i >= 0 ? i : -1;
    }

    private int binarySearch(V key)
    {
        return binarySearch(0, size - 1, key);
    }

    private int binarySearch(int low, int high, V key)
    {
        V[] array = data;

        while (low <= high) {
            int mid = (low + high) / 2;
            V midVal = array[mid];
            int cmp = comparator.compare(midVal, key);

            if (cmp < 0) {
                low = mid + 1;
            }
            else if (cmp > 0) {
                high = mid - 1;
            }
            else {
                if (key.equals(midVal)) {
                    return mid; // key found
                }
                else {
                    int i = binarySearch(low, mid - 1, key);
                    if (i >= 0) {
                        return i;
                    }
                    i = binarySearch(mid + 1, high, key);
                    return i;
                }
            }
        }
        return -(low + 1);  // key not found.
    }

    public Object first()
    {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return data[0];
    }

    public Object last()
    {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return data[size - 1];
    }

    public int size()
    {
        return size;
    }

    public boolean isEmpty()
    {
        return size == 0;
    }

    public V remove(int index)
    {
        modCount++;
        if (index >= size) {
            throw new ArrayIndexOutOfBoundsException(index + " >= " +
                    size);
        }
        else if (index < 0) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        V old = data[index];
        int j = size - index - 1;
        if (j > 0) {
            System.arraycopy(data, index + 1, data, index, j);
        }
        data[--size] = null; /* to let gc do its work */
        return old;
    }

    public boolean remove(Object obj)
    {
        modCount++;
        int i = indexOf(obj);
        if (i >= 0) {
            return remove(i) != null;
        }

        return false;
    }

    /**
     * Increases the capacity of this vector, if necessary, to ensure
     * that it can hold at least the number of components specified by
     * the minimum capacity argument.
     * <p/>
     * <p>If the current capacity of this vector is less than
     * <tt>minCapacity</tt>, then its capacity is increased by replacing its
     * internal data array, kept in the field <tt>elementData</tt>, with a
     * larger one.  The size of the new data array will be the old size plus
     * <tt>capacityIncrement</tt>, unless the value of
     * <tt>capacityIncrement</tt> is less than or equal to zero, in which case
     * the new capacity will be twice the old capacity; but if this new size
     * is still smaller than <tt>minCapacity</tt>, then the new capacity will
     * be <tt>minCapacity</tt>.
     *
     * @param minCapacity the desired minimum capacity.
     */
    public synchronized void ensureCapacity(int minCapacity)
    {
        modCount++;
        ensureCapacityHelper(minCapacity);
    }

    /**
     * This implements the unsynchronized semantics of ensureCapacity.
     * Synchronized methods in this class can internally call this
     * method for ensuring capacity without incurring the cost of an
     * extra synchronization.
     *
     * @see java.util.Vector#ensureCapacity(int)
     */
    private void ensureCapacityHelper(int minCapacity)
    {
        int oldCapacity = data.length;
        if (minCapacity > oldCapacity) {
            Object oldData[] = data;
            int newCapacity = oldCapacity * 2;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            data = (V[])new Object[newCapacity];
            System.arraycopy(oldData, 0, data, 0, size);
        }
    }

    /**
     * Returns the current capacity of this vector.
     *
     * @return the current capacity (the length of its internal
     *         data arary, kept in the field <tt>elementData</tt>
     *         of this vector.
     */
    public int capacity()
    {
        return data.length;
    }

    /**
     * Tests if the specified object is a component in this vector.
     *
     * @param elem an object.
     * @return <code>true</code> if and only if the specified object
     *         is the same as a component in this vector, as determined by the
     *         <tt>equals</tt> method; <code>false</code> otherwise.
     */
    public boolean contains(Object elem)
    {
        return indexOf(elem) >= 0;
    }

    /**
     * Returns a clone of this vector. The copy will contain a
     * reference to a clone of the internal data array, not a reference
     * to the original internal data array of this <tt>Vector</tt> object.
     *
     * @return a clone of this vector.
     */
    public Object clone()
    {
        try {
            SortedList<V> v = (SortedList<V>)super.clone();
            v.data = (V[])new Object[size];
            System.arraycopy(data, 0, v.data, 0, size);
            v.modCount = 0;
            v.comparator = comparator;
            v.size = size;
            return v;
        }
        catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

    /**
     * Returns an array containing all of the elements in this Vector
     * in the correct order.
     *
     * @since 1.2
     */
    public Object[] toArray()
    {
        Object[] result = new Object[size];
        System.arraycopy(data, 0, result, 0, size);
        return result;
    }

    /**
     * Returns an array containing all of the elements in this Vector in the
     * correct order.  The runtime type of the returned array is that of the
     * specified array.  If the Vector fits in the specified array, it is
     * returned therein.  Otherwise, a new array is allocated with the runtime
     * type of the specified array and the size of this Vector.<p>
     * <p/>
     * If the Vector fits in the specified array with room to spare
     * (i.e., the array has more elements than the Vector),
     * the element in the array immediately following the end of the
     * Vector is set to null.  This is useful in determining the length
     * of the Vector <em>only</em> if the caller knows that the Vector
     * does not contain any null elements.
     *
     * @param a the array into which the elements of the Vector are to
     *          be stored, if it is big enough; otherwise, a new array of the
     *          same runtime type is allocated for this purpose.
     * @return an array containing the elements of the Vector.
     * @throws java.lang.ArrayStoreException the runtime type of a is not a supertype
     *                                       of the runtime type of every element in this Vector.
     */
    public <V> V[] toArray(V a[])
    {
        if (a.length < size) {
            a = (V[])java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }

        System.arraycopy(data, 0, a, 0, size);

        if (a.length > size) {
            a[size] = null;
        }

        return a;
    }

    // Positional Access Operations

    /**
     * Returns the element at the specified position in this Vector.
     *
     * @param index index of element to return.
     * @throws java.lang.ArrayIndexOutOfBoundsException
     *          index is out of range (index
     *          &lt; 0 || index &gt;= size()).
     * @since 1.2
     */
    public V get(int index)
    {
        if (index >= size) {
            throw new ArrayIndexOutOfBoundsException(index);
        }

        return data[index];
    }

    /**
     * Replaces the element at the specified position in this Vector with the
     * specified element.
     *
     * @param index   index of element to replace.
     * @param element element to be stored at the specified position.
     * @return the element previously at the specified position.
     * @throws java.lang.ArrayIndexOutOfBoundsException
     *          index out of range
     *          (index &lt; 0 || index &gt;= size()).
     * @throws java.lang.IllegalArgumentException
     *          fromIndex &gt; toIndex.
     * @since 1.2
     */
    public V set(int index, V element)
    {
        throw new IllegalStateException("Unsupported");
    }


    /**
     * Inserts the specified element at the specified position in this Vector.
     * Shifts the element currently at that position (if any) and any
     * subsequent elements to the right (adds one to their indices).
     *
     * @param index index at which the specified element is to be inserted.
     * @param obj   element to be inserted.
     * @throws java.lang.ArrayIndexOutOfBoundsException
     *          index is out of range
     *          (index &lt; 0 || index &gt; size()).
     * @since 1.2
     */
    public void add(int index, V obj)
    {
        add(obj);
    }

    /**
     * Removes all of the elements from this Vector.  The Vector will
     * be empty after this call returns (unless it throws an exception).
     *
     * @since 1.2
     */
    public void clear()
    {
        for (int i = 0; i < size; i++) {
            data[i] = null;
        }

        size = 0;
    }

    /**
     * Inserts all of the elements in in the specified Collection into this
     * Vector at the specified position.  Shifts the element currently at
     * that position (if any) and any subsequent elements to the right
     * (increases their indices).  The new elements will appear in the Vector
     * in the order that they are returned by the specified Collection's
     * iterator.
     *
     * @param index index at which to insert first element
     *              from the specified collection.
     * @param c     elements to be inserted into this Vector.
     * @throws java.lang.ArrayIndexOutOfBoundsException
     *          index out of range (index
     *          &lt; 0 || index &gt; size()).
     * @since 1.2
     */
    public boolean addAll(int index, Collection<? extends V> c)
    {
        return addAll(c);
    }

    /**
     * Returns a view of the portion of this list between <tt>fromIndex</tt>,
     * inclusive, and <tt>toIndex</tt>, exclusive.  (If <tt>fromIndex</tt> and
     * <tt>toIndex</tt> are equal, the returned list is empty.)  The returned
     * list is backed by this list, so changes in the returned list are
     * reflected in this list, and vice-versa.  The returned list supports all
     * of the optional list operations supported by this list.<p>
     * <p/>
     * This method eliminates the need for explicit range operations (of the
     * sort that commonly exist for arrays).  Any operation that expects a
     * list can be used as a range operation by operating on a subList view
     * instead of a whole list.  For example, the following idiom removes a
     * range of elements from a list:
     * <pre>
     *     list.subList(from, to).clear();
     * </pre>
     * Similar idioms may be constructed for <tt>indexOf</tt> and
     * <tt>lastIndexOf</tt>, and all of the algorithms in the
     * <tt>Collections</tt> class can be applied to a subList.<p>
     * <p/>
     * The semantics of the list returned by this method become undefined if
     * the backing list (i.e., this list) is <i>structurally modified</i> in
     * any way other than via the returned list.  (Structural modifications are
     * those that change the size of the list, or otherwise perturb it in such
     * a fashion that iterations in progress may yield incorrect results.)<p>
     * <p/>
     * This implementation returns a list that subclasses
     * <tt>AbstractList</tt>.  The subclass stores, in private fields, the
     * offset of the subList within the backing list, the size of the subList
     * (which can change over its lifetime), and the expected
     * <tt>modCount</tt> value of the backing list.  There are two variants
     * of the subclass, one of which implements <tt>RandomAccess</tt>.
     * If this list implements <tt>RandomAccess</tt> the returned list will
     * be an instance of the subclass that implements <tt>RandomAccess</tt>.<p>
     * <p/>
     * The subclass's <tt>set(int, Object)</tt>, <tt>get(int)</tt>,
     * <tt>add(int, Object)</tt>, <tt>remove(int)</tt>, <tt>addAll(int,
     * Collection)</tt> and <tt>removeRange(int, int)</tt> methods all
     * delegate to the corresponding methods on the backing abstract list,
     * after bounds-checking the index and adjusting for the offset.  The
     * <tt>addAll(Collection c)</tt> method merely returns <tt>addAll(size,
     * c)</tt>.<p>
     * <p/>
     * The <tt>listIterator(int)</tt> method returns a "wrapper object" over a
     * list iterator on the backing list, which is created with the
     * corresponding method on the backing list.  The <tt>iterator</tt> method
     * merely returns <tt>listIterator()</tt>, and the <tt>size</tt> method
     * merely returns the subclass's <tt>size</tt> field.<p>
     * <p/>
     * All methods first check to see if the actual <tt>modCount</tt> of the
     * backing list is equal to its expected value, and throw a
     * <tt>ConcurrentModificationException</tt> if it is not.
     *
     * @param fromIndex low endpoint (inclusive) of the subList.
     * @param toIndex   high endpoint (exclusive) of the subList.
     * @return a view of the specified range within this list.
     * @throws IndexOutOfBoundsException endpoint index value out of range
     *                                   <tt>(fromIndex &lt; 0 || toIndex &gt; size)</tt>
     * @throws IllegalArgumentException  endpoint indices out of order
     *                                   <tt>(fromIndex &gt; toIndex)</tt>
     */
    public List<V> subList(int fromIndex, int toIndex)
    {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException("Index out of bound:["
                    + fromIndex + ", " + toIndex + "]");
        }

        int num = toIndex - fromIndex;
        V[] array = (V[])new Object[num];
        System.arraycopy(data, fromIndex, array, 0, num);

        SortedList<V> list = new SortedList<V>();
        list.data = array;
        list.size = num;
        list.comparator = comparator;

        return list;
    }

    /**
     * 区段删除
     *
     * @param from low endpoint (inclusive) of the subList.
     * @param to   high endpoint (exclusive) of the subList.
     * @return a view of the specified range within this list.
     * @throws IndexOutOfBoundsException endpoint index value out of range
     *                                   <tt>(fromIndex &lt; 0 || toIndex &gt; size)</tt>
     * @throws IllegalArgumentException  endpoint indices out of order
     *                                   <tt>(fromIndex &gt; toIndex)</tt>
     */
    public List<V> remove(int from, int to)
    {
        List<V> list = subList(from, to);
        int removed = list.size();
        if (removed > 0) {
            int next = to + 1;
            if (next < size) {
                System.arraycopy(data, next, data, from, size - next);
            }
            // Let gc do its work
            int newCount = size - removed;
            while (size > newCount) {
                data[--size] = null;
            }
            modCount++;
        }

        return list;
    }
}
