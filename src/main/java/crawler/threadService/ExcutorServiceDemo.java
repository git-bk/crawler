package crawler.threadService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExcutorServiceDemo {

    /*
     * 线程池的核心构造器
     * @param corePoolSize 核心工作线程数量，即使空闲着也不会被销户的线程数
     * @param maximumPoolSize 线程允许创建的最大线程数
     * @param keepAliveTime 当线程数大于核心工作线程数时，超出的那些已经空闲的线程被销毁前等待保持的时间
     * @param unit 时间单位（keepAliveTime）
     * @param workQueue the queue to use for holding tasks before they are executed. This queue will hold only the
     * {@code Runnable} tasks submitted by the {@code execute} method.
     * @param threadFactory the factory to use when the executor creates a new thread
     * @param handler the handler to use when execution is blocked because the thread bounds and queue capacities are
     * reached
     * @throws IllegalArgumentException if one of the following holds:<br> {@code corePoolSize < 0}<br> {@code
     * keepAliveTime < 0}<br> {@code maximumPoolSize <= 0}<br> {@code maximumPoolSize < corePoolSize}
     * @throws NullPointerException if {@code workQueue} or {@code threadFactory} or {@code handler} is null
     */
    // public ThreadPoolExecutor(
    // int corePoolSize,
    // int maximumPoolSize,
    // long keepAliveTime,
    // TimeUnit unit,
    // BlockingQueue<Runnable> workQueue,
    // ThreadFactory threadFactory,
    // RejectedExecutionHandler handler
    // )

    public static void main(String[] args) {

        // 1.可以提交（submit）无限多个任务到线程中，但是在任何时刻，都只有固定数量（现在是5）的工作线程，这些线程可以重复使用。
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10; i++) {
            executorService.execute(new ATask(i));
            executorService.execute(new BTask(i));
        }
        executorService.shutdown();//
        System.out.println(Thread.currentThread().getName() + "执行shutdown");
    }

    /**
     * A任务
     */
    private static class ATask implements Runnable {

        private final Integer id;

        public ATask(Integer id){
            this.id = id;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " in");
            System.out.println(Thread.currentThread().getName() + " run " + this.toString());
            System.out.println(Thread.currentThread().getName() + " over " + this.toString());
        }

        @Override
        public String toString() {
            return "ATask_" + this.id.toString();
        }
    }

    /**
     * B任务
     */
    private static class BTask implements Runnable {

        private final Integer id;

        public BTask(Integer id){
            this.id = id;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " in");
            System.out.println(Thread.currentThread().getName() + " run " + this.toString());
            System.out.println(Thread.currentThread().getName() + " over " + this.toString());
        }

        @Override
        public String toString() {
            return "BTask_" + this.id.toString();
        }

    }

}
