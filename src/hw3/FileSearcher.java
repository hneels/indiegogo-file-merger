/*** CS622 Assignment 3
 * This FileSearcher class contains utility methods to merge files downloaded from https://webrobots.io/indiegogo-dataset
 * into one file, search that file for a given keyword, and print data about the frequency & timestamp of all search queries.
 * @author Hope Neels
 */


package hw3;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileSearcher {

    // NEW: ArrayList field to hold individual search queries
    private ArrayList<Query> queryList;
    // NEW: HashMap field to hold frequency of keyword usage
    private HashMap<String, Integer> frequencyMap;


    // NEW: Constructor that initializes new empty ArrayList and HashMap to store data about searches
    public FileSearcher() {
        this.queryList = new ArrayList<>();
        this.frequencyMap = new HashMap<>();
    }

    /**
     * mergeFiles method merges all csv or json files in a directory into one output file.
     * It only copies the first 3,000 lines from each file to keep the destination file size manageable
     *
     * @param sourceFolder a File object that is a directory containing one or more csv or json files
     * @param destinationFile a csv/ json File object that will contain the first 3,000 lines of each file in sourceFolder
     *                        after this method completes. (if destinationFile starts with any content it will be overwritten.)
     * @throws IllegalArgumentException if the first parameter sourceFolder is not a directory
     */
    public void mergeFiles(File sourceFolder, File destinationFile) throws IllegalArgumentException {

        // declare streams so they can be closed in finally block
        BufferedReader input = null;
        BufferedWriter output = null;

        try {
            // make sure the source File is a directory
            if (!sourceFolder.isDirectory()) throw new IllegalArgumentException("Source must be a directory.");
            // keep the same output stream for all input files so the destination file doesn't get overwritten
            output = new BufferedWriter(new FileWriter(destinationFile));

            // get the file extension type for the destination file (.json or .csv)
            String fileName = destinationFile.getName();
            String fileType = fileName.substring(fileName.lastIndexOf('.'));
            System.out.println("File type is " + fileType);

            // iterate through all files in the directory, if there are any
            File[] files = sourceFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    // only process input files whose type matches the output file
                    if (file.getName().endsWith(fileType)) {
                        System.out.println("Copying file " + file.getName());

                        // initialize buffered input stream for each file
                        input = new BufferedReader(new FileReader(file));

                        // read each line from input stream and copy to output stream
                        int linesRead = 0;
                        String nextLine;
                        // copy 3,000 max lines from each file to keep the file size manageable
                        while (((nextLine = input.readLine()) != null) && (linesRead < 3_000)) {
                            output.write(nextLine);
                            output.newLine();
                            linesRead++;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // nested try-catch as shown in lecture 2 example 4
            try {
                // close the resources even if an exception occurred
                if (input != null) input.close();
                if (output != null) output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * findKeyword method searches for instances of a given String keyword within a File object.
     * For each record found with matching keyword, the record's line number, close_date and fund_raised_percent attributes
     * are printed to console. At the end, the number of total records containing that keyword is printed and the search query
     * data is saved to this class's ArrayList and HashMap collections for later printing.
     * @param keyword a String keyword to search for within the file
     * @param sourceFile a csv File object containing comma-separated data downloaded from https://webrobots.io/indiegogo-dataset
     */
    public void findKeyword(String keyword, File sourceFile) {
        // declare stream so it can be properly closed in finally block
        BufferedReader fileReader = null;

        try {
            // convert keyword to lowercase to properly search for it within each line
            keyword = keyword.toLowerCase();
            System.out.println("\nSearching file for keyword '" + keyword + "'...");

            // new buffered input stream from source file
            fileReader = new BufferedReader(new FileReader(sourceFile));

            // get the file extension type for the source file (.json or .csv)
            String fileName = sourceFile.getName();
            String fileType = fileName.substring(fileName.lastIndexOf('.'));

            // regex patterns (used below) compiled here outside the loop for increased efficiency
            // regex for JSON files: gets a substring from after close_date":" up to the next quotation mark, with positive lookbehind
            Pattern patternDate = Pattern.compile("(?<=close_date\":\")[^\"]+");
            // regex for JSON files: gets a substring from after funds_raised_percent": up to the next comma, with positive lookbehind
            Pattern patternFunds = Pattern.compile("(?<=funds_raised_percent\":)[^,]+");
            //  regex to find the keyword. per instructions, check for exact word but not substrings in URLS or column titles
            // e.g. if keyword is "robot", then "robots" should match but NOT "robot_id", "something-robot" or "/robot/something"
            Pattern patternKeyword = Pattern.compile("(?<![/\\-_])" + keyword + "(?![/\\-_])");

            // read each line from file, one by one
            String nextLine;
            int lineNumber = 1;
            int keywordCount = 0;
            while ((nextLine = fileReader.readLine()) != null) {

                // check if keyword exists in lowercase version of this line, using regex explained above
                Matcher matcherKeyword = patternKeyword.matcher(nextLine.toLowerCase());
                if (matcherKeyword.find()) {

                    // fields to be retrieved and printed below
                    String closeDate = "";
                    String fundPercent = "";

                    // if destination is a CSV file
                    if (fileType.equals(".csv")) {
                        // split the line on field separators "," but not instances of "","" that are found within 'tags' field
                        String[] lineTokens = nextLine.split("(?<!\")\",\"(?!\")");

                        // if the line is malformed, skip to the next iteration
                        if (lineTokens.length < 8) {
                            lineNumber++;
                            continue;
                        }
                        // get close_date from index 4 column
                        closeDate = lineTokens[4];
                        // get fund_raised_percent from index 7 column
                        fundPercent = lineTokens[7];

                    } else {
                        // destination is a JSON file

                        // find match between the "closed_date" field regex (explained above) and nextLine, as shown in lecture 2
                        Matcher matcherDate = patternDate.matcher(nextLine);
                        // get the matched substring for close_date field and save it for printing below
                        if (matcherDate.find()) closeDate = matcherDate.group();

                        // get match between the "funds_raised_percent" regex (explained above) and nextLine
                        Matcher matcherFunds = patternFunds.matcher(nextLine);
                        // get the matched substring for funds_raised_percent field and save it for printing below
                        if (matcherFunds.find()) fundPercent = matcherFunds.group();
                    }

                    // NEW: deleted print statements for each individual line here, for simplicity of output for HW3

                    keywordCount++;
                }
                lineNumber++;
            }
            System.out.println("Total records that contain keyword '" + keyword + "' : " + keywordCount);

            // NEW: save the search keyword, result, and timestamp to the queryList ArrayList field
            queryList.add(new Query(keyword, keywordCount, new Date()));

            // NEW: update frequency of this keyword's usage in frequencyMap HashMap field
            Integer result = frequencyMap.get(keyword);
            // if this keyword has already been searched, increment its frequency, otherwise set frequency to 1
            result = (result == null) ? 1 : ++result;
            frequencyMap.put(keyword, result);


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // close resources in finally-block, using nested try-catch as shown in lecture
            try {
                if (fileReader != null) fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /** NEW method that prints to the console the history of all keyword searches, including keyword phrase,
     * result from the file, timestamp of the search, and frequency of keyword usage
     * This method is called from Hw3.java after all keywords have been searched for.
     */
    public void printSearches() {
        // print the search history column headers with proper formatting
        System.out.println("\nSearch History: " + queryList.size() + " searches");
        System.out.println("----------------------------------------------------------------------");
        System.out.printf("%-20s %-20s %-30s %n", "Keyword", "Result", "Timestamp");
        System.out.println("----------------------------------------------------------------------");

        // print the history of searches with timestamps saved in the queryList ArrayList
        for (Query query : queryList) {
            System.out.println(query);
        }

        // print the frequency of keywords and total number of keywords searched, saved in frequencyMap HashMap
        System.out.println("----------------------------------------------------------------------");
        System.out.println("Keyword Frequency:");
        // trim the curly braces from the frequencyMap string representation
        String frequencies = frequencyMap.toString();
        frequencies = frequencies.substring(1, frequencies.length() - 1);
        System.out.println(frequencies);
        // total keywords without duplicates
        System.out.println("Total keywords: " + frequencyMap.size());

    }
}

