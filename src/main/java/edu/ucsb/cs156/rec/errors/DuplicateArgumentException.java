package edu.ucsb.cs156.rec.errors;

/**
 * This is an error class for a custom RuntimeException in Java that is used to indicate
 * when an entity of a specific type has already been added to the database.
 */
public class DuplicateArgumentException extends RuntimeException {
  /**
   * Constructor for the exception
   * 
   * @param entityType The class of the entity that was not found, e.g. User.class
   * @param id the id that was being searched for
   */
  public DuplicateArgumentException(String reqType) {
    super("The request type of %s has already been added to the database"
      .formatted(reqType));
  }
}
