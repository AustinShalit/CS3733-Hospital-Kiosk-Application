package edu.wpi.cs3733.d19.teamO.entity.database;

import java.util.Set;

import edu.wpi.cs3733.d19.teamO.entity.ExternalTransportationRequest;
import edu.wpi.cs3733.d19.teamO.entity.SanitationRequest;

/**
 * Database access object for {@link SanitationRequest}s.
 */
interface ExternalTransportationRequestDao extends Dao<Integer, ExternalTransportationRequest> {
  Set<ExternalTransportationRequest> getNumberByCategory(String type);
}
