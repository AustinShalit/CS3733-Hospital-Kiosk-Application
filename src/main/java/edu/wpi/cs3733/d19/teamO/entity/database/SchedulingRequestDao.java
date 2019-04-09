package edu.wpi.cs3733.d19.teamO.entity.database;

import java.time.LocalDateTime;
import java.util.Set;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.SchedulingRequest;

/**
 * Database access object for {@link edu.wpi.cs3733.d19.teamO.entity.SchedulingRequest}s.
 */
interface SchedulingRequestDao extends Dao<Integer, SchedulingRequest> {
  /**
   * Used to check for conflicts before inserting them.
   *
   * @param schedulingRequest The scheduling request you want that might get added.
   * @return Returns false if no conflict, Returns true if there would be a conflict.
   */
  boolean wouldConflict(SchedulingRequest schedulingRequest);

  Set<Node> allAvailableNodes(LocalDateTime localDateTime);
}
