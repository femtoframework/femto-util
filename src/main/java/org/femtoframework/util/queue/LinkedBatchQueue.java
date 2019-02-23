package org.femtoframework.util.queue;

/**
 * 采用Linked方式的批处理队列
 *
 * @author fengyun
 * @version 1.00 2005-2-9 15:36:54
 */
public class LinkedBatchQueue<V>
    extends LinkedQueue<V>
    implements BatchQueue<V>
{
    /**
     * 添加一批对象
     *
     * @param batch 一批对象
     */
    public synchronized void offerBatch(V[] batch)
    {
        if (batch != null && batch.length > 0) {
            for (int i = 0; i < batch.length; i++) {
                offer0(batch[i]);
            }
            notifyAll();
        }
    }

    /**
     * 等待指定毫秒数，期望获取指定数量的对象
     *
     * @param wait
     * @param max
     */
    public synchronized V[] pollBatch(long wait, int max)
    {
        if (wait <= 50) {
            wait = 50;
        }

        long start = System.currentTimeMillis();
        while (isEmpty()) {
            try {
                wait(wait);
            }
            catch (InterruptedException e) {
                return null;
            }
            if (size() >= max) {
                break;
            }
            else if (System.currentTimeMillis() - start > wait) {
                if (isEmpty()) {
                    return null;
                }
                else {
                    break;
                }
            }
        }

        int s = size();
        s = s > max ? max : s;
        V[] objs = (V[])new Object[s];
        for (int i = 0; i < s; i++) {
            objs[i] = poll0();
        }
        return objs;
    }
}
