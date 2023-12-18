package edu.brown.cs.student.server.Handlers;

import com.squareup.moshi.Moshi;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Class for handling queries with an invalid endpoint (anything but the four out server handles).
 * Will just give the user an error message telling them valid endpoint options
 */
public class WildCardHandler implements Route {
  /**
   * Handle method will always return a message to the user with valid endpoints
   *
   * @param request request from user
   * @param response
   * @return
   * @throws Exception
   */
  @Override
  public Object handle(Request request, Response response) throws Exception {
    return new EndpointNotFound(
            "Endpoint was not valid. Must be one of "
                + " 'loadmap', 'loadcsv', 'searchcsv', 'viewcsv', 'broadmap', 'map', or 'broadband'")
        .serialize();
  }

  /**
   * Record structuring the error message
   *
   * @param result will always be error_bad_json
   * @param ERRORMESSAGE informative message
   */
  public record EndpointNotFound(String result, String ERRORMESSAGE) {
    /**
     * The actual message returned
     *
     * @param ERRORMESSAGE informative message
     */
    public EndpointNotFound(String ERRORMESSAGE) {
      this("error_bad_json", ERRORMESSAGE);
    }

    /**
     * Method serialize message into json
     *
     * @return json of the returned message
     */
    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(WildCardHandler.EndpointNotFound.class).toJson(this);
    }
  }
}
