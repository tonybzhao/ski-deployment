package edu.brown.cs.student.server.Caching;

import java.util.HashMap;
import java.util.List;

/**
 * Interface for customizable way to remove items from cache Will be passed into the cache for
 * querys, and will be used to decide when/how cached items will be removed
 */
public interface RemoveCacheItem {
  /**
   * Only method required, which returns the updated hashmap with the new item to cash, and removed
   * one if deemed necessary
   *
   * @param map
   * @param countyName
   * @param info
   * @return
   */
  public HashMap updateCache(
      HashMap<String, List<List<String>>> map, String countyName, List<List<String>> info);
}
