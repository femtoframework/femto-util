package org.femtoframework.util.queue;

/**
 * An extension of java.util.Queue
 *
 * @param <E>
 */
public interface Queue<E> extends java.util.Queue<E> {

    /**
     * Retrieves and removes the head of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @param timeout Timeout in milliseconds
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    E poll(long timeout);

    /**
     * Return element by given index
     */
    E remove(int i);


    /**
     * Retrieves, but does not remove, the head of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    @Override
    default E peek() {
        return peek(0);
    }

    /**
     * Peeks element by given index
     *
     * @param i Index
     * @return The element
     */
    E peek(int i);
}
