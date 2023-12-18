package edu.brown.cs.student.Testing.CSVTesting.Dimensions;

import edu.brown.cs.student.CSVCode.Parsing.CreatorFromRow;
import edu.brown.cs.student.CSVCode.Parsing.FactoryFailureException;
import edu.brown.cs.student.CSVCode.Parsing.Parse;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public final class DemoParse {

  public static void main(String[] args) {

    Reader evenReader = null;
    try {
      evenReader =
          new FileReader(
              "/Users/tyype1/Desktop/Brown/sem3/CS32/"
                  + "Projects/csv-tyypezza/data/census/"
                  + "dol_ri_earnings_disparity.csv");
    } catch (IOException i) {
      System.out.println("Was not a proper file path");
    }

    Reader riReader = null;
    try {
      riReader =
          new FileReader(
              "/Users/tyype1/Desktop/Brown/sem3/CS32/Projects/csv-tyypezza/data/census/ri.csv");
    } catch (IOException i) {
      System.out.println("Was not a proper file path");
    }

    Reader stringReader =
        new StringReader(
            "Height,Weight,Length,Width\n" + "51,54,32,31\n" + "12,13,14,89\n" + "100,165,122,12");

    Reader dimReader = null;
    try {
      dimReader =
          new FileReader(
              "/Users/tyype1/Desktop/Brown/sem3/CS32"
                  + "/Projects/csv-tyypezza/data/census/even.csv");
    } catch (IOException i) {
      System.out.println("Was not a proper file path");
    }

    // Basic creator
    CreatorFromRow<List<String>> creator =
        new CreatorFromRow<List<String>>() {
          @Override
          public List<String> create(List<String> row) throws FactoryFailureException {
            return row;
          }
        };

    // dimensions creator
    DimensionsCreatorFromRow dimCreator = new DimensionsCreatorFromRow();

    Parse<List<String>> evenParse = new Parse<List<String>>(evenReader, creator);
    Parse<List<String>> stringParse = new Parse<List<String>>(stringReader, creator);
    Parse<DimensionsTestClass> evenDimParser =
        new Parse<DimensionsTestClass>(dimReader, dimCreator);
    Parse<List<String>> riParse = new Parse<>(riReader, creator);

    //        for(DimensionsTestClass s : evenDimParser) {
    //            System.out.println(s);
    //        }
    //
    //        for(List<String> s : evenParse) {
    //            System.out.println(s);
    //        }

    for (List<String> s : riParse) {
      System.out.println(s);
    }
  }
}
