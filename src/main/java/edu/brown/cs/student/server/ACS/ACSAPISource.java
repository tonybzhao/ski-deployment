package edu.brown.cs.student.server.ACS;

import static spark.Spark.connect;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.server.Search.ColumnIdentifier;
import edu.brown.cs.student.server.Search.Search;
import edu.brown.cs.student.server.Caching.CachedItems;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import okio.Buffer;

/**
 * This is the ACSAPISource class. It implements the BroadbandDataSource interface and is
 * responsible for making calls to the API and handling the data received, as well as interactions
 * with the cache.
 */
public class ACSAPISource implements BroadbandDataSource {
  CachedItems cache;
  StateIds states;

  /**
   * This is the constructor for the class. It associates the cache and collection holding states to
   * IDs.
   *
   * @param cache A cache containing data that is already requested
   * @param states A collection containing references to IDs from state names
   */
  public ACSAPISource(CachedItems cache, StateIds states) {
    this.cache = cache;
    this.states = states;
  }

  /**
   * This method makes a call to the API. It checks for various exceptions that might result from
   * the connection or data received. It forms a connections, accesses the API through the URL, and
   * converts data from all the states to state codes into a form that we can use. If the request
   * has not been made yet, it saves the data into the cache. Otherwise, it checks the cache for the
   * requested data.
   *
   * @param states The collection holding references to IDs from state names
   * @param stateName The state name to be searched
   * @return The ID of the state that is searched
   * @throws DatasourceException An exception thrown in the case of a bad request, or malformed data
   */
  private static String resolveStateID(StateIds states, String stateName)
      throws DatasourceException {
    try {
      if (states.isStateToIdsEmpty()) {
        URL requestURL =
            new URL("https", "api.census.gov", "/data/2010/dec/sf1?get=NAME&for=state:*");
        HttpURLConnection clientConnection = connect(requestURL);
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<List> jsonAdapter = moshi.adapter(List.class);
        List<List<String>> body =
            jsonAdapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
        clientConnection.disconnect();
        Search search =
            new Search(body, stateName, ColumnIdentifier.Identifier.NONE, "", true, false);
        List<List<String>> stateRow = search.searchParsed();
        if (stateRow.isEmpty()) {
          throw new DatasourceException("State not found");
        }
        states.setStatesToIds(body);
        String stateID = stateRow.get(0).get(1);
        if (body == null) throw new DatasourceException("Malformed response from ACS");

        return stateID;
      } else {
        Search search =
            new Search(
                states.getStatesToIds(),
                stateName,
                ColumnIdentifier.Identifier.NONE,
                "",
                true,
                false);
        String stateID = search.searchParsed().get(0).get(1);
        return stateID;
      }
    } catch (IOException e) {
      throw new DatasourceException(e.getMessage());
    }
  }

  /**
   * A similar method to the previous, this method handles the request for the county ID. The metod
   * also forms a connections via a url, then returns it in a list of list of strings to be searched
   * for the desired county ID. If the cache contains the state and county already, it retreives it
   * from the cache.
   *
   * @param cache The cache containing previously made requests.
   * @param stateID The state ID for the state in which the counties are searched
   * @param countyName The name of the country to be searched for
   * @param stateName The state to be searched in
   * @return The county ID that is found
   * @throws DatasourceException An exception thrown if a county is not found or a bad request or
   *     bad datasource is used.
   */
  private static String resolveCountyID(
      CachedItems cache, String stateID, String countyName, String stateName)
      throws DatasourceException {
    try {
      if (!cache.checkCounty(countyName)) {
        URL requestURL =
            new URL(
                "https",
                "api.census.gov",
                "/data/2010/dec/sf1?get=NAME&for=county:*&in=state:" + stateID);
        HttpURLConnection clientConnection = connect(requestURL);
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<List> jsonAdapter = moshi.adapter(List.class);
        List<List<String>> body =
            jsonAdapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
        clientConnection.disconnect();
        Search search =
            new Search(
                body,
                countyName + ", " + stateName,
                ColumnIdentifier.Identifier.NONE,
                "",
                true,
                false);
        List<List<String>> countyRow = search.searchParsed();
        if (countyRow.isEmpty()) {
          throw new DatasourceException("County not found");
        }
        String countyID = countyRow.get(0).get(2);
        if (body == null) throw new DatasourceException("Malformed response from ACS");
        return countyID;
      } else {
        String countyID = cache.getInfo(countyName).get(0).get(2);
        return countyID;
      }
    } catch (IOException e) {
      throw new DatasourceException(e.getMessage());
    }
  }

  /**
   * This method deals with forming the connection with the API. It is a helper called to form the
   * connection and returns it to be used by the resolve methods.
   *
   * @param requestURL The requested URL from the API that is passed in
   * @return The connection to the API
   * @throws IOException Exception thrown in the try catch used to then throw a DatasourceException
   * @throws DatasourceException Exception thrown in cases of bad input, connection, or unexpected
   *     results.
   */
  private static HttpURLConnection connect(URL requestURL) throws IOException, DatasourceException {
    URLConnection urlConnection = requestURL.openConnection();
    if (!(urlConnection instanceof HttpURLConnection)) {
      throw new DatasourceException("unexpected: result of connection wasn't HTTP");
    }
    HttpURLConnection clientConnection = (HttpURLConnection) urlConnection;
    clientConnection.connect();
    if (clientConnection.getResponseCode() != 200) {
      throw new DatasourceException(
          // Only ever found when county is too small
          "County too small");
    }
    return clientConnection;
  }

  /**
   * Method used to resolve the percentage of broadband coverage at a certain state and county ID.
   * This either makes a new request or checks the cache to retrieve the percentage based on the IDs
   * passed in.
   *
   * @param cache The cache containing past queries that is passed in
   * @param stateID The ID of the state to be searched
   * @param countyID The ID of the county to be searched
   * @param countyName The county name of the county that is searched
   * @return Returns the percentage of broadband coverage in record form
   * @throws DatasourceException Exception thrown in the case of unexpected results, input, or
   *     connections.
   */
  private static BroadbandData percentageAtID(
      CachedItems cache, String stateID, String countyID, String countyName)
      throws DatasourceException {
    try {
      if (!cache.checkCounty(countyName)) {
        URL requestURL =
            new URL(
                "https",
                "api.census.gov",
                "/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:"
                    + countyID
                    + "&in=state:"
                    + stateID);
        HttpURLConnection clientConnection = connect(requestURL);
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<List> jsonAdapter = moshi.adapter(List.class);
        List<List<String>> body =
            jsonAdapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
        clientConnection.disconnect();
        if (body == null) throw new DatasourceException("Malformed response from ACS");
        cache.addCounty(countyName, body);
        cache.addFrequency(countyName);
        return new BroadbandData(body.get(1).get(1));
      } else {
        cache.addFrequency(countyName);
        return new BroadbandData(cache.getInfo(countyName).get(1).get(1));
      }
    } catch (IOException e) {
      throw new DatasourceException(e.getMessage());
    }
  }

  /**
   * Interface method that is utilized by this class to consolidate and produce a percentage in
   * record form. Calls methods to resolve state and county IDs, takes in a location to be searched
   * for, and calls the percentage method to return a final result.
   *
   * @param location Location including state and county to be searched for.
   * @return Percentage of broadband coverage in record form
   * @throws DatasourceException Exception thrown by the helper methods for bad data, requests, or
   *     unexpected results
   */
  @Override
  public BroadbandData getBroadbandData(Location location) throws DatasourceException {
    String stateID = resolveStateID(this.states, location.state());
    String countyID = resolveCountyID(this.cache, stateID, location.county(), location.state());
    BroadbandData percentage = percentageAtID(this.cache, stateID, countyID, location.county());
    return percentage;
  }
}
