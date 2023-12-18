package edu.brown.cs.student.Testing.CSVTesting;

import static org.testng.AssertJUnit.assertEquals;

import edu.brown.cs.student.Ski.Records.Resort;
import edu.brown.cs.student.Ski.Records.ResortInfo;
import edu.brown.cs.student.Ski.Records.SnowForecast;
import edu.brown.cs.student.Ski.Records.WeatherForecast;
import edu.brown.cs.student.Ski.ResortConditions;
import edu.brown.cs.student.Ski.ResortList;
import edu.brown.cs.student.Ski.ScrapeRetrieval;
import edu.brown.cs.student.Ski.SnowConditions;
import edu.brown.cs.student.server.ACS.DatasourceException;
import edu.brown.cs.student.server.Caching.CachedResorts;
import edu.brown.cs.student.server.Search.ColumnIdentifier;
import edu.brown.cs.student.server.Search.Search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/** Class to test the search method */
public class SearchTest {

  String file = "";
  String stateFile = "";
  String uneven = "";

  @Before
  public void setup() {
    // Even file
    file = "/Users/tyype1/Desktop/Brown/sem3/CS32/Projects/csv-" + "tyypezza/data/census/even.csv";
    // state file
    stateFile =
        "/Users/tyype1/Desktop/Brown/sem3/CS32/Projects/csv-tyypezza"
            + "/data/census/dol_ri_earnings_disparity.csv";
    // uneven file
    uneven =
        "/Users/tyype1/Desktop/Brown/sem3/CS32/Projects/csv-" + "tyypezza/data/census/uneven.csv";
  }

  // Test basic search with headers and no headers when search
  // value is not a header
  @Test
  public void testBasicSearch() {
    Search s = new Search(file, "51", ColumnIdentifier.Identifier.NONE, "", false, true);
    List<Integer> expected = List.of(1);
    assertEquals(expected, s.search());

    Search s2 = new Search(file, "51", ColumnIdentifier.Identifier.NONE, "", true, true);
    List<Integer> expected2 = List.of(1);
    assertEquals(expected2, s2.search());
  }

  // Test basic search with headers and no headers when search value is a header
  @Test
  public void testBasicHeaderSearch() {
    Search s = new Search(file, "Height", ColumnIdentifier.Identifier.NONE, "", false, true);
    List<Integer> expected = List.of(0);
    assertEquals(expected, s.search());

    Search s2 = new Search(file, "Height", ColumnIdentifier.Identifier.NONE, "", true, true);
    List<Integer> expected2 = new ArrayList<>();
    assertEquals(expected2, s2.search());
  }

  // Test search where multiple rows are returned
  @Test
  public void testMultipleRowsSearch() {
    Search s = new Search(stateFile, "RI", ColumnIdentifier.Identifier.NONE, "", true, true);
    List<Integer> expected = List.of(1, 2, 3, 4, 5, 6);
    assertEquals(expected, s.search());
  }

  // Test search case possibilities
  // Case matters and is different - doesn't return
  // Case doesn't matter and is different - returns
  @Test
  public void testCaseSearch() {
    Search s = new Search(stateFile, "Ri", ColumnIdentifier.Identifier.NONE, "", true, false);
    List<Integer> expected = List.of(1, 2, 3, 4, 5, 6);
    assertEquals(expected, s.search());

    Search s2 = new Search(stateFile, "Ri", ColumnIdentifier.Identifier.NONE, "", true, true);
    List<Integer> expected2 = Collections.emptyList();
    assertEquals(expected2, s2.search());
  }

  // Test search when index specified, both finding in column and
  // not finding since its in another
  // column
  @Test
  public void testIndexSearch() {
    Search s = new Search(stateFile, "White", ColumnIdentifier.Identifier.INDEX, "1", true, true);
    List<Integer> expected = List.of(1);
    assertEquals(expected, s.search());

    Search s2 = new Search(stateFile, "White", ColumnIdentifier.Identifier.INDEX, "0", true, true);
    List<Integer> expected2 = new ArrayList<>();
    assertEquals(expected2, s2.search());
  }

  // Test search when header specified, both finding in
  // column and not finding since it's in another
  // column
  @Test
  public void testHeaderSearch() {
    Search s =
        new Search(stateFile, "White", ColumnIdentifier.Identifier.HEADER, "Data Type", true, true);
    List<Integer> expected = List.of(1);
    assertEquals(expected, s.search());

    Search s2 =
        new Search(stateFile, "White", ColumnIdentifier.Identifier.HEADER, "State", true, true);
    List<Integer> expected2 = new ArrayList<>();
    assertEquals(expected2, s2.search());
  }

  // Test when value is not in CSV
  @Test
  public void testNotInSearch() {
    Search s =
        new Search(stateFile, "notin", ColumnIdentifier.Identifier.HEADER, "Data Type", true, true);
    List<Integer> expected = new ArrayList<>();
    assertEquals(expected, s.search());
  }

  // Test when column index/header is missing item
  @Test
  public void testUnevenSearch() {
    Search s = new Search(uneven, "12", ColumnIdentifier.Identifier.INDEX, "3", true, true);
    List<Integer> expected = List.of(3);
    assertEquals(expected, s.search());
  }

  @Test
  public void test2() throws IOException {
    ResortList list = new ResortList();
    String name = "";
    for (int i = 0; i < list.getResortNames().size(); i++) {
      if(list.getResortNames().get(i).contains("Schladming")){
        name = list.getResortNames().get(i).toLowerCase();
        break;
      }
    }
    System.out.println(name);
  }

  @Test
  public void test() throws IOException, InterruptedException, DatasourceException {
    ResortList list = new ResortList();
    String name = "";
    for (int i = 0; i < list.getResortNames().size(); i++) {
      if(list.getResortNames().get(i).contains("Okemo")){
        name = list.getResortNames().get(i).toLowerCase();
        break;
      }
    }
    ScrapeRetrieval scrapeRetrieval = new ScrapeRetrieval();
    scrapeRetrieval.organize(scrapeRetrieval.retrieve());

  //  System.out.println(list.getResortMap());
    //System.out.println(list.getResortNames());
    ResortConditions conditions = new ResortConditions();
   //WeatherForecast forecast1 = conditions.getForecast("Schladming – Planai/\u200B\u200BHochwurzen/\u200B\u200BHauser Kaibling/\u200B\u200BReiteralm (4-Berge-Skischaukel)");
    WeatherForecast forecast1 = conditions.getForecast("Okemo");
    SnowConditions snow = new SnowConditions();
   //SnowForecast forecast2 = snow.getForecast("Schladming – Planai/\u200B\u200BHochwurzen/\u200B\u200BHauser Kaibling/\u200B\u200BReiteralm (4-Berge-Skischaukel)");
    SnowForecast forecast2 = snow.getForecast("Okemo");

    Integer lifts = scrapeRetrieval.getLift("Okemo");
   ResortInfo info = list.getResortMap().get(name);
   Resort resort = new Resort(name,lifts, info, forecast1, forecast2);
    System.out.println(resort);

    //  CachedResorts resorts = new CachedResorts(list);
    //   resorts.populateCache(list);
   // System.out.println(conditions.regexInput("Two words"));
  }
}

