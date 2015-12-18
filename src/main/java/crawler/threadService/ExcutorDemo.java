package crawler.threadService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExcutorDemo {

    public static void main(String[] args) {
        // new Thread(new Task1()).start();
        // try {
        // Thread.sleep(5000);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
        // new Thread(new Task2()).start();

        // 可以提交（submit）无限多个任务到线程中，但是任何时刻，只有5个
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10; i++) {
            executorService.execute(new ATask(i));
            System.out.println("加入Task" + i);
        }
        executorService.shutdown();
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
