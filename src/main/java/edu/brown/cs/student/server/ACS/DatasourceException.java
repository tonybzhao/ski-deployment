package edu.brown.cs.student.server.ACS;

/**
 * General exception class used heavily by the Broadband class. It throws and exception for any
 * cases where we find the data is in a form that we do not expect, for bad connections, or bad
 * results.
 */
public class DatasourceException extends Exception {
  private final Throwable cause;

  /**
   * Constructor for the exception. Throws an error message that is passed in.
   *
   * @param message Message to be thrown by the error
   */
  public DatasourceException(String message) {
    super(message);
    this.cause = null;
  }

  /**
   * Secondary constructor allowing for the exception to be thrown
   *
   * @param message Message for the error
   * @param cause Throwable cause to be passed in
   */
  public DatasourceException(String message, Throwable cause) {
    super(message);
    this.cause = cause;
  }
}
