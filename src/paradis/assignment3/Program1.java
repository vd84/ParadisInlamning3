
// Peter Idestam-Almquist, 2020-02-04.
// Douglas Hammarstam doha6991

// [Do necessary modifications of this file.]

package paradis.assignment3;

// [You are welcome to add some import statements.]

import java.util.concurrent.*;

public class Program1 {
    final static int NUM_WEBPAGES = 40;
    private static WebPage[] webPages = new WebPage[NUM_WEBPAGES];

    private static BlockingQueue<WebPage> downloadQueue = new ArrayBlockingQueue<WebPage>(1000000);
    private static BlockingQueue<WebPage> analyzeQueue = new ArrayBlockingQueue<WebPage>(10000000);
    private static BlockingQueue<WebPage> categorizeQueue = new ArrayBlockingQueue<WebPage>(1000000);
    private static ExecutorService e = new ThreadPoolExecutor(1, 4, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(40));


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
    private static void downloadWebPages() {

        try {

            while (true) {
                //System.out.println("running download");
                e.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            WebPage webPage;
                            webPage = downloadQueue.take();

                            webPage.download();
                            analyzeQueue.add(webPage);
                        } catch (
                                InterruptedException e) {
                            e.printStackTrace();
                        }

                    }

                });


            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }


    // [Do modify this sequential part of the program.]
    private static void analyzeWebPages() {
        try {

            while (true) {
                //System.out.println("running download");
                e.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            WebPage webPage;
                            webPage = analyzeQueue.take();

                            webPage.analyze();
                            categorizeQueue.add(webPage);
                        } catch (
                                InterruptedException e) {
                            e.printStackTrace();
                        }

                    }

                });


            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }

    // [Do modify this sequential part of the program.]
    private static void categorizeWebPages() {
        try {

            while (true) {
                //System.out.println("running download");
                e.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            WebPage webPage;
                            webPage = categorizeQueue.take();

                            webPage.categorize();
                        } catch (
                                InterruptedException e) {
                            e.printStackTrace();
                        }

                    }

                });


            }
        } catch (
                Exception e) {
            e.printStackTrace();
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
        categorizeWebPages();


        // Stop timing.
        long stop = System.nanoTime();

        // Present the result.
        presentResult();


        // Present the execution time.
        System.out.println("Execution time (seconds): " + (stop - start) / 1.0E9);

        if (analyzeQueue.size() <= 0){
            System.
        }

    }
}
