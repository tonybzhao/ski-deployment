package edu.brown.cs.student.Testing.CSVTesting.Dimensions;

/**
 * Class to use when testing a custom creator from row Just has 4 ints as parameters To account for
 * headers in the CSV, can also take in no parameters and will have its bool isHeader set to true so
 * program knows it is representing the header
 */
public class DimensionsTestClass {
  int height;
  int weight;
  int length;
  int width;
  Boolean isHeader;

  /**
   * Constructor with all dimensions
   *
   * @param height just an int
   * @param weight just an int
   * @param length just an int
   * @param width just an int
   */
  public DimensionsTestClass(int height, int weight, int length, int width) {
    this.height = height;
    this.weight = weight;
    this.length = length;
    this.width = width;
    this.isHeader = false;
  }

  /** Constructor for when the header is being parsed Just sets isHeader to true */
  public DimensionsTestClass() {
    this.isHeader = true;
  }

  /**
   * toString for when parse prints out rows Just prints out as if it was a list of Strings of the
   * dimensions, or if header the header names
   *
   * @return String of object representing row
   */
  @Override
  public String toString() {
    if (this.isHeader) {
      return "[Height, Weight, Length, Width]";
    }
    return "Dimension: [" + height + ", " + weight + ", " + length + ", " + width + "]";
  }

  /**
   * Equals method for ParseTest to see if outputted rows are what they should be Just checks if
   * each dimensions is equal, or if they are both headers
   *
   * @param obj
   * @return true if equal
   */
  @Override
  public boolean equals(Object obj) {
    DimensionsTestClass dim = (DimensionsTestClass) obj;
    if (this.isHeader == true && dim.isHeader == true) {
      return true;
    } else {
      if (this.height == dim.height
          && this.weight == dim.weight
          && this.length == dim.length
          && this.width == dim.width) {
        return true;
      }
    }
    return false;
  }
}
