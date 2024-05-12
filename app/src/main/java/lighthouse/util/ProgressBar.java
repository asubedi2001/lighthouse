package lighthouse.util;

import java.util.Collections;

/**
 * A simple static class to handle the printing of a progress bar
 */
public class ProgressBar {
    /** 
     * Percentage at which to display tick marks on the progress bar.
     */
    public static final double TICKMARK_PCT = .1;

    /**
     * Private default constructor for creation of a ProgressBar object
     */
    private ProgressBar() {
    }

    /**
     * Handle printing the progress bar based off of index loading percentage
     * @param counter number of files processed
     * @param totalDocuments total number of files to be processed
     */
    public static void printProgressBar(int counter, int totalDocuments) {
        // loaded_pct = round((counter / file_count) * 10)
        // to_load_pct = 10 - loaded_pct
        // progress_bar = "\r[" + "█" * loaded_pct + " " * to_load_pct + "] "
        // progress_pct = "0%" if loaded_pct == 0 else f"{loaded_pct}0%"
        // print(progress_bar + progress_pct, end="")

        // compute the scaled percentage of loaded vs toLoad
        long loadedPct = Math.round(((double) counter / totalDocuments) * 10);
        long toLoadPct = 10 - loadedPct;

        String progressBar = "\r[";
        progressBar += String.join("", Collections.nCopies((int) loadedPct, "█"));
        progressBar += String.join("", Collections.nCopies((int) toLoadPct, " "));
        progressBar += "] ";

        String progressPct;
        if (loadedPct == 0) {
            progressPct = "0%";
        } else {
            progressPct = loadedPct + "0%";
        }

        System.out.print(progressBar + progressPct);
    }
}
