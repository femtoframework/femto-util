package org.femtoframework.util;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;


/**
 * 将<code>Object[]</code>转变为列举形式
 *
 * @author fengyun
 * @version 1.02 Mar 4, 2004 14:42 PM Fixed ListIterator behavior
 * @see java.util.ListIterator
 * @see java.util.Enumeration
 */
public class ArrayIterator<V> implements ListIterator<V>, Iterator<V>, Enumeration<V>
{
    /**
     * 数组中的起始位置
     */
    protected int off;

    /**
     * 数组中的结束位置
     */
    protected int end;

    /**
     * 在数组中的索引
     */
    protected int index;

    /**
     * 数组
     */
    protected V[] array;

    /**
     * 构造
     *
     * @param array 数组
     */
    protected ArrayIterator(V[] array)
    {
        this(array, 0, array.length);
    }

    /**
     * 构造
     *
     * @param array 数组
     * @param off   起始位置
     * @param len   有效访问长度
     * @throws NullPointerException           如果array是<code>null</code>
     * @throws ArrayIndexOutOfBoundsException 如果给定的起始位置和长度有问题
     */
    protected ArrayIterator(V[] array, int off, int len)
    {
        if (array == null) {
            throw new NullPointerException("Array is null");
        }

        if (off < 0) {
            throw new ArrayIndexOutOfBoundsException("Off=" + off);
        }
        if (len < 0) {
            throw new ArrayIndexOutOfBoundsException("Len=" + len);
        }
        if (off > array.length - len) {
            throw new ArrayIndexOutOfBoundsException("Invalid off=" + off + " or len=" + len);
        }
        this.array = array;
        this.off = off;
        this.index = off;
        this.end = off + len;
    }

    /**
     * 返回是否还有后面的元素
     *
     * @return <tt>true</tt> 如果还存在
     */
    public boolean hasNext()
    {
        return index < end;
    }


    /**
     * 返回后一个
     *
     * @return [Object] 存在后一个
     * @throws NoSuchElementException 如果没有元素
     */
    public V next()
    {
        if (index < end) {
            return array[index++];
        }
        throw new NoSuchElementException("No element left");
    }

    /**
     * 返回是否还有前面的元素
     *
     * @return <tt>true</tt> 如果还存在
     */
    public boolean hasPrevious()
    {
        return index > off;
    }

    /**
     * 返回前一个
     *
     * @return [Object] 存在前一个
     * @throws NoSuchElementException 如果没有元素
     */
    public V previous()
    {
        if (index > off) {
            return array[--index];
        }
        throw new NoSuchElementException("No element");
    }

    /**
     * 返回后一个的索引
     *
     * @return 索引,如果不存在返回<code>-1</code>
     */
    public int nextIndex()
    {
        return index;
    }

    /**
     * 返回前一个的索引
     *
     * @return 索引,如果不存在返回<code>-1</code>
     */
    public int previousIndex()
    {
        return index - 1;
    }


    /**
     * 将当前位置的元素设置为<code>null</code>
     *
     * @throws IllegalStateException 如果当前的位置不对
     */
    public void remove()
    {
        if (index >= off && index <= end) {
            array[index] = null;
        }
        throw new IllegalStateException("Invalid Index");
    }

    /**
     * 设置当前位置的元素值
     *
     * @param obj 对象
     * @throws IllegalStateException 如果当前的位置不对
     * @throws ClassCastException    对象类型不对
     */
    public void set(V obj)
    {
        if (index >= off && index <= end) {
            array[index] = obj;
        }
        throw new IllegalStateException("Invalid Index");
    }

    /**
     * 不支持该操作
     *
     * @throws UnsupportedOperationException 不支持
     */
    public void add(V o)
    {
        throw new UnsupportedOperationException("Unsupported");
    }

    /**
     * 实现java.util.Enumeration
     *
     * @see java.util.Enumeration#hasMoreElements()
     * @see #hasNext()
     */
    public boolean hasMoreElements()
    {
        return hasNext();
    }

    /**
     * 实现java.util.Enumeration
     *
     * @see java.util.Enumeration#nextElement()
     * @see #next()
     */
    public V nextElement()
    {
        return next();
    }
}
