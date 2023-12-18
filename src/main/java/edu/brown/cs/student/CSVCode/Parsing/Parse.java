package edu.brown.cs.student.CSVCode.Parsing;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;

/**
 * Parse class is used for developers to parse their CSV into a list of objects of their choice It
 * takes in a Reader with their CSV file path, and a custom row parser that implements
 * CreatorFromRow and returns an object of their choice
 *
 * <p>Added to be able to parse new CSV from server, parse now has the parameter regex, which takes
 * in a regex and uses it to split. Can call without and uses the old regex.
 *
 * <p>TO USE: Must create a custom CreatorFromRow class, and pass that in with a reader with CSV
 * file to a parse object, then run parse. This could be done in a custom main method.
 *
 * @param <T> the object type the developer wants their return list to contain
 */
public class Parse<T> implements Iterable<T> {
  Reader reader;
  CreatorFromRow<T> creator;
  String regex;

  /**
   * Constructor for parse
   *
   * @param reader Reader with CSV file path
   * @param creator custom row parser implementing CreatorFromRow
   */
  public Parse(Reader reader, CreatorFromRow<T> rowType) {
    this.reader = reader;
    this.creator = rowType;
    this.regex = ",(?=([^\\\"]*\\\"[^\\\"]*\\\")*(?![^\\\"]*\\\"))";
  }

  public Parse(Reader reader, CreatorFromRow<T> rowType, String regex) {
    this.reader = reader;
    this.creator = rowType;
    this.regex = regex;
  }

  /**
   * Main parse method
   *
   * @return List<T> where T is developers desired object for parsed csv to contain
   */
  public List<T> parse() {
    BufferedReader bf = new BufferedReader(reader);
    List<T> rowList = new ArrayList<>();
    try {
      String fl = bf.readLine();
      while (fl != null) {
        rowList.add(this.creator.create(parseRow(fl)));
        fl = bf.readLine();
      }
    } catch (IOException i) {
      System.err.println("While parsing, the file path was not found.");
    } catch (FactoryFailureException e) {
      System.err.println(
          "Error parsing the row from a list of string to "
              + "the desired object with the inputted creator from row.");
      // for testing purposes
      return Collections.emptyList();
    }
    return rowList;
  }

  /**
   * Method that takes in each line from the CSV, and returns a List<String>, where each String was
   * seperated by the provided regex Allows row to be passed into the custom CreatorFromRow
   *
   * @param row line from CSV as a String
   * @return List<String> of row split by regex
   */
  public List<String> parseRow(String row) {
    final Pattern regexSplitCSVRow = Pattern.compile(this.regex);

    List<String> list = List.of(regexSplitCSVRow.split(row));
    return list;
  }

  @NotNull
  @Override
  public Iterator<T> iterator() {
    return this.parse().iterator();
  }
}
