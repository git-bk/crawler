package crawler.connectorService;

import java.util.concurrent.ArrayBlockingQueue;

/*
 * 使用阻塞队列实现生产者消费者模型 1.展示效果 2.阻塞队列作用：
 * 阻塞队列（BlockingQueue）是一个支持两个附加操作的队列。
 * 1.在队列为空时，获取元素的线程会等待队列变为非空。
 * 2.当队列满时，存储元素的线程会等待队列可用 。
 * 
 */
public class ProducerConsumerDemo {

    // 队列中最多存在10个元素
    private final Integer                    queueSize = 10;
    // 阻塞队列
    private final ArrayBlockingQueue<String> queue     = new ArrayBlockingQueue<String>(queueSize);

    public void init() {
        new Thread(new Producer()).start();
        new Thread(new Consumer()).start();
    }

    public class Producer implements Runnable {

        @Override
        public void run() {
            produce();
        }

        private void produce() {
            int index = 1;
            while (true) {
                index++;
                try {
                    String item = "元素" + index;
                    queue.put(item);
                    System.out.println("向队列取中插入" + item + "，队列剩余空间：" + (queueSize - queue.size()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public class Consumer implements Runnable {

        @Override
        public void run() {
            consume();
        }

        private void consume() {
            while (true) {
                try {
                    String item = queue.take();
                    System.out.println("向队列取中取出" + item + "，队列剩余空间：" + (queueSize - queue.size()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
