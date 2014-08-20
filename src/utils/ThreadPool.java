package utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池
 * 
 * @author chuckcheng
 * @version [版本号, 2011-9-28]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ThreadPool {
	
	private static final String CLASSTAG = ThreadPool.class.getSimpleName();
	
	private static final int POOL_SIZE = 10;

	private static ExecutorService executorService = Executors
			.newFixedThreadPool(POOL_SIZE);

//	private static Object classLock = ThreadPool.class;
	
	/**
	 * 不允许实例化
	 */
	private ThreadPool() { }

	/**
	 * 加入线程池
	 * 
	 * @param runnable
	 *            任务
	 * @see [类、类#方法、类#成员]
	 */
	public static void add(Runnable runnable) {
		try {
//			synchronized (classLock) {
				executorService.submit(runnable);
//			}
		} catch (Exception e) {
			LogUtils.error(CLASSTAG + e.getMessage());
		}

		// threadPool.execute(runnable);
	}

    public static void shutdown()
    {
        executorService.shutdownNow();
    }

	// private static ThreadPoolExecutor threadPool = new
	// ThreadPoolExecutor(2, 5,
	// 3, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(50),
	// new RejectedExecutionHandler()
	// {
	// public void rejectedExecution(final Runnable r,
	// final ThreadPoolExecutor executor)
	// {
	// Thread thread = new Thread()
	// {
	// public void run()
	// {
	// try
	// {
	// Thread.sleep(1000);
	// }
	// catch (InterruptedException e)
	// {
	// }
	// LogUtils.error(r.toString()
	// + ":　readd current thread to threadPool");
	// executor.execute(r);
	// };
	// };
	// thread.start();
	// }
	// });

}
