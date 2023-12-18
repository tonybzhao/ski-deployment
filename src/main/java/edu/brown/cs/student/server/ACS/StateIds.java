package edu.brown.cs.student.server.ACS;

import java.util.Collections;
import java.util.List;

/**
 * Class dealing primarily with information about the state names to state IDs. Contains a list of
 * list of strings with this information
 */
public class StateIds {
  List<List<String>> statesToIds;

  /** Constructor initializing the list of list of strings. */
  public StateIds() {
    this.statesToIds = Collections.emptyList();
  }

  /**
   * Boolean that checks if this list of list of strings is empty.
   *
   * @return True if empty, false if not
   */
  public boolean isStateToIdsEmpty() {
    if (this.statesToIds.isEmpty()) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Getter method returning the list of list of strings
   *
   * @return States to IDs list of list of strings
   */
  public List<List<String>> getStatesToIds() {
    return this.statesToIds;
  }

  /**
   * Setter that sets the list of list of strings
   *
   * @param statesToIds List of list of strings passed in
   */
  public void setStatesToIds(List<List<String>> statesToIds) {
    this.statesToIds = statesToIds;
  }
}
