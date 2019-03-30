package edu.wpi.cs3733.d19.teamO.entity.database;

import java.util.Optional;
import java.util.Set;

/**
 * Database Access Object.  Abstract object to access a database. Used to isolate the application
 * layer from the persistence layer.  Can perform CRUD operations (Create, Retrieve, Update,
 * Delete).
 *
 * @param <T> The type of data being accessed
 */
interface Dao<T> {
  /**
   * Retrieves an object from the database.
   *
   * @param id The ID of the object
   * @return An Optional containing the object if it exists
   */
  Optional<T> get(String id);

  /**
   * Retrieves all the objects from the database.
   *
   * @return A Set of all the objects
   */
  Set<T> getAll();

  /**
   * Creates a new object in the database.
   *
   * @return True if the operation was successful
   */
  boolean insert(T o);

  /**
   * Updates an object in the database.
   *
   * @return True if the operation was successful
   */
  boolean update(T o);

  /**
   * Deletes an object from the database.
   *
   * @return True if the operation was successful
   */
  boolean delete(T o);
}
