// Peter Idestam-Almquist, 2020-02-04.
// Douglas Hammarstam doha6991

// [Do necessary modifications of this file.]

package paradis.assignment3;

// [You are welcome to add some import statements.]

import com.sun.security.jgss.GSSUtil;

import java.util.concurrent.*;

public class Program1 {
    final static int NUM_WEBPAGES = 40;
    private static WebPage[] webPages = new WebPage[NUM_WEBPAGES];

    private static BlockingQueue<WebPage> downloadQueue = new LinkedBlockingQueue<WebPage>();
    private static LinkedBlockingQueue<WebPage> analyzeQueue = new LinkedBlockingQueue<WebPage>();
    private static BlockingQueue<WebPage> categorizeQueue = new LinkedBlockingQueue<WebPage>();
    private static ExecutorService downloadEx = ForkJoinPool.commonPool();
    private static ExecutorService analyzeEx = ForkJoinPool.commonPool();
    private static ExecutorService categorizeEx = ForkJoinPool.commonPool();

    private static volatile Object lock = new Object();


    //private static BlockingQueue<WebPage> queue = new ArrayBlockingQueue<WebPage>(NUM_WEBPAGES);


    // [You are welcome to add some variables.]

    // [You are welcome to modify this method, but it should NOT be parallelized.]
    private static void initialize() {
        for (int i = 0; i < NUM_WEBPAGES; i++) {
            webPages[i] = new WebPage(i, "http://www.site.se/page" + i + ".html");
            downloadQueue.add(webPages[i]);


        }
    }

    // [Do modify this sequential part of the program.]
    private static void downloadWebPages() throws InterruptedException {

        for (;;) {
            System.out.println("Download");

            System.out.println(downloadQueue.size());
            downloadEx.submit(() -> {
                WebPage webPage = null;
                try {
                    webPage = downloadQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                assert webPage != null;
                webPage.download();
                analyzeQueue.add(webPage);



            });


        }

    }


    // [Do modify this sequential part of the program.]
    private static void analyzeWebPages() throws InterruptedException {


        for (;;) {

            System.out.println(analyzeQueue.size());
            analyzeEx.submit(() -> {
                WebPage webPage = null;
                try {
                    webPage = analyzeQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                assert webPage != null;
                webPage.analyze();
                categorizeQueue.add(webPage);


            });

        }
    }

    // [Do modify this sequential part of the program.]
    private static void categorizeWebPages() {


        for (;;) {

            System.out.println(categorizeQueue.size());
            categorizeEx.submit(() -> {
                WebPage webPage = null;
                try {
                    webPage = categorizeQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                assert webPage != null;
                webPage.categorize();


            });

        }
    }

    // [You are welcome to modify this method, but it should NOT be parallelized.]
    private static void presentResult() {

        for (int i = 0; i < NUM_WEBPAGES; i++) {
            System.out.println(webPages[i]);
        }


    }

    public static void main(String[] args) throws InterruptedException {


        // Initialize the list of webpages.
        initialize();

        // Start timing.
        long start = System.nanoTime();


        // Do the work.


        downloadWebPages();

        analyzeWebPages();
        //categorizeWebPages();


        // Stop timing.
        long stop = System.nanoTime();

        // Present the result.
        presentResult();


        // Present the execution time.
        System.out.println("Execution time (seconds): " + (stop - start) / 1.0E9);

    }
}