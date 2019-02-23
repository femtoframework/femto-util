package org.femtoframework.util.queue;

import java.util.AbstractQueue;
import java.util.Iterator;

/**
 * 链表式队列
 *
 * @author fengyun
 * @version 1.00 2005-2-9 15:24:09
 */
public class LinkedQueue<V> extends AbstractQueue<V> implements Queue<V>
{
    private int size = 0;

    //Head
    private Entry<V> head = new Entry<V>();
    private Entry<V> tail = head;

    //Free Head
    private static final int MAX_FREE = 16;
    private Entry<V> free = new Entry<V>();
    private int freeSize = 0;

    public LinkedQueue()
    {
    }

    /**
     * Returns an iterator over the elements contained in this collection.
     *
     * @return an iterator over the elements contained in this collection
     */
    @Override
    public Iterator<V> iterator() {
        throw new IllegalStateException("Unsupported");
    }

    public synchronized boolean isEmpty()
    {
        return size() <= 0;
    }

    public int size()
    {
        return size;
    }

    /**
     * 入队
     *
     * @param obj 对象
     * @return 对象是否已经入队
     */
    public synchronized boolean offer(V obj)
    {
        if (obj != null) {
            offer0(obj);
            notifyAll();
            return true;
        }
        else {
            return false;
        }
    }

    private Entry<V> getFree()
    {
        if (free.next == null) {
            return new Entry<V>();
        }
        else {
            Entry<V> entry = free.next;
            free.next = entry.next;
            entry.next = null;
            freeSize--;
            return entry;
        }
    }

    private void recycle(Entry<V> entry)
    {
        entry.recycle();
        if (freeSize < MAX_FREE) {
            entry.next = free.next;
            free.next = entry;
            freeSize++;
        }
    }

    protected synchronized void offer0(V obj)
    {
        Entry<V> entry = getFree();
        entry.value = obj;
        tail.next = entry;
        tail = entry;
        size++;
    }

    protected V poll0()
    {
        Entry<V> entry = head.next;
        V obj = entry.value;
        head.next = entry.next;
        if (tail == entry) {
            tail = head;
        }
        size--;
        recycle(entry);
        return obj;
    }

    protected V poll1()
    {
        while (isEmpty()) {
            try {
                wait();
            }
            catch (InterruptedException e) {
                return null;
            }
        }
        return poll0();
    }

    public synchronized V poll()
    {
        return poll1();
    }

    /**
     * 选取指定的对象
     *
     * @param i 索引
     */
    public synchronized V peek(int i)
    {
        if (i < 0 || i >= size) {
            throw new IllegalArgumentException("Index out of bound:" + i);
        }

        int j = 0;
        Entry<V> entry = head.next;
        for (; j < i; j++) {
            entry = entry.next;
        }

        return entry.value;
    }

    /**
     * 根据删除指定的对象
     *
     * @return 返回对应的对象
     */
    public V remove(int i)
    {
        if (i < 0 || i >= size) {
            throw new IllegalArgumentException("Index out of bound:" + i);
        }

        synchronized (this) {
            if (i == 0) {
                return poll0();
            }
            else {
                int j = 0;
                int e = i - 1;
                Entry<V> pre = head.next;
                for (; j < e; j++) {
                    pre = pre.next;
                }

                Entry <V>entry = pre.next;
                V obj = entry.value;
                pre.next = entry.next;
                if (tail == entry) {
                    tail = pre;
                }
                size--;
                recycle(entry);
                return obj;
            }
        }
    }

    /**
     * 将指定的对象从队列中删除
     *
     * @param obj 对象
     * @return 返回对应的对象
     */
    public boolean remove(Object obj)
    {
        if (isEmpty()) {
            return false;
        }
        synchronized (this) {
            Entry<V> pre = head;
            Entry<V> entry = pre.next;
            while (entry != null) {
                if (obj.equals(entry.value)) {
                    pre.next = entry.next;
                    if (tail == entry) {
                        tail = pre;
                    }
                    recycle(entry);
                    //删除该节点
                    size--;
                    return true;
                }
                pre = entry;
                entry = entry.next;
            }
        }
        return false;
    }

    /**
     * 出队<br>
     * 等待指定的毫秒数，如果没有对象入队，返回<code>null</code>
     *
     * @param timeout 超时
     * @return 对象
     */
    public synchronized V poll(long timeout)
    {
        if (timeout <= 0) {
            return poll1();
        }

        long start = System.currentTimeMillis();
        int wait = 500;
        if (timeout < wait) {
            wait = 50;
        }

        while (isEmpty()) {
            try {
                wait(wait);
            }
            catch (InterruptedException e) {
                return null;
            }
            if (!isEmpty()) {
                break;
            }
            else if (System.currentTimeMillis() - start > timeout) {
                return null;
            }
        }
        return poll0();
    }

    /**
     * Queue Size;
     */
    public String toString()
    {
        return "Queue Size:" + size;
    }

    static class Entry<V>
    {
        Entry<V> next = null;
        V value = null;

        Entry()
        {
        }

        Entry(V value, Entry<V> next)
        {
            this.value = value;
            this.next = next;
        }

        void recycle()
        {
            this.next = null;
            this.value = null;
        }

        public String toString()
        {
            return value.toString();
        }
    }

}
