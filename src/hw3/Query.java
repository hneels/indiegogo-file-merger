/**
 * A Query object represents a single search query within the FileSearcher class. This encapsulates data about a query's
 * keyword, result returned from searching text file, and timestamp of the search. Query objects are stored
 * within the FileSearcher class's queryList ArrayList field.
 * @author Hope Neels
 */
package hw3;

import java.util.Date;

public class Query {

    // the keyword, timestamp, and result will never change after the search is completed

    // the String keyword
    private final String keyword;
    // the number of records this keyword matched in the file
    private final int result;
    // the time of the search
    private final Date timestamp;

    // constructor initializes all final fields
    public Query(String keyword, int result, Date timestamp) {
        this.keyword = keyword;
        this.result = result;
        this.timestamp = timestamp;
    }

    // getters
    public String getKeyword() {
        return keyword;
    }
    public int getResult() {
        return result;
    }
    public Date getTimestamp() {
        return timestamp;
    }

    // toString method used within FileSearcher.PrintSearches(), using String.format as shown in textbook
    @Override
    public String toString() {
        return String.format("%-20s %-20d %-30s", this.keyword, this.result, this.timestamp);
    }

    // two search terms are equal if they have the same keyword, result, and timestamp. this method is not used in this
    // program but just included for good design
    @Override
    public boolean equals(Object other) {
        // if they are literally the same object return true
        if (this == other) {
            return true;
        }

        // if other is not a Query return false
        if (!(other instanceof Query)) {
            return false;
        }

        // other is a Query object, so downcast and compare fields accordingly
        Query o = (Query) other;
        return (o.keyword.equals(this.keyword) && o.timestamp.equals(this.timestamp) && (o.result == this.result));
    }
}
