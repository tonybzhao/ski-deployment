package edu.brown.cs.student.server.Caching;

import java.util.*;

/**
 * Class for the removal logic. It implements the RemoveCacheItem interface to allow developers
 * flexibility in how they want to edit the cache and remove items from it. It contains a hashmap
 * keeping track of frequency of requests.
 */
public class OurRemoveCache implements RemoveCacheItem {
  HashMap<String, Integer> countyFrequency;

  /**
   * Constructor for the removal class. Takes in a hashmap keeping track of request frequency.
   *
   * @param countyFrequency Hashmap of request frequency passed in
   */
  public OurRemoveCache(HashMap<String, Integer> countyFrequency) {
    this.countyFrequency = countyFrequency;
  }

  /**
   * Interface method that allows for the developer to tailor the cache removal and updating to
   * their needs. It allows for flexibility in methods of removal and management of memory.
   *
   * @param map Map containing the cached information
   * @param countyName County name to be updated
   * @param info Value to be cached
   * @return Returns the updated cache
   */
  @Override
  public HashMap updateCache(
      HashMap<String, List<List<String>>> map, String countyName, List<List<String>> info) {
    if (map.size() < 2) {
      map.put(countyName, info);
    } else if (map.size() >= 2) {
      String minCounty = this.findMin(map);
      this.countyFrequency.remove(minCounty);
      map.remove(minCounty);
      map.put(countyName, info);
      // remove from both Maps
      // add only to main map, will be added to frequency elsewhere
    }
    return map;
  }

  /**
   * Method to find the county in the cache that has been queried the least
   *
   * @param map the cache
   * @return the county (key) with the least amount of searches to be removed from cache
   */
  private String findMin(HashMap<String, List<List<String>>> map) {
    String minCounty = null;
    int minCountyFreq = -1;
    Iterator<Map.Entry<String, List<List<String>>>> mapIterator = map.entrySet().iterator();

    while (mapIterator.hasNext()) {
      Map.Entry<String, List<List<String>>> entry = mapIterator.next();
      String key = entry.getKey();

      int comparisonFreq = this.countyFrequency.get(key);
      if (minCountyFreq == -1) {
        minCountyFreq = comparisonFreq;
        minCounty = key;
      } else if (comparisonFreq < minCountyFreq) {
        minCountyFreq = comparisonFreq;
        minCounty = key;
      }
    }
    return minCounty;
  }
}
