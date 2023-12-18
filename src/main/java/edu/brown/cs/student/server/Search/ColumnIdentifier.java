package edu.brown.cs.student.server.Search;

/**
 * Class to hold enum for column identifier type Will allow search to know if it needs to look in
 * certain columns, and if so whether it should look by column index or header name.
 */
public class ColumnIdentifier {
  public enum Identifier {
    NONE,
    HEADER,
    INDEX
  }
}
