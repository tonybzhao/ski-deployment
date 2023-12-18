package edu.brown.cs.student.server.Main;

import static spark.Spark.after;

import edu.brown.cs.student.Ski.ResortList;
import edu.brown.cs.student.Ski.ScrapeRetrieval;
import edu.brown.cs.student.server.ACS.DatasourceException;
import edu.brown.cs.student.server.ACS.StateIds;

import edu.brown.cs.student.server.Caching.CachedItems;
import edu.brown.cs.student.server.Caching.CachedResorts;
import edu.brown.cs.student.server.Handlers.*;
import spark.Spark;

import java.io.IOException;

/**
 * Server class for to run the server that can be used to make requests to load, search, and view
 * csvs, and to search the ACS database for the percentage of households with broadband access in a
 * certain US county. Just need to run it, then copy print statement into a browser, and use README
 * instructions to send queries.
 */
public class Server {
  public static void main(String[] args) throws IOException, InterruptedException, DatasourceException {
    int port = 3232;


    ResortList list = new ResortList();
      ScrapeRetrieval scraper = new ScrapeRetrieval();
      scraper.organize(scraper.retrieve());

    CachedResorts cache = new CachedResorts(list);
      System.out.println("Server is ready to use!");


    Spark.port(port);

    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });

    Spark.get("resorts", new ResortHandler(list, cache));
    Spark.get("*", new WildCardHandler());

    Spark.init();
    Spark.awaitInitialization();

    System.out.println("Server started at http://localhost:" + port);
  }
}
