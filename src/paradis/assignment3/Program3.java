// Peter Idestam-Almquist, 2020-02-04.
// Douglas Hammarstam doha6991

// [Do necessary modifications of this file.]

package paradis.assignment3;

// [You are welcome to add some import statements.]

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public class Program3 {
    final static int NUM_WEBPAGES = 300;
    private static WebPage[] webPages = new WebPage[NUM_WEBPAGES];
    private static int NUM_THREADS = 3;
    private static BlockingQueue<WebPage> downloadQueue = new LinkedBlockingQueue<WebPage>();
    private static LinkedBlockingQueue<WebPage> analyzeQueue = new LinkedBlockingQueue<WebPage>();
    private static BlockingQueue<WebPage> categorizeQueue = new LinkedBlockingQueue<WebPage>();
    private static MyExecutor myExecutor = new MyExecutor(NUM_THREADS);
    private static LinkedBlockingQueue<WebPage> printQueue = new LinkedBlockingQueue<WebPage>();
    private static LinkedBlockingQueue<Runnable> runnables = new LinkedBlockingQueue<>();


    // [You are welcome to add some variables.]

    // [You are welcome to modify this method, but it should NOT be parallelized.]
    private static void initialize() {
        for (int i = 0; i < NUM_WEBPAGES; i++) {
            webPages[i] = new WebPage(i, "http://www.site.se/page" + i + ".html");
            downloadQueue.add(webPages[i]);


        }
    }

    // [Do modify this sequential part of the program.]
    private static void downloadWebPages() {
        WebPage webPage;
        webPage = downloadQueue.poll();
        if (webPage != null) {
            Runnable download = () -> {
                webPage.download();
                analyzeQueue.add(webPage);
            };
            myExecutor.execute(download);

        }


    }


    // [Do modify this sequential part of the program.]
    private static void analyzeWebPages() {
        WebPage webPage;
        webPage = analyzeQueue.poll();
        if (webPage != null) {
            Runnable analyze = () -> {
                webPage.analyze();
                categorizeQueue.add(webPage);

            };
            myExecutor.execute(analyze);
        }


    }

    // [Do modify this sequential part of the program.]
    private static void categorizeWebPages() {
        WebPage webPage;
        webPage = categorizeQueue.poll();
        if (webPage != null) {
            Runnable categorize = () -> {
                webPage.categorize();
                printQueue.add(webPage);
            };
            myExecutor.execute(categorize);
        }

    }


    // [You are welcome to modify this method, but it should NOT be parallelized.]
    private static void presentResult() {
        for (int i = 0; i < NUM_WEBPAGES; i++) {
            System.out.println(webPages[i]);
        }


    }

    private static class MyExecutor implements ExecutorService {
        boolean running;

        Thread[] threads;


        public MyExecutor(int numThreads) {
            threads = new Thread[numThreads];
            for (int i = 0; i < threads.length; i++) {
                threads[i] = new Thread();
            }
            initiateThreads();

        }

        @Override
        public void shutdown() {
            running = false;

        }


        @Override
        public List<Runnable> shutdownNow() {
            return null;
        }

        @Override
        public boolean isShutdown() {
            return false;
        }

        @Override
        public boolean isTerminated() {
            return false;
        }

        @Override
        public boolean awaitTermination(long l, TimeUnit timeUnit) throws InterruptedException {
            return false;
        }

        @Override
        public <T> Future<T> submit(Callable<T> callable) {
            return null;
        }

        @Override
        public <T> Future<T> submit(Runnable runnable, T t) {
            return null;
        }

        @Override
        public Future<?> submit(Runnable runnable) {
            return null;
        }

        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection) throws InterruptedException {
            return null;
        }

        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection, long l, TimeUnit timeUnit) throws InterruptedException {
            return null;
        }

        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> collection) throws InterruptedException, ExecutionException {
            return null;
        }

        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> collection, long l, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
            return null;
        }

        @Override
        public void execute(Runnable runnable) {
            runnables.add(runnable);
        }

        public void initiateThreads() {
            running = true;
            for (int i = 0; i < threads.length; i++) {
                threads[i] = new Thread(() -> {
                    Runnable runnable = () -> {
                        while (running) {

                            Runnable runnable1 = runnables.poll();
                            if (runnable1 != null)
                                runnable1.run();

                        }


                    };
                    runnable.run();

                });
                threads[i].start();

            }

        }
    }

    public static void main(String[] args) {


        // Initialize the list of webpages.
        initialize();

        // Start timing.
        long start = System.nanoTime();

        // Do the work.
        while (printQueue.size() < NUM_WEBPAGES) {
            downloadWebPages();
            analyzeWebPages();
            categorizeWebPages();

        }
        myExecutor.shutdown();


        // Stop timing.
        long stop = System.nanoTime();

        // Present the result.
        presentResult();


        // Present the execution time.
        System.out.println("Execution time (seconds): " + (stop - start) / 1.0E9);

    }
}