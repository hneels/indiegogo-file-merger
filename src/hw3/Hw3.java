/**
 * Class to test the methods in FileSearcher.java class.
 * This main method creates a new JSON or CSV file and a new FileSearcher instance, calls FileSearcher.mergeFiles
 * to merge source files into one destination file, then calls FileSearcher.findKeyword on different search keywords.
 * Finally, it calls FileSearcher.printSearches to show the history of all search queries, including result and timestamp
 * from each search, and frequency of usage for each keyword.
 * @author Hope Neels
 */
package hw3;

import java.io.File;

public class Hw3 {

    public static void main(String[] args) {

        // create new CSV or JSON file to copy others into in project root with relative path
        // change this to "aggregate.json" or "aggregate.csv" to try out both file types
        File aggregate = new File("aggregate.csv");

        // get the "indiegogo" folder from project root
        File sourceFolder = new File("indiegogo");
        System.out.println("indiegogo folder exists? " + sourceFolder.exists());

        FileSearcher fileSearcher = new FileSearcher();

        // call helper to merge all files from indiegogo folder into destination aggregate file
        fileSearcher.mergeFiles(sourceFolder, aggregate);

        // all keywords to search for: 10 total searches but 7 distinct keywords
        String[] keywords = {"fitness", "wearable", "robot", "food", "holistic", "beauty", "kitten", "wearable", "robot", "wearable"};

        // find all records with each keyword in the merged file
        for (String keyword : keywords) {
            fileSearcher.findKeyword(keyword, aggregate);
        }

        // print the search queries, using FileSearcher method that retains data in an ArrayList and HashMap
        fileSearcher.printSearches();
    }
}
