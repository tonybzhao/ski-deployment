package edu.brown.cs.student.CSVCode.main;

import edu.brown.cs.student.CSVCode.Parsing.CreatorFromRow;
import edu.brown.cs.student.CSVCode.Parsing.FactoryFailureException;
import edu.brown.cs.student.CSVCode.Parsing.Parse;
import edu.brown.cs.student.server.Search.ColumnIdentifier;
import edu.brown.cs.student.server.Search.Search;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Scanner;

/**
 * I wrote my main class as the interface for the end-users who want to search It uses the scanner
 * class and terminal for an easy and interactive way to search in their CSVs.
 */
public final class Main {
  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private Main(String[] args) {}

  /** Run is where the entire scanner class interface runs */
  private void run() {

    Scanner myScanner = new Scanner(System.in);
    System.out.println(
        "Hello user. You have started the SEARCH portion of this CSV code.\n"
            + "This will print out any rows found containing the item "
            + "you are searching for. \nRow 0 is considered the first row, "
            + "and the "
            + "headers row is counted, \nso for a CSV with headers the first  "
            + "row will be 1.\n\n");

    Boolean searchAgain = true;
    // Loop gives user option to keep searching without restarting program
    while (searchAgain) {
      String file = "";
      String value = "";
      ColumnIdentifier.Identifier columnIdentifier = ColumnIdentifier.Identifier.NONE;
      String identifier = "";
      Boolean hasHeaders = false;
      Boolean checkCase = true;

      // File path
      Boolean fileDone = false;
      Reader reader = null;
      System.out.println("START:\nEnter the path to the CSV file you would " + "like to search.");
      while (fileDone == false) {
        file = myScanner.nextLine().toLowerCase();
        if (file.equals("exit")) {
          System.out.println("You exited the program.");
          System.exit(0);
        }
        try {
          reader = new FileReader(file);
          break;
        } catch (IOException i) {
          System.out.println(
              "Was not a proper file path. Please enter again. "
                  + "If you want to exit, enter exit.");
        }
      }

      // Have headers?
      Boolean headerDone = false;
      System.out.println("Does your CSV have headers? (yes/no)");
      while (headerDone == false) {
        String header = myScanner.nextLine().toLowerCase();
        if (header.equals("yes")) {
          hasHeaders = true;
          break;
        } else if (header.equals("no")) {
          break;
        }
        System.out.println("Please enter 'yes' or 'no'.");
      }

      boolean sameFile = true;
      // Allows user to keep searching same file without reentering file path
      while (sameFile) {

        // Value
        System.out.println("Enter the item you would like to search for.");
        value = myScanner.nextLine();

        // Check Case
        Boolean caseDone = false;
        // User can check for case if desired
        System.out.println(
            "Do you want SEARCH to check for upper/lower case of your "
                + "searched item? (yes/no)");
        while (caseDone == false) {
          String check = myScanner.nextLine().toLowerCase();
          if (check.equals("no")) {
            checkCase = false;
            break;
          } else if (check.equals("yes")) {
            break;
          }
          System.out.println("Please enter 'yes' or 'no'.");
        }

        // Can search in a certain column
        System.out.println(
            "Do you want to search for "
                + value
                + " only in a certain column? If not"
                + " the whole CSV will be searched. (yes/no)");

        Boolean columnDone = false;
        while (columnDone == false) {
          String answer = myScanner.nextLine().toLowerCase();
          if (answer.equals("yes")) {
            // Can specify column by index or header
            System.out.println(
                "Would like to to specify the column by column number or "
                    + "header name? (number/header)");
            Boolean choiceDone = false;
            while (choiceDone == false) {
              String type = myScanner.nextLine().toLowerCase();
              if (type.equals("number")) {
                columnIdentifier = ColumnIdentifier.Identifier.INDEX;

                // Column number
                System.out.println(
                    "What column number would you like to search? " + "(0 is first column)");
                while (!myScanner.hasNextInt()) {
                  System.out.println("Please enter a number.");
                  myScanner.next();
                }
                identifier = myScanner.nextLine();
                break;

              } else if (type.equals("header")) {
                columnIdentifier = ColumnIdentifier.Identifier.HEADER;

                // Header name
                System.out.println("What header name would you like to " + "search?");
                identifier = myScanner.nextLine();
                break;
              }
              System.out.println("Please enter 'column' or 'header'.");
            }
            break;

          } else if (answer.equals("no")) {
            break;
          }
          System.out.println("Please enter 'yes' or 'no'.");
        }

        System.out.println("All set. Searching now...");

        CreatorFromRow<List<String>> creator =
            new CreatorFromRow<List<String>>() {
              @Override
              public List<String> create(List<String> row) throws FactoryFailureException {
                return row;
              }
            };

        Parse<List<String>> parse = new Parse<List<String>>(reader, creator);
        ColumnIdentifier column = new ColumnIdentifier();
        Search mySearch =
            new Search(file, value, columnIdentifier, identifier, hasHeaders, checkCase);
        mySearch.search();

        Boolean finalDone = false;
        // User can search again
        System.out.println("Would you like to search again?");
        while (finalDone == false) {
          String again = myScanner.nextLine().toLowerCase();
          if (again.equals("no")) {
            searchAgain = false;
            sameFile = false;
            break;
          } else if (again.equals("yes")) {
            Boolean sameFileDone = false;
            // User can search in same file without reentering path
            System.out.println("Would you like to search the same CSV file?");
            while (sameFileDone == false) {
              String same = myScanner.nextLine().toLowerCase();
              if (same.equals("no")) {
                sameFile = false;
                break;
              } else if (same.equals("yes")) {
                break;
              }
              System.out.println("Please enter 'yes' or 'no'.");
            }
            break;
          }
          System.out.println("Please enter 'yes' or 'no'.");
        }
      }
    }
    System.out.println("Search finished. Run again to search again.");
  }
}
