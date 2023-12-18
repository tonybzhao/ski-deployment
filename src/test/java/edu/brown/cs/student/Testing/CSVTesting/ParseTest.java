package edu.brown.cs.student.Testing.CSVTesting;

import static org.testng.AssertJUnit.assertEquals;

import edu.brown.cs.student.CSVCode.Parsing.CreatorFromRow;
import edu.brown.cs.student.CSVCode.Parsing.FactoryFailureException;
import edu.brown.cs.student.CSVCode.Parsing.Parse;
import edu.brown.cs.student.Testing.CSVTesting.Dimensions.DimensionsCreatorFromRow;
import edu.brown.cs.student.Testing.CSVTesting.Dimensions.DimensionsTestClass;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/** Class to test the parse method */
public class ParseTest {
  Parse<List<String>> evenNormalParser = null;
  Parse<List<String>> stringParser = null;
  Parse<List<String>> unevenNormalParser = null;
  Parse<DimensionsTestClass> evenDimParser = null;
  Parse<DimensionsTestClass> unevenDimParser = null;
  Parse<DimensionsTestClass> nonIntDimParser = null;

  @Before
  public void setup() {
    // reader with even csv
    Reader evenReader = null;
    try {
      evenReader =
          new FileReader(
              "/Users/tyype1/Desktop/Brown/sem3/CS32"
                  + "/Projects/csv-tyypezza/data/census/even.csv");
    } catch (IOException i) {
      System.out.println("Was not a proper file path");
    }

    // reader with even csv but string
    Reader stringReader = null;
    String string =
        "Height,Weight,Length,Width\n" + "51,54,32,31\n" + "12,13,14,89\n" + "100,165,122,12";
    stringReader = new StringReader(string);

    // reader with uneven csv
    Reader unevenReader = null;
    try {
      unevenReader =
          new FileReader(
              "/Users/tyype1/Desktop/Brown/sem3/CS32"
                  + "/Projects/csv-tyypezza/data/census/uneven.csv");
    } catch (IOException i) {
      System.out.println("Was not a proper file path");
    }

    // reader with notint csv
    Reader nonIntReader = null;
    try {
      nonIntReader =
          new FileReader(
              "/Users/tyype1/Desktop/Brown/sem3/CS32"
                  + "/Projects/csv-tyypezza/data/census/not_int_dim.csv");
    } catch (IOException i) {
      System.out.println("Was not a proper file path");
    }

    // basic creator that just returns the row
    CreatorFromRow<List<String>> creator =
        new CreatorFromRow<List<String>>() {
          @Override
          public List<String> create(List<String> row) throws FactoryFailureException {
            return row;
          }
        };

    // dimensions creator
    DimensionsCreatorFromRow dimCreator = new DimensionsCreatorFromRow();

    evenNormalParser = new Parse<List<String>>(evenReader, creator);
    stringParser = new Parse<List<String>>(stringReader, creator);
    unevenNormalParser = new Parse<List<String>>(unevenReader, creator);
    evenDimParser = new Parse<DimensionsTestClass>(evenReader, dimCreator);
    unevenDimParser = new Parse<DimensionsTestClass>(unevenReader, dimCreator);
    nonIntDimParser = new Parse<DimensionsTestClass>(nonIntReader, dimCreator);
  }

  // Basic test for parse with file reader
  @Test
  public void testParseFileReader() {
    List<String> row1 = List.of("Height", "Weight", "Length", "Width");
    List<String> row2 = List.of("51", "54", "32", "31");
    List<String> row3 = List.of("12", "13", "14", "89");
    List<String> row4 = List.of("100", "165", "122", "12");

    List<List<String>> expected = List.of(row1, row2, row3, row4);

    assertEquals(expected, stringParser.parse());
  }

  // Basic test for parse with string reader
  @Test
  public void testParseStringReader() {
    List<String> row1 = List.of("Height", "Weight", "Length", "Width");
    List<String> row2 = List.of("51", "54", "32", "31");
    List<String> row3 = List.of("12", "13", "14", "89");
    List<String> row4 = List.of("100", "165", "122", "12");

    List<List<String>> expected = List.of(row1, row2, row3, row4);

    assertEquals(expected, evenNormalParser.parse());
  }

  // Test for parse with uneven csv
  @Test
  public void testParseUneven() {
    List<String> row1 = List.of("Height", "Weight", "Length", "Width");
    List<String> row2 = List.of("51", "54", "32", "31");
    List<String> row3 = List.of("12", "13", "14");
    List<String> row4 = List.of("100", "165", "122", "12");

    List<List<String>> expected = List.of(row1, row2, row3, row4);

    assertEquals(expected, unevenNormalParser.parse());
  }

  // Test with custom CreaorFromRow that works
  @Test
  public void testParseDim() {
    DimensionsTestClass row1 = new DimensionsTestClass();
    DimensionsTestClass row2 = new DimensionsTestClass(51, 54, 32, 31);
    DimensionsTestClass row3 = new DimensionsTestClass(12, 13, 14, 89);
    DimensionsTestClass row4 = new DimensionsTestClass(100, 165, 122, 12);

    List<DimensionsTestClass> expected = List.of(row1, row2, row3, row4);
    assertEquals(expected, evenDimParser.parse());
  }

  // Test with custom CreatorFromRow that has an error parsing the row
  // In this case, custom Creator needs row of 4 items, but is given 3
  // So return an empty list and prints a message
  @Test
  public void testParseDimUneven() {
    assertEquals(Collections.emptyList(), unevenDimParser.parse());
  }

  // Test with custon Creator that has trouble parsing
  // In this case, custon Creator has to parse data to ints, but given string
  // that can't be
  @Test
  public void testParseDimWrongType() {
    assertEquals(Collections.emptyList(), nonIntDimParser.parse());
  }
}
