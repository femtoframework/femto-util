package org.femtoframework.util.queue;

public interface BatchQueue<E> extends Queue<E> {

    /**
     * 添加一批对象
     *
     * @param batch 一批对象
     */
    void offerBatch(E[] batch);

    /**
     * 等待指定毫秒数，期望获取指定数量的对象
     *
     * @param timeout Timeout in milliseconds
     * @param max Max expected results
     */
    E[] pollBatch(long timeout, int max);
}
