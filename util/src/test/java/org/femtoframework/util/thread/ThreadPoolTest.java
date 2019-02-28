package org.femtoframework.util.thread;


import org.femtoframework.bean.BeanPhase;
import org.femtoframework.util.nutlet.NutletUtil;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 测试ThreadPool
 *
 * @author fengyun
 * @version 1.00 2005-2-20 2:21:51
 */
public class ThreadPoolTest
{
    /**
     * 测试构造
     *
     * @throws Exception
     */
    @Test
    public void testThreadPool0() throws Exception
    {
        new ThreadPool();
    }

    /**
     * 测试doInit
     *
     * @throws Exception
     */
    @Test
    public void testDoInit() throws Exception
    {
        ThreadPool pool = new ThreadPool();
        pool.init();
    }

    /**
     * 测试doStart
     *
     * @throws Exception
     */
    @Test
    public void testDoStart() throws Exception
    {
        ThreadPool pool = new ThreadPool();
        pool.init();
        pool.start();

        pool.stop();
    }

    /**
     * 测试设置最大线程数
     *
     * @throws Exception
     */
    @Test
    public void testSetMaxThreads() throws Exception
    {
        ThreadPool pool = new ThreadPool();
        pool.setMaxThreads(999);
        assertEquals(999, pool.getMaxThreads());
    }

    /**
     * 测试返回最大线程数
     *
     * @throws Exception
     */
    @Test
    public void testGetMaxThreads() throws Exception
    {
        ThreadPool pool = new ThreadPool();
        assertEquals(ThreadPool.MAX_THREADS, pool.getMaxThreads());
    }

    /**
     * 测试设置最小线程数
     *
     * @throws Exception
     */
    @Test
    public void testSetMinSpareThreads() throws Exception
    {
        ThreadPool pool = new ThreadPool();
        pool.setMinSpareThreads(999);
        assertEquals(999, pool.getMinSpareThreads());
    }

    /**
     * 返回最小线程数
     *
     * @throws Exception
     */
    @Test
    public void testGetMinSpareThreads() throws Exception
    {
        ThreadPool pool = new ThreadPool();
        assertEquals(ThreadPool.MIN_SPARE_THREADS, pool.getMinSpareThreads());
    }

    /**
     * 测试设置最大线程数
     *
     * @throws Exception
     */
    @Test
    public void testSetMaxSpareThreads() throws Exception
    {
        ThreadPool pool = new ThreadPool();
        pool.setMaxSpareThreads(999);
        assertEquals(999, pool.getMaxSpareThreads());
    }

    /**
     * 测试返回最大线程数
     *
     * @throws Exception
     */
    @Test
    public void testGetMaxSpareThreads() throws Exception
    {
        ThreadPool pool = new ThreadPool();
        assertEquals(ThreadPool.MAX_SPARE_THREADS, pool.getMaxSpareThreads());
    }

    /**
     * 测试返回当前的线程总数
     *
     * @throws Exception
     */
    @Test
    public void testGetCurrentThreadCount0() throws Exception
    {
        ThreadPool pool = new ThreadPool();
        assertEquals(0, pool.getCurrentThreadCount());
        pool.start();
        assertEquals(pool.getMinSpareThreads(), pool.getCurrentThreadCount());

        pool.stop();
        pool.destroy();
    }

    /**
     * 测试返回当前的线程总数
     *
     * @throws Exception
     */
    @Test
    public void testGetCurrentThreadCount1() throws Exception
    {
        ThreadPool pool = new ThreadPool();
        assertEquals(0, pool.getCurrentThreadCount());
        pool.start();
        assertEquals(pool.getMinSpareThreads(), pool.getCurrentThreadCount());
        pool.setDaemon(true);

        for (int i = 0; i < 20; i++) {
            pool.allocate();
        }
        assertEquals(20, pool.getCurrentThreadCount());

        pool.stop();
        pool.destroy();
    }

    /**
     * 测试返回当前在使用中的线程数目
     *
     * @throws Exception
     */
    @Test
    public void testGetCurrentThreadBusy() throws Exception
    {
        ThreadPool pool = new ThreadPool();
        assertEquals(0, pool.getCurrentThreadBusy());
        pool.start();
        assertEquals(0, pool.getCurrentThreadBusy());
        pool.setDaemon(true);

        int n = NutletUtil.getInt(pool.getMaxThreads());
        for (int i = 0; i < n; i++) {
            pool.allocate();
        }
        assertEquals(n, pool.getCurrentThreadBusy());

        pool.stop();
        pool.destroy();
    }

    /**
     * 测试执行一个任务
     *
     * @throws Exception
     */
    @Test
    public void testExecute0() throws Exception
    {
        ThreadPool pool = new ThreadPool();
        pool.start();
        pool.setDaemon(true);

        final boolean[] done = new boolean[1];
        pool.execute(new Runnable()
        {

            /**
             * 运行
             */
            public void run()
            {
                done[0] = true;
            }
        });

        Thread.sleep(100);
        assertTrue(done[0]);

        pool.stop();
        pool.destroy();
    }

    /**
     * 测试执行一个任务（需要等待）
     *
     * @throws Exception
     */
    @Test
    public void testExecute1() throws Exception
    {
        final ThreadPool pool = new ThreadPool();
        pool.start();
        pool.setDaemon(true);
        pool.setMaxThreads(10);

        ThreadController controller = null;
        for (int i = 0, threads = pool.getMinSpareThreads(); i < threads; i++) {
            controller = pool.allocate();
        }

        final boolean[] done = new boolean[1];
        Thread thread = new Thread()
        {
            public void run()
            {
                pool.execute(new Runnable()
                {

                    /**
                     * 运行
                     */
                    public void run()
                    {
                        done[0] = true;
                    }
                });
            }
        };
        thread.start();

        pool.recycle(controller);

        Thread.sleep(100);
        assertTrue(done[0]);

        pool.stop();
        pool.destroy();
    }

    /**
     * 测试竞争线程（如果没有可用的线程，则一直等到有可用的线程为止）
     *
     * @throws Exception
     */
    @Test
    public void testCompete() throws Exception
    {
        final ThreadPool pool = new ThreadPool();
        pool.start();
        pool.setDaemon(true);
        pool.setMaxThreads(10);

        ThreadController controller = null;
        for (int i = 0, threads = pool.getMinSpareThreads(); i < threads; i++) {
            controller = pool.allocate();
        }

        final ThreadController[] controllers = new ThreadController[1];
        Thread thread = new Thread()
        {
            public void run()
            {
                controllers[0] = pool.compete();
            }
        };
        thread.start();

        pool.recycle(controller);

        Thread.sleep(100);
        assertNotNull(controllers[0]);

        pool.stop();
        pool.destroy();
    }

    /**
     * 测试分配线程，如果没有可用的线程则返回<code>null</code>
     *
     * @throws Exception
     */
    @Test
    public void testAllocate() throws Exception
    {
        final ThreadPool pool = new ThreadPool();
        pool.start();
        pool.setDaemon(true);
        pool.setMaxThreads(10);

        ThreadController controller = null;
        for (int i = 0, threads = pool.getMinSpareThreads(); i < threads; i++) {
            controller = pool.allocate();
        }

        assertNull(pool.allocate());
        pool.recycle(controller);

        assertNotNull(pool.allocate());

        pool.stop();
        pool.destroy();
    }

    /**
     * 测试当没有线程的时候的扩展处理方法
     *
     * @throws Exception
     */
    @Test
    public void testCompeteNoThread() throws Exception
    {
        final ThreadPool pool = new ThreadPool();
        pool.start();
        pool.setDaemon(true);
        pool.setMaxThreads(10);

        pool.competeNoThread();

        pool.stop();
        pool.destroy();
    }

    /**
     * 测试没有线程可以分配的时候调用的方法
     *
     * @throws Exception
     */
    @Test
    public void testAllocateNoThread() throws Exception
    {
        final ThreadPool pool = new ThreadPool();
        pool.start();
        pool.setDaemon(true);
        pool.setMaxThreads(10);

        pool.allocateNoThread();

        pool.stop();
        pool.destroy();
    }

    /**
     * 测试真正停止
     *
     * @throws Exception
     */
    @Test
    public void testDoStop() throws Exception
    {
        final ThreadPool pool = new ThreadPool();
        pool.start();
        pool.setDaemon(true);
        pool.setMaxThreads(10);

        pool.allocateNoThread();

        pool.stop();
        pool.destroy();
    }


    /**
     * 测试真正的摧毁
     *
     * @throws Exception
     */
    @Test
    public void testDoDestroy() throws Exception
    {
        final ThreadPool pool = new ThreadPool();
        pool.start();
        pool.setDaemon(true);
        pool.setMaxThreads(10);

        pool.allocateNoThread();

        pool.stop();
        pool.doDestroy();
    }


    /**
     * 测试监控线程池
     *
     * @throws Exception
     */
    @Test
    public void testDoMonitor() throws Exception
    {
        final ThreadPool pool = new ThreadPool();
        pool.start();
        pool.setDaemon(true);
        pool.setMaxThreads(10);

        pool.doMonitor();

        pool.stop();
        pool.destroy();
    }

    /**
     * 测试检查空闲的线程
     *
     * @throws Exception
     */
    @Test
    public void testCheckSpareControllers() throws Exception
    {
        final ThreadPool pool = new ThreadPool();
        pool.start();
        pool.setDaemon(true);
        pool.setMaxSpareThreads(15);

        ThreadController[] controllers = new ThreadController[11];
        for (int i = 0, threads = controllers.length; i < threads; i++) {
            controllers[i] = pool.allocate();
        }

        for (int i = 0, threads = controllers.length; i < threads; i++) {
            pool.recycle(controllers[i]);
        }

        assertEquals(pool.getMinSpareThreads()
                     + pool.getIncThreads(), pool.getCurrentThreadCount());
        pool.checkSpareControllers();
        assertEquals(pool.getMaxSpareThreads(), pool.getCurrentThreadCount());

        pool.stop();
        pool.destroy();
    }

    /**
     * 测试回收线程控制器
     *
     * @throws Exception
     */
    @Test
    public void testRecycle() throws Exception
    {
        final ThreadPool pool = new ThreadPool();
        pool.start();
        pool.setDaemon(true);

        ThreadController[] controllers = new ThreadController[10];
        for (int i = 0, threads = controllers.length; i < threads; i++) {
            controllers[i] = pool.allocate();
        }
        //如果线程池已经停止，那么回收之后所有的线程也将被停止
        pool.stop();

        for (int i = 0, threads = controllers.length; i < threads; i++) {
            assertTrue(controllers[i].getBeanPhase() == BeanPhase.STARTED);
            pool.recycle(controllers[i]);
            assertTrue(controllers[i].getBeanPhase() == BeanPhase.STOPPED);
        }

        pool.destroy();
    }

    /**
     * 测试终止线程
     *
     * @throws Exception
     */
    @Test
    public void testTerminate() throws Exception
    {
        final ThreadPool pool = new ThreadPool();
        pool.start();
        pool.setDaemon(true);

        ThreadController[] controllers = new ThreadController[pool.getMinSpareThreads()];
        for (int i = 0, threads = controllers.length; i < threads; i++) {
            controllers[i] = pool.allocate();
        }

        //结束相应数目的线程
        assertEquals(pool.getMinSpareThreads(), pool.getCurrentThreadCount());
        for (int i = 0, threads = controllers.length; i < threads; i++) {
            assertTrue(controllers[i].getBeanPhase() == BeanPhase.STARTED);
            pool.terminate(controllers[i]);
            assertTrue(controllers[i].getBeanPhase() == BeanPhase.DESTROYED);
        }

        //线程池当前线程数已经减少
        assertEquals(0, pool.getCurrentThreadCount());

        //重新开辟，依然可以开辟相应数量的线程
        for (int i = 0, threads = controllers.length; i < threads; i++) {
            controllers[i] = pool.allocate();
        }
        assertEquals(pool.getMinSpareThreads(), pool.getCurrentThreadCount());
        pool.stop();
        pool.destroy();
    }

    /**
     * 实际摧毁一个线程
     *
     * @throws Exception
     */
    @Test
    public void testDestroyIt() throws Exception
    {
        final ThreadPool pool = new ThreadPool();
        pool.start();
        pool.setDaemon(true);

        ThreadController controller = pool.allocate();
        assertTrue(controller.getBeanPhase() == BeanPhase.STARTED);
        pool.destroyIt(controller);
        assertTrue(controller.getBeanPhase() == BeanPhase.DESTROYED);
        pool.stop();
        pool.destroy();
    }

    /**
     * 测试调整配置中不合理的部分
     *
     * @throws Exception
     */
    @Test
    public void testAdjustLimits() throws Exception
    {
        final ThreadPool pool = new ThreadPool();
        pool.setMinSpareThreads(100);
        pool.setMaxThreads(10);
        pool.setMaxSpareThreads(9999);

        assertEquals(100, pool.getMinSpareThreads());
        assertEquals(10, pool.getMaxThreads());
        assertEquals(9999, pool.getMaxSpareThreads());

        pool.adjustLimits();
        assertEquals(10, pool.getMinSpareThreads());
        assertEquals(10, pool.getMaxThreads());
        assertEquals(10, pool.getMaxSpareThreads());
    }

    /**
     * 测试创建线程
     *
     * @throws Exception
     */
    @Test
    public void testCreateThread() throws Exception
    {
        final ThreadPool pool = new ThreadPool();
        pool.init();

        assertEquals(0, pool.getCurrentThreadCount());
        pool.createThread();
        assertEquals(1, pool.getCurrentThreadCount());
        pool.stop();
        pool.destroy();
    }

    /**
     * 测试批量打开线程
     *
     * @throws Exception
     */
    @Test
    public void testOpenThreads0() throws Exception
    {
        final ThreadPool pool = new ThreadPool();
        pool.start();

        assertEquals(pool.getMinSpareThreads(), pool.getCurrentThreadCount());
        assertTrue(pool.getMaxSpareThreads() > pool.getMinSpareThreads());
        pool.openThreads(pool.getMaxSpareThreads());
        assertEquals(pool.getMaxSpareThreads(), pool.getCurrentThreadCount());
        pool.stop();
        pool.destroy();
    }

    /**
     * 测试批量打开线程，如果制定的目标线程数超过最大线程数，那么用最大线程数
     *
     * @throws Exception
     */
    @Test
    public void testOpenThreads1() throws Exception
    {
        final ThreadPool pool = new ThreadPool();
        pool.start();

        int n = NutletUtil.getInt(0xFF);
        assertEquals(pool.getMinSpareThreads(), pool.getCurrentThreadCount());
        pool.openThreads(n);

        //如果超过最大线程数，打开的线程为最大线程数
        n = n > pool.getMaxThreads() ? pool.getMaxThreads() : n;
        //如果小于最小线程数，打开的线程为最小线程数
        n = n < pool.getMinSpareThreads() ? pool.getMinSpareThreads() : n;

        assertEquals(n, pool.getCurrentThreadCount());
        pool.stop();
        pool.destroy();
    }

    /**
     * 测试返回增长的线程数
     *
     * @throws Exception
     */
    @Test
    public void testGetIncThreads() throws Exception
    {
        ThreadPool pool = new ThreadPool();
        assertEquals(ThreadPool.INC_THREADS, pool.getIncThreads());
    }

    /**
     * 测试设置增长线程数
     *
     * @throws Exception
     */
    @Test
    public void testSetIncThreads() throws Exception
    {
        ThreadPool pool = new ThreadPool();
        pool.setIncThreads(9);
        assertEquals(9, pool.getIncThreads());
    }

}