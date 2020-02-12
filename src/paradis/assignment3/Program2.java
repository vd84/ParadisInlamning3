// Peter Idestam-Almquist, 2020-02-04.
// Douglas Hammarstam doha6991

// [Do necessary modifications of this file.]

package paradis.assignment3;

// [You are welcome to add some import statements.]

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingQueue;

public class Program2 {
    final static int NUM_WEBPAGES = 40;
    private static List<WebPage> webPages = new ArrayList<>();

    // [You are welcome to add some variables.]

    // [You are welcome to modify this method, but it should NOT be parallelized.]
    private static void initialize() {
        for (int i = 0; i < NUM_WEBPAGES; i++) {
            webPages.add(new WebPage(i, "http://www.site.se/page" + i + ".html"));

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
        }





    public static void main(String[] args) {
        // Initialize the list of webpages.
        initialize();
        // Start timing.
        long start = System.nanoTime();

        webPages.parallelStream().forEach(Program2::doAll);

        // Stop timing.
        long stop = System.nanoTime();

        int numProcessors = Runtime.getRuntime().availableProcessors();
        int parallelism = ForkJoinPool.commonPool().getParallelism();



        // Present the result.
        presentResult();
        System.out.println("Available processors: " + numProcessors);
        System.out.println("Parallelism (threads): " + parallelism);

        // Present the execution time.
        System.out.println("Execution time (seconds): " + (stop - start) / 1.0E9);
    }
}
