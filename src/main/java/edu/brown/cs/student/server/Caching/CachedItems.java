package edu.brown.cs.student.server.Caching;

import java.util.HashMap;
import java.util.List;

/**
 * Class dealing primarily with caching. Contains a hashmap of previous requested percentages, a
 * developer tailored class for removing cached items once a limit is reached, and a frequency of
 * request tracker.
 */
public class CachedItems {
  HashMap<String, List<List<String>>> percentages;
  RemoveCacheItem customRemove;
  HashMap<String, Integer> countyFrequency;

  /**
   * This constructor initializes a hashmap for percentages, frequency, and the cache removal
   * system.
   */
  public CachedItems() {
    this.percentages = new HashMap<String, List<List<String>>>();
    this.countyFrequency = new HashMap<>();
    this.customRemove = new OurRemoveCache(this.countyFrequency);
  }

  /**
   * This is a getter that returns the list of list of strings for a state.
   *
   * @param stateName State name to be searched
   * @return List of list of strings for a state
   */
  public List<List<String>> getInfo(String stateName) {
    return this.percentages.get(stateName);
  }

  /**
   * This boolean checks if a county exists in the broadband percentage hashmap.
   *
   * @param countyName County to be checked
   * @return True if county is in, false if not
   */
  public boolean checkCounty(String countyName) {
    if (this.percentages.containsKey(countyName)) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Method that adds a county to the hashmap. Calls on the removal class to delegate
   * responsibility.
   *
   * @param countyName County to be added
   * @param info Value to be cached
   */
  public void addCounty(String countyName, List<List<String>> info) {
    this.percentages = this.customRemove.updateCache(this.percentages, countyName, info);
  }

  /**
   * Whenever a request is made to a stored county, we add to the frequency tracker. This is
   * utilized by the removal class.
   *
   * @param countyName County name to be searched, frequency is increased
   */
  public void addFrequency(String countyName) {
    int frequencyToAdd = 1 + this.countyFrequency.getOrDefault(countyName, 0);
    this.countyFrequency.put(countyName, frequencyToAdd);
  }
}
