// Peter Idestam-Almquist, 2020-02-04.
// Douglas Hammarstam doha6991

// [Do necessary modifications of this file.]

package paradis.assignment3;

// [You are welcome to add some import statements.]

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class Program2 {
    final static int NUM_WEBPAGES = 40;
    private static List<WebPage> webPages = new ArrayList<>();
    private static LinkedBlockingQueue<WebPage> printQueue = new LinkedBlockingQueue<WebPage>();

    // [You are welcome to add some variables.]

    // [You are welcome to modify this method, but it should NOT be parallelized.]
    private static void initialize() {
        for (int i = 0; i < NUM_WEBPAGES; i++) {
            WebPage webPage = new WebPage(i, "http://www.site.se/page" + i + ".html");
            webPages.add(webPage);

        }
    }

    // [Do modify this sequential part of the program.]
    private static void downloadWebPages() {
        for (int i = 0; i < NUM_WEBPAGES; i++) {
            webPages.get(i).download();
        }
    }

    // [Do modify this sequential part of the program.]
    private static void analyzeWebPages() {
        for (int i = 0; i < NUM_WEBPAGES; i++) {
            webPages.get(i).analyze();
        }
    }

    // [Do modify this sequential part of the program.]
    private static void categorizeWebPages() {
        for (int i = 0; i < NUM_WEBPAGES; i++) {
            webPages.get(i).categorize();
        }
    }

    // [You are welcome to modify this method, but it should NOT be parallelized.]
    private static void presentResult() {
        for (int i = 0; i < NUM_WEBPAGES; i++) {
            System.out.println(webPages.get(i));
        }
    }



        private static void doAll(WebPage webPage) {
            webPage.download();
            webPage.analyze();
            webPage.categorize();
            printQueue.add(webPage);
        }





    public static void main(String[] args) {
        // Initialize the list of webpages.
        initialize();


        // Start timing.
        long start = System.nanoTime();

        webPages.parallelStream().forEach(Program2::doAll);


        // Do the work.
        //downloadWebPages();
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
