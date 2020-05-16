package task;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: STAR
 * Date: 2020 -01
 * Time: 9:07
 */
public class FileScanTask {
    private  final ExecutorService POLL = Executors.newFixedThreadPool(4);
    //private static volatile int COUNT;
    private  AtomicInteger COUNT  = new AtomicInteger();

    private  final CountDownLatch LATCH = new CountDownLatch(1);


    private  FileScanCallback callback;

    public FileScanTask(FileScanCallback callback) {
        this.callback = callback;
    }

    /**
     * 启动扫描根目录
     */
    public void startScan(File root) {
//        synchronized (this) {
//            COUNT++;
//        }
        COUNT.incrementAndGet();
        POLL.execute(new Runnable() {
            @Override
            public void run() {
                list(root);
            }
        });
    }

    /**
     * 等待所有扫描任务扫描完毕
     */
    public void waitFinish() throws InterruptedException {

//        try {
//            synchronized (this) {
//                this.wait();
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        try {
            LATCH.await();
        } finally {
            // 中断所有线程
            POLL.shutdown();//调用每个线程的interrupt()中断
            // POLL.shutdownNow();// 调用的是stop()
        }
    }

    public void list (File dir) {
        if(!Thread.interrupted()) {
            try {
                callback.execute(dir);
                //System.out.println(dir.getPath());
                if (dir.isDirectory()) {
                    File[] children = dir.listFiles();
                    if (children != null && children.length > 0) {
                        for (File child : children) {
                            // 启动子线程执行子线程文件夹的扫描任务
                            if (child.isDirectory()) {
//                        synchronized (this) {
//                            COUNT++;
//                        }
                                COUNT.incrementAndGet();
                                POLL.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        list(child);
                                    }
                                });
                            } else {
                                callback.execute(child);
                               // System.out.println(child.getPath());
                            }
                        }
                    }
                }
            } finally {
                //        synchronized (this) {
//            //COUNT--;
//            if (COUNT == 0) {
//                this.notifyAll();
//            }
//        }

                // 所有线程执行完毕
                if (COUNT.decrementAndGet() == 0) {
                    //通知
                    LATCH.countDown();
                }
            }
        }

    }


//    public static void main(String[] args) throws InterruptedException {
//        FileScanTask task = new FileScanTask();
//        task.startScan(new File("C:\\Users\\STAR\\Desktop\\比特C"));
//        synchronized (task) {
//            task.wait();
//        }
//        System.out.println("执行完毕");
//
//    }

}
