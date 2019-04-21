package edu.wpi.cs3733.d19.teamO.entity.database;

import edu.wpi.cs3733.d19.teamO.entity.GiftRequest;

import java.util.Set;

/**
 * Database access object for {@link GiftRequest}s.
 */
interface GiftRequestDao extends Dao<Integer, GiftRequest> {
    Set<GiftRequest> getNumberByCategory(String type);
}
