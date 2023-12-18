package edu.brown.cs.student.server.ACS;

/**
 * Record that stores the data from the broadband percentages. Essentially just stores and returns
 * the broadband percentage for a location.
 *
 * @param percOfHouses Percent of houses with broadband in a location
 */
public record BroadbandData(String percOfHouses) {
  public String toOurServerParams() {
    return "percOfHouses=" + percOfHouses;
  }
}
