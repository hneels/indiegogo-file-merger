# Collection Storing Search Terms
#### Hope Neels
#### CS622 Assignment 3

This assignment builds on and updates Assignment 2 (in branch master).

### Query Class
The new Query class encapsulates data about an individual search query, retaining the search's keyword String, result of matching records found in the file, and unique timestamp of the search. Its instance variables are final since search data does not change once the search is submitted. Its toString() method is used within the FileSearcher class for printing the Collection results (see below) and its equals() method is not used in this program but is included for good design.

### FileSearcher class 
The code from last week's Hw2 has been refactored into the FileSearcher class for better encapsulation. The formerly static methods of this class are now instance methods, so that it can retain an ArrayList and HashMap of search data and so that a FileSearcher can be instantiated to call methods from the Hw3 class (see below). All the new code in this class (updated from Hw2) is commented with **//NEW** for clarity.

- The class now has a "queryList" Collection that retains data about each search. This is represented as an ArrayList of Query objects. 
- It also now has a "frequencyMap" HashMap that maintains the frequency of keyword usage, with the searched String as a key and the number of times it has been searched as the value.
- As an alternate design choice, a single ArrayList or HashMap could have been used for all this data, but I chose to demonstrate two different types of Collections, one with String/Integer values and one with custom objects, to show proficiency for the assignment.
- The new constructor initializes both of these Collections.
- The mergeFiles() method now copies only 3,000 lines instead of 10,000
- The findKeyword() method now saves query data to the ArrayList and HashMap when each search is complete. It no longer prints column data for each individual line number in which the keyword is found, since that isn't the focus of Assignment 3.
- The new printSearches() method prints to the console the history of searches. First it prints the data from the query ArrayList, including the total number of searches and formatted columns for each keyword, result from the file, and timestamp of the search. It then prints the data from the frequency HashMap, including the frequency of each keyword and total number of keywords used.


### Hw3 Class
This class contains only a main method that demonstrates the methods of FileSearcher class. The functionality of Hw2 is retained, and will still work whether you merge JSON or CSV files. A new FileSearcher object is instantiated, and the CSV or JSON files are merged as before. Next, it iterates through an array of 10 keywords, some of which are duplicates to demonstrate the "keyword frequency" feature of the program. Once these ten searches for seven unique keywords are completed, it calls the printSearches method described above to print all the search data retained by the ArrayList and HashMap in the FileSearcher class.