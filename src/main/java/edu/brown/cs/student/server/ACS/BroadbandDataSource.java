package edu.brown.cs.student.server.ACS;

/**
 * Interface dealing with the BroadBand class. Contains a method that gets the broadband data for a
 * location.
 */
public interface BroadbandDataSource {
  BroadbandData getBroadbandData(Location location) throws DatasourceException;
}
