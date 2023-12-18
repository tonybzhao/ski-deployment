package edu.brown.cs.student.Testing.CSVTesting.Dimensions;

import edu.brown.cs.student.CSVCode.Parsing.CreatorFromRow;
import edu.brown.cs.student.CSVCode.Parsing.FactoryFailureException;
import java.util.List;

/**
 * Class to test a custom Creator from row Takes in list of strings as a row. Row must contain 4
 * items, either all ints (dimensions) Or Height, Weight, Length, and Width
 */
public class DimensionsCreatorFromRow implements CreatorFromRow {

  /**
   * CreatorFromRow create method
   *
   * @param row list passed in from parse
   * @return parsed list as a DimensionsTestClass object
   * @throws FactoryFailureException when row cannot be parsed
   */
  @Override
  public DimensionsTestClass create(List row) throws FactoryFailureException {
    if (row.equals(List.of("Height", "Weight", "Length", "Width"))) {
      return new DimensionsTestClass();
    }
    if (row.size() != 4) {
      throw new FactoryFailureException(
          "Row must have 4 entries (Height, Weight, Length, Width).", row);
    }
    DimensionsTestClass dim = null;
    try {
      int height = Integer.valueOf((String) row.get(0));
      int weight = Integer.valueOf((String) row.get(1));
      int length = Integer.valueOf((String) row.get(2));
      int width = Integer.valueOf((String) row.get(3));
      dim = new DimensionsTestClass(height, weight, length, width);

    } catch (NumberFormatException e) {
      throw new FactoryFailureException(
          "At least one entry did is not a proper" + "input for a DimensionTestClass.", row);
    }
    return dim;
  }
}
