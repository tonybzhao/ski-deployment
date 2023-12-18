package edu.brown.cs.student.server.ACS;

/**
 * This is a record holding a location to be searched. Contains a state and county.
 *
 * @param state State to be searched
 * @param county County to be searched
 */
public record Location(String state, String county) {
  public String toOurServerParams() {
    return "state=" + state + "&county=" + county;
  }
}
