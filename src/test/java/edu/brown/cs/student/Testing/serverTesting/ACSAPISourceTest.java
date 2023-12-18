package edu.brown.cs.student.Testing.serverTesting;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.testng.AssertJUnit.assertEquals;

import edu.brown.cs.student.server.ACS.*;
import edu.brown.cs.student.server.Caching.CachedItems;
import org.junit.Before;
import org.junit.jupiter.api.Test;

/** Class to send real querys to ACS api to test requests */
public class ACSAPISourceTest {
  @Before
  public void setup() {}

  /**
   * Test for successful request to real api source for broadband data
   *
   * @throws DatasourceException
   */
  @Test
  public void testACSAPISourceSuccess_REAL() throws DatasourceException {
    ACSAPISource source = new ACSAPISource(new CachedItems(), new StateIds());
    Location location = new Location("California", "Orange County");
    BroadbandData bd = source.getBroadbandData(location);

    assertNotNull(bd);
    assertEquals("93.0", bd.percOfHouses());
  }

  /**
   * Test for bad request to real api source for broadband data because state does not exist
   *
   * @throws DatasourceException
   */
  @Test
  public void testACSAPISourceBadState_REAL() throws DatasourceException {
    ACSAPISource source = new ACSAPISource(new CachedItems(), new StateIds());
    Location location = new Location("Californi", "Orange County");
    try {
      BroadbandData bd = source.getBroadbandData(location);
    } catch (DatasourceException d) {
      assertEquals("State not found", d.getMessage());
    }
  }

  /**
   * Test for bad request to real api source for broadband data because county does not exist
   *
   * @throws DatasourceException
   */
  @Test
  public void testACSAPISourceBadCounty_REAL() throws DatasourceException {
    ACSAPISource source = new ACSAPISource(new CachedItems(), new StateIds());
    Location location = new Location("California", "Oran County");
    try {
      BroadbandData bd = source.getBroadbandData(location);
    } catch (DatasourceException d) {
      assertEquals("County not found", d.getMessage());
    }
  }

  /**
   * Test for bad request to real api source for broadband data because county was too small
   *
   * @throws DatasourceException
   */
  @Test
  public void testACSAPISourceSmallCounty_REAL() throws DatasourceException {
    ACSAPISource source = new ACSAPISource(new CachedItems(), new StateIds());
    Location location = new Location("California", "Alpine County");
    try {
      BroadbandData bd = source.getBroadbandData(location);
    } catch (DatasourceException d) {
      assertEquals("County too small", d.getMessage());
    }
  }
}
