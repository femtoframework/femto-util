package org.femtoframework.nio;

import org.femtoframework.util.nutlet.NutletUtil;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.*;

/**
 * @author fengyun
 * @version 1.00 2004-12-27 11:04:09
 */

public class ByteBufferPoolTest
{
    /**
     * 测试返回ByteBuffer，不指定容积
     *
     * @throws Exception
     */
    @Test
    public void testAllocate0() throws Exception
    {
        ByteBuffer buffer = ByteBufferPool.allocate();
        assertNotNull(buffer);
        assertEquals(1024, buffer.capacity());
        ByteBufferPool.recycle(buffer);
        ByteBuffer buffer2 = ByteBufferPool.allocate();
        assertTrue(buffer == buffer2);
        ByteBufferPool.recycle(buffer2);
    }

    private static int[] CAPACITIES = new int[]{
        ByteBufferPool.SIZE_512,
        ByteBufferPool.SIZE_1024,
        ByteBufferPool.SIZE_2048,
        ByteBufferPool.SIZE_4096,
        ByteBufferPool.SIZE_8192
    };

    /**
     * 测试返回ByteBuffer，指定容积（固定）
     *
     * @throws Exception
     */
    @Test
    public void testAllocate1() throws Exception
    {
        for (int i = 0; i < CAPACITIES.length; i++) {
            ByteBuffer buffer = ByteBufferPool.allocate(CAPACITIES[i]);
            assertNotNull(buffer);
            assertEquals(CAPACITIES[i], buffer.capacity());
            ByteBufferPool.recycle(buffer);
            ByteBuffer buffer2 = ByteBufferPool.allocate(CAPACITIES[i]);
            assertTrue(buffer == buffer2);
            ByteBufferPool.recycle(buffer2);
        }
    }

    private static int[] NONE_STANDARD = new int[]{
        333,
        999,
        1444,
        2049,
        5555
    };

    /**
     * 测试返回ByteBuffer，指定非标准容积
     *
     * @throws Exception
     */
    @Test
    public void testAllocate2() throws Exception
    {
        for (int i = 0; i < NONE_STANDARD.length; i++) {
            ByteBuffer buffer = ByteBufferPool.allocate(NONE_STANDARD[i]);
            assertNotNull(buffer);
            assertEquals(NONE_STANDARD[i], buffer.capacity());
            ByteBufferPool.recycle(buffer);
            ByteBuffer buffer2 = ByteBufferPool.allocate(NONE_STANDARD[i]);
            assertTrue(buffer != buffer2);
            ByteBufferPool.recycle(buffer2);
        }
    }

    /**
     * 测试小于8192
     *
     * @throws Exception
     */
    @Test
    public void testAllocateBySize1() throws Exception
    {
        for (int i = 0; i < NONE_STANDARD.length; i++) {
            ByteBuffer buffer = ByteBufferPool.allocateBySize(NONE_STANDARD[i]);
            assertNotNull(buffer);
            assertEquals(CAPACITIES[i], buffer.capacity());
            ByteBufferPool.recycle(buffer);
        }
    }

    /**
     * 测试超过8192
     *
     * @throws Exception
     */
    @Test
    public void testAllocateBySize2() throws Exception
    {
        int size = NutletUtil.getInt(1024) + 32 * 1024;
        ByteBuffer buffer = ByteBufferPool.allocateBySize(size);
        assertNotNull(buffer);
        assertEquals(size, buffer.capacity());
        ByteBufferPool.recycle(buffer);
    }

    /**
     * 是否拥有指定的Pool
     *
     * @throws Exception
     */
    @Test
    public void testHasPool() throws Exception
    {
        for (int i = 0; i < CAPACITIES.length; i++) {
            assertTrue(ByteBufferPool.hasPool(CAPACITIES[i]));
        }
        for (int i = 0; i < NONE_STANDARD.length; i++) {
            assertFalse(ByteBufferPool.hasPool(NONE_STANDARD[i]));
        }
    }

    /**
     * 测试回收，边界
     *
     * @throws Exception
     */
    @Test
    public void testRecycle0() throws Exception
    {
        ByteBufferPool.recycle(null);
    }
}