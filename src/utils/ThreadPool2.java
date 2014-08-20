package utils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: youxueliu
 * Date: 13-11-15
 * Time: 下午4:41
 */
public class ThreadPool2
{
    private static Map<String,List<Future<?>>> sTasks = Maps.newConcurrentHashMap();
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 20;
    private static final int KEEP_ALIVE = 1;

    private static final ThreadFactory sThreadFactor = new ThreadFactory()
    {
        private final AtomicInteger mCount = new AtomicInteger(1);
        @Override
        public Thread newThread(Runnable runnable)
        {
            return new Thread(runnable, "new Thread at ThreadPool2 #" + mCount.getAndIncrement());
        }
    };
    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE,MAXIMUM_POOL_SIZE,KEEP_ALIVE, TimeUnit.SECONDS,new SynchronousQueue<Runnable>(),sThreadFactor) ;

    /**
     * 给页面添加任务
     * @param key 协同任务共同的特征
     * @param runnable
     */
    public static void add(String key,final Runnable runnable)
    {
        if (Strings.isNullOrEmpty(key))
        {
            ThreadPool.add(runnable);
            return;
        }

        final Future<?> f = THREAD_POOL_EXECUTOR.submit(runnable);
        List<Future<?>> futures = sTasks.get(key);
        if (futures == null)
        {
            futures = Lists.newCopyOnWriteArrayList();
            futures.add(f);
            sTasks.put(key,futures);
        }
    }


    /**
     * 把页面任务取消
     * @param key
     */
    public static void cancel(String key)
    {
        final List<Future<?>> futures = sTasks.get(key);
        if (futures != null)
        {
            for (Future<?> f : futures)
            {
                if (!f.isDone())
                {
                    f.cancel(true);
                }
            }

            futures.clear();

            if (Aggregates.isNullOrEmpty(futures))
                sTasks.remove(key);
        }
    }

    public static void shutDown()
    {
        if (!THREAD_POOL_EXECUTOR.isShutdown())
            THREAD_POOL_EXECUTOR.shutdownNow();
    }
}
