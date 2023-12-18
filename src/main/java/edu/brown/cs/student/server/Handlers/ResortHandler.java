package edu.brown.cs.student.server.Handlers;

import com.squareup.moshi.Moshi;
import edu.brown.cs.student.Ski.Records.Resort;
import edu.brown.cs.student.Ski.ResortList;
import edu.brown.cs.student.server.Caching.CachedResorts;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Map;
import java.util.Set;

public class ResortHandler implements Route {
    private ResortList list;
    private CachedResorts cache;

    public ResortHandler(ResortList list, CachedResorts cache) {
        this.list = list;
        this.cache = cache;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {

        Set<String> params = request.queryParams();
        if (params.size() != 1) {
            return new ResortFailure(
                    "error_bad_json",
                    params.toString(),
                    "Must have 1 parameter.")
                    .serialize();
        }
        if (!params.contains("type")) {
            return new ResortFailure(
                    "error_bad_json",
                    params.toString(),
                    "Must have parameter type.")
                    .serialize();
        }
        if (request.queryParams("type").equals("populate")) {
            this.cache.populateCache(this.list);
            return new ResortSuccess("Populating!");
        }
        if (request.queryParams("type").equals("cache")) {
            return new CacheSuccess("Success", this.cache.getCache());
        }
        return new ResortFailure(
                "error_bad_json",
                params.toString(),
                "Incorrect params.")
                .serialize();
    }

    public record ResortSuccess(String result, String SUCCESSMESSAGE) {
        /**
         * Constructor
         *
         * @param params       params passed into query
         * @param ERRORMESSAGE informative message for why broadband call failed
         */
        public ResortSuccess(String SUCCESSMESSAGE) {
            this("success", SUCCESSMESSAGE);
        }

        /**
         * Method serialize message into json
         *
         * @return json of the failure message
         */
        String serialize() {
            Moshi moshi = new Moshi.Builder().build();
            return moshi.adapter(ResortSuccess.class).toJson(this);
        }
    }

    public record CacheSuccess(String result, String SUCCESSMESSAGE, Map<String, Resort> map) {
        public CacheSuccess(String SUCCESSMESSAGE, Map<String, Resort> map) {
            this("success", SUCCESSMESSAGE, map);
        }

        /**
         * Method serialize message into json
         *
         * @return json of the failure message
         */
        String serialize() {
            Moshi moshi = new Moshi.Builder().build();
            return moshi.adapter(CacheSuccess.class).toJson(this);
        }
    }
}


public record ResortFailure(String result, String params, String ERRORMESSAGE) {
        /**
         * Constructor
         *
         * @param params params passed into query
         * @param ERRORMESSAGE informative message for why broadband call failed
         */
        public ResortFailure(String params, String ERRORMESSAGE) {
            this("failure", params, ERRORMESSAGE);
        }
        /**
         * Method serialize message into json
         *
         * @return json of the failure message
         */
        String serialize() {
            Moshi moshi = new Moshi.Builder().build();
            return moshi.adapter(ResortFailure.class).toJson(this);
        }
    }
}
