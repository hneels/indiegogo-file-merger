# File Merger and Searcher
#### Hope Neels, CS622 Assignment 2

*Note: this project was an assignment for CS622: Advanced Programming Techniques. Copying any portion and submitting it as your own work is a violation of Boston University's Academic Conduct Code and is prohibited.*

### Summary
This Java command-line program sequentially merges files downloaded from https://webrobots.io/indiegogo-dataset, with a maximum of 10,000 lines copied from each file (to keep the file size manageable). The source files and merged file can be of type JSON or CSV. It then searches the resulting merged file for a given keyword, which is chosen as "fitness" for this demonstration. For each record in the merged file in which the keyword is found, that record's line number, "fund_raised_percent" column value, and "close_date" column value are printed to the console. Finally, the total number of records found containing that keyword is printed. The program demonstrates proper file I/O with buffered streams, regular expressions with Pattern and Matcher classes, and exception handling with nested try-catch clauses.

### Required Setup
A directory named "indiegogo" must be placed in the project root directory if it does not exist already, containing JSON and/or CSV files downloaded from the <a href="https://webrobots.io/indiegogo-dataset">indiegogo dataset</a>. While testing this program I downloaded 2 JSON and 3 CSV files. JSON and CSV can exist in the directory simultaneously, because the program will only merge files of the same type each time it runs. To test with different types of input, change the new file on line 180 to "aggregate.csv" or "aggregate.json". Note that if this aggregate file already exists it will be overwritten. The keyword "fitness" on line 190 can also be changed to "robot", "wearable", or any other word.

Run the "Hw2.java" file from your terminal.

### Main method
A new JSON or CSV file is created in the project root. The "indiegogo" directory containing all the files to be merged together is also retrieved as a File object. The mergeFiles method, described below, copies the first 10,000 lines from each file of the same type within the "indiegogo" directory into the new aggregate file. Next, the findKeyword method (described below) is called on the merged file with the keyword "fitness", searching for all records containing that keyword and printing select information retrieved from those records.

### mergeFiles method
This method takes two File objects as arguments. The first (the source) must be a directory for the program to work, so the method will throw IllegalArgumentException if it is not. First, the BufferedReader and BufferedWriter streams are declared above the try-block so they can be properly closed in the finally-block. The method uses buffered text stream classes wrapping the low-level streams to improve efficiency. The method parses the file type of the destination aggregate file and retrieves an array of all files in the source directory. If the destination file is a CSV file then only CSV files in the source directory are copied, and if it's a JSON file only JSON files are copied. The method then copies lines from the input stream into the output stream destination file, stopping when 10,000 lines have been copied from each file or the end of the file is reached. Importantly, one output stream object is used throughout the method so that the output file doesn't get overwritten each time a new input file is copied. The possible IOExceptions are appropriately caught and handled, and the files are closed in the nested finally-block, ensuring that even if an error is thrown when closing the files it will be caught and handled.

### findKeyword method
Now that the files from indiegogo have been merged into one file, that new file is inspected for a given keyword. The findKeyword method takes two parameters, a String keyword to search for and a File object to search within. The File parameter must be a JSON or CSV file in the format of the Indiegogo files available at the URL above. As above, the BufferedReader object is declared above the try-block and initialized within it, so it can be properly closed in the finally-block. As above, the method reads from the input file stream line-by-line. Each line is examined to see if it contains the parameter keyword, ignoring capitalization in the keyword and the record so that "Fitness" will be matched if the keyword is "fitness". The keyword will not match if it is found within a URL or column title. In other words, if the keyword is "robot" then "party for robots" will match but not "robot_id", "my-robot", or "url/robot". If the keyword is matched, the line is parsed based on the file type.

* CSV Files: if the source file is a CSV file (as determined by its extension) the line is split into an array of tokens using regular expressions with negative lookahead/ lookbehind. To ensure the line is appropriately broken up by column, the line is split on "," but NOT "","" (because within the 'tags' column there are some instances of "","" that do not indicate a new field). If the line is improperly formatted and does not contain "fund_raised_percent" and "close_date" fields, it is discarded and the next line is considered. Otherwise, the line's "close_date" field is retrieved from the 4th index of the tokens array and the "fund_raised_percent" field is retrieved from the 7th index of the array.
* JSON files: if the extension of the source file is .json, the "close_date" and "funds_raised_percent" substrings are retrieved with the Pattern and Matcher classes, using regular expressions with positive lookbehind. The "close_date" field is the substring following the characters **close_date":"** up to, but not including, the next quotation mark. The "funds_rased_percent" field is the substring following the phrase **funds_raised_percent":** up to, but not including, the next comma (because this column's value is not enclosed in quotations).

Once this record's "close_date" and "funds_raised_percent" columns have been retrieved based on the file type, they are printed to the console.

The console output looks like this:
<br>Keyword found on line 30
<br>close_date is 2017-03-03T23:59:59-08:00
<br>fund_raised_percent is 0.0809

The total number of records found containing that keyword is printed once processing is complete. Note that this is not the total instances of the word  (which would be a larger number) but rather the total records found, which could each contain multiple instances of said keyword.
As above, the IOExceptions that could be thrown in this process are appropriately handled and the file resources are closed within nested finally-blocks.
