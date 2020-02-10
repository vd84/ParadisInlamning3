
// Peter Idestam-Almquist, 2020-02-04.
// Douglas Hammarstam doha6991

// [Do necessary modifications of this file.]

package paradis.assignment3;

// [You are welcome to add some import statements.]

import java.util.Random;
import java.util.concurrent.*;

public class Program1 {
    final static int NUM_WEBPAGES = 100;
    private static WebPage[] webPages = new WebPage[NUM_WEBPAGES];

    private static BlockingQueue<WebPage> downloadQueue = new ArrayBlockingQueue<WebPage>(NUM_WEBPAGES);
    //private static BlockingQueue<WebPage> queue = new ArrayBlockingQueue<WebPage>(NUM_WEBPAGES);
    static public Producer producer;
    public Consumer consumer;


    public class Consumer implements Runnable {
        private BlockingQueue<WebPage> queue;
        private Random random = new Random();

        Consumer(BlockingQueue<WebPage> queue) {
            this.queue = queue;

        }

        public void run() {
            while (true) {
                try {
                    WebPage webPage = queue.take();
                    consume(webPage);
                } catch (Exception exception) {
                    System.out.println(exception);
                }
            }
        }

        void consume(WebPage webPage) {
            // It takes some time to consume a task.
            try {
                Thread.sleep(random.nextInt(1000));
            } catch (Exception exception) {
                System.out.println(exception);
            }
            webPage.download();
        }
    }

    public static class Producer implements Runnable {
        private BlockingQueue<WebPage> queue;
        private Random random = new Random();

        Producer(BlockingQueue<WebPage> queue) {
            this.queue = queue;
        }

        public void run() {
            while (true) {
                try {
                    WebPage webPage = produce();
                    queue.put(webPage);
                } catch (Exception exception) {
                    System.out.println(exception);
                }
            }
        }

        WebPage produce() {
            // It takes some time to produce a task.
            try {
                Thread.sleep(random.nextInt(1000));
            } catch (Exception exception) {
                System.out.println(exception);
            }
            return new WebPage();
        }
    }

    class Task {
        private String data;

        Task(String data) {
            this.data = data;
        }

        String getData() {
            return data;
        }
    }

    // [You are welcome to add some variables.]

    // [You are welcome to modify this method, but it should NOT be parallelized.]
    private static void initialize() {
        for (int i = 0; i < NUM_WEBPAGES; i++) {
            webPages[i] = new WebPage(i, "http://www.site.se/page" + i + ".html");
            downloadQueue.add(webPages[i]);
            producer = new Producer(downloadQueue);


        }
    }

    // [Do modify this sequential part of the program.]
    private static void downloadWebPages() {

        downloadQueue.parallelStream().forEach(WebPage::download);


    }

    // [Do modify this sequential part of the program.]
    private static void analyzeWebPages() {
        for (int i = 0; i < NUM_WEBPAGES; i++) {
            webPages[i].analyze();
        }
    }

    // [Do modify this sequential part of the program.]
    private static void categorizeWebPages() {
        for (int i = 0; i < NUM_WEBPAGES; i++) {
            webPages[i].categorize();
        }
    }

    // [You are welcome to modify this method, but it should NOT be parallelized.]
    private static void presentResult() {
        for (int i = 0; i < NUM_WEBPAGES; i++) {
            System.out.println(webPages[i]);
        }
    }

    public static void main(String[] args) {




        // Initialize the list of webpages.
        initialize();

        // Start timing.
        long start = System.nanoTime();

        // Do the work.
        downloadWebPages();
        //analyzeWebPages();
        //categorizeWebPages();

        // Stop timing.
        long stop = System.nanoTime();

        // Present the result.
        presentResult();

        // Present the execution time.
        System.out.println("Execution time (seconds): " + (stop - start) / 1.0E9);
    }
}
