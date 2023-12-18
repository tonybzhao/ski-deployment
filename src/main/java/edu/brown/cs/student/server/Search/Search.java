package edu.brown.cs.student.server.Search;

import edu.brown.cs.student.CSVCode.Parsing.CreatorFromRow;
import edu.brown.cs.student.CSVCode.Parsing.FactoryFailureException;
import edu.brown.cs.student.CSVCode.Parsing.Parse;
import edu.brown.cs.student.server.ACS.DatasourceException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Search class is used by end-users to search their CSV files for a specified item. It prints out
 * each row found containing the item, and returns a List<int> containing the row index (0 being
 * first) of the rows containing item Can specify search by column header or index, and can make
 * search require exact case when searching for item.
 *
 * <p>TO USE: Easiest is to run the Main method, which uses scanner to interact with the user in the
 * terminal where they will easily customize search. Could also create a custom main method and
 * create a search object to use
 */
public class Search {

  List<List<String>> parsedCSV;
  String fileName;
  String value;
  ColumnIdentifier.Identifier identifierType;
  String columnIdentifier;
  Boolean hasHeaders;
  Boolean checkCase;

  /**
   * Constructor for search
   *
   * @param fileName String of path to CSV file
   * @param value String to search for
   * @param identifierType ColumnIdentifier.Identifier NONE if column doesn't matter INDEX if search
   *     by column index HEADER if search by column header
   * @param columnIdentifier String of either index or header name. If no Identifier, any String
   *     works
   * @param hasHeaders Boolean if CSV has headers
   * @param checkCase Boolean if value needs to match exact case
   */
  public Search(
      String fileName,
      String value,
      ColumnIdentifier.Identifier identifierType,
      String columnIdentifier,
      Boolean hasHeaders,
      Boolean checkCase) {
    this.fileName = fileName;
    this.value = value;
    this.columnIdentifier = columnIdentifier;
    this.identifierType = identifierType;
    this.hasHeaders = hasHeaders;
    this.checkCase = checkCase;
    this.parsedCSV = null;
  }

  public Search(
      List<List<String>> parsedCSV,
      String value,
      ColumnIdentifier.Identifier identifierType,
      String columnIdentifier,
      Boolean hasHeaders,
      Boolean checkCase) {
    this.fileName = null;
    this.value = value;
    this.columnIdentifier = columnIdentifier;
    this.identifierType = identifierType;
    this.hasHeaders = hasHeaders;
    this.checkCase = checkCase;
    this.parsedCSV = parsedCSV;
  }

  /**
   * Main search method Prints out each row containing the searched value, or that value wasn't
   * found
   *
   * @return List<int> where each int is the index of the rows containing search value Empty list of
   *     value not found
   */
  public List<Integer> search() {
    // Reader to read filepath
    Reader reader = null;

    // initialize return list
    List<Integer> rowsFound = new ArrayList<>();

    // Try to make sure file is valid
    try {
      reader = new FileReader(this.fileName);

      // Basic creator form row that just returns the same List<String>
      CreatorFromRow<List<String>> creator =
          new CreatorFromRow<List<String>>() {
            @Override
            public List<String> create(List<String> row) throws FactoryFailureException {
              return row;
            }
          };

      // Parsing CSV so search can run
      Parse<List<String>> parser = new Parse<>(reader, creator);
      List<List<String>> parsedCSV = parser.parse();

      // If CSV has headers, will start searching at row 1
      int currentRow = 0;
      if (hasHeaders) {
        currentRow = 1;
      }

      // Identiy column to be searched in specified
      int searchColumn = -1;
      if (this.identifierType == ColumnIdentifier.Identifier.HEADER) {
        searchColumn = parsedCSV.get(0).indexOf(this.columnIdentifier);
      } else if (this.identifierType == ColumnIdentifier.Identifier.INDEX) {
        // Try in case inputted index was not a number
        try {
          searchColumn = Integer.parseInt(this.columnIdentifier);
        } catch (NumberFormatException n) {
          System.out.println("Index was not a number.");
          System.exit(0);
        }
      }

      // Search whole CSV
      if (this.identifierType == ColumnIdentifier.Identifier.NONE) {
        Boolean valueFound = false;
        for (int i = currentRow; i < parsedCSV.size(); i++) {
          List<String> row = parsedCSV.get(i);
          if (doesRowContain(row, value)) {
            System.out.println("Row " + i + ": " + row);
            rowsFound.add(i);
            valueFound = true;
          }
        }
        if (valueFound == false) {
          System.out.println("Value was not found.");
        }
      } else {
        // Search specific column
        if (searchColumn == -1) {
          System.out.println("Header was not found.");
        } else if (searchColumn >= parsedCSV.get(0).size()) {
          System.out.println("Column number was greater " + "than number of columns.");
        } else {
          Boolean valueFound = false;
          for (int i = currentRow; i < parsedCSV.size(); i++) {
            List<String> row = parsedCSV.get(i);
            if (doesRowContainAt(row, value, searchColumn)) {
              System.out.println("Row " + i + ": " + row);
              rowsFound.add(i);
              valueFound = true;
            }
          }
          if (valueFound == false) {
            System.out.println("Value was not found.");
          }
        }
      }
    } catch (IOException i) {
      System.out.println("File path not found while searching");
      System.exit(0);
    }
    return rowsFound;
  }

  public List<List<String>> searchParsed() throws DatasourceException {
    // initialize return list
    List<List<String>> rowsFound = new ArrayList<>();

    // If CSV has headers, will start searching at row 1
    int currentRow = 0;
    if (hasHeaders) {
      currentRow = 1;
    }

    // Identiy column to be searched in specified
    int searchColumn = -1;
    if (this.identifierType == ColumnIdentifier.Identifier.HEADER) {
      searchColumn = this.parsedCSV.get(0).indexOf(this.columnIdentifier);
    } else if (this.identifierType == ColumnIdentifier.Identifier.INDEX) {
      // Try in case inputted index was not a number
      try {
        searchColumn = Integer.parseInt(this.columnIdentifier);
      } catch (NumberFormatException n) {
        throw new DatasourceException("Index was not a number");
      }
    }

    // Search whole CSV
    if (this.identifierType == ColumnIdentifier.Identifier.NONE) {
      Boolean valueFound = false;
      for (int i = currentRow; i < this.parsedCSV.size(); i++) {
        List<String> row = this.parsedCSV.get(i);
        if (doesRowContain(row, value)) {
          // System.out.println("Row " + i + ": " + row);
          rowsFound.add(row);
          valueFound = true;
        }
      }
      if (valueFound == false) {
        // System.out.println("Value was not found.");
      }
    } else {
      // Search specific column
      if (searchColumn == -1) {
        throw new DatasourceException("Header was not found");
      } else if (searchColumn >= this.parsedCSV.get(0).size()) {
        throw new DatasourceException("Index was to big");
      } else {
        Boolean valueFound = false;
        for (int i = currentRow; i < this.parsedCSV.size(); i++) {
          List<String> row = this.parsedCSV.get(i);
          if (doesRowContainAt(row, value, searchColumn)) {
            // System.out.println("Row " + i + ": " + row);
            rowsFound.add(row);
            valueFound = true;
          }
        }
        if (valueFound == false) {
          // System.out.println("Value was not found.");
        }
      }
    }
    return rowsFound;
  }

  /**
   * Method to search a row for the item Can be case-sensitive if specified
   *
   * @param row List<String> row to search
   * @param value String value to search for
   * @return Boolean if row contains value
   */
  public boolean doesRowContain(List<String> row, String value) {
    for (String item : row) {
      if (!checkCase) {
        item = item.toLowerCase();
        value = value.toLowerCase();
      }
      if (item.equals(value)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Method to search a row for the item at specific index Can be case-sensitive if specified
   *
   * @param row List<String> row to search
   * @param value String value to search for
   * @param column int of column to be search - will only check corresponding item
   * @return Boolean if row contains value at index
   */
  public boolean doesRowContainAt(List<String> row, String value, int column) {
    String item = "";
    try {
      item = row.get(column);
      if (!checkCase) {
        item = item.toLowerCase();
        value = value.toLowerCase();
      }
      if (item.equals(value)) {
        return true;
      }
    } catch (IndexOutOfBoundsException b) {
      // I won't crash the program since some rows in may have the correct
      // value in that column, however I will supply a warning so user knows
      System.out.println(
          "Possible Warning: Desired column to " + "search in was missing some data.");
    }
    return false;
  }
}
