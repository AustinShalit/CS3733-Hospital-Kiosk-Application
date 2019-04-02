package edu.wpi.cs3733.d19.teamO.entity.database;

import edu.wpi.cs3733.d19.teamO.entity.SecurityRequest;

/**
 * Database access object for {@link edu.wpi.cs3733.d19.teamO.entity.SecurityRequest}s.
 */
interface SecurityRequestDao extends Dao<Integer, SecurityRequest> {
  /**
   * Creates a new Security Request in the database, with an explicit ID.
   *
   * @return True if the operation was successful
   */
  boolean insertExplicitId(SecurityRequest o);
}
