// Peter Idestam-Almquist, 2020-02-04.
// Douglas Hammarstam doha6991

// [Do necessary modifications of this file.]

package paradis.assignment3;

// [You are welcome to add some import statements.]

import java.util.concurrent.*;

public class Program1 {
    final static int NUM_WEBPAGES = 300;
    private static WebPage[] webPages = new WebPage[NUM_WEBPAGES];

    private static BlockingQueue<WebPage> downloadQueue = new LinkedBlockingQueue<WebPage>();
    private static LinkedBlockingQueue<WebPage> analyzeQueue = new LinkedBlockingQueue<WebPage>();
    private static BlockingQueue<WebPage> categorizeQueue = new LinkedBlockingQueue<WebPage>();
    private static ExecutorService executor = ForkJoinPool.commonPool();
    private static LinkedBlockingQueue<WebPage> printQueue = new LinkedBlockingQueue<WebPage>();




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
            executor.execute(download);

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
            executor.execute(analyze);
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
            executor.submit(categorize);
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
        while (printQueue.size() < NUM_WEBPAGES) {
            downloadWebPages();
            analyzeWebPages();
            categorizeWebPages();


        }

        executor.shutdown();


        // Stop timing.
        long stop = System.nanoTime();

        // Present the result.
        presentResult();


        // Present the execution time.
        System.out.println("Execution time (seconds): " + (stop - start) / 1.0E9);

    }
}