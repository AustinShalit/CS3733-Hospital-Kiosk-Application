package edu.wpi.cs3733.d19.teamO.entity;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A ScheduleRequest with a request ID, start time, end time, name of who reserved, and a room ID.
 */
public class SchedulingRequest {
  private static int idCount;
  private int id;
  private final LocalDateTime startTime;
  private final LocalDateTime endTime;
  private final String whoReserved;
  private final Node room;

  // Constructors

  /**
<<<<<<< HEAD
   * Constructor for scheduling requests not yet added to the scheduling_request table.
   * @param startTime start time of reservation
   * @param endTime end time of reservation
   * @param name name of the person requesting
   * @param room room being requested
=======
   * Constructor with id and full start and end times specified.
   *
   * @param id            ID
   * @param startTime     Start time
   * @param endTime       End Time
   * @param timeRequested Time Request opened
   * @param timeCompleted Time Request approved
   * @param name          Who requested the room
   * @param room          Which room to reserve
>>>>>>> master
   */
  public SchedulingRequest(final LocalDateTime startTime, final LocalDateTime endTime,
                           final String name, final Node room) {
    this.id = idCount++; // not added to table yet
    this.startTime = startTime;
    this.endTime = endTime;
    this.whoReserved = name;
    this.room = room;
  }

  /**
   * Constructor for retrieving info from existing scheduling requests.
   * @param startTime start time of reservation
   * @param endTime end time of reservation
   * @param name name of the person requesting
   * @param room room being requested
   */
  public SchedulingRequest(int id, final LocalDateTime startTime, final LocalDateTime endTime,
                           final String name, final Node room) {
    this.id = id;
    this.startTime = startTime;
    this.endTime = endTime;
    this.whoReserved = name;
    this.room = room;
  }

  /**
   * Get the id number of the current request.
   * @return the id of current request
   */
  public int getID() {
    return id;
  }

  /**
   * Set the Id of the the current request.
   * @param id the id to be set
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Get the start time of the current request.
   * @return the startTime
   */
  public LocalDateTime getStartTime() {
    return startTime;
  }

  /**
   * Get the end time of the current request.
   * @return the endTime
   */
  public LocalDateTime getEndTime() {
    return endTime;
  }

  /**
   * Get the name of who reserved the room.
   * @return name of whoReserved
   */
  public String getWhoReserved() {
    return whoReserved;
  }

  /**
   * Get the room to be reserved.
   * @return the roomId
   */
  public Node getRoom() {
    return room;
  }

  /**
   * Check equality of a given request against the current request.
   *
   * @param o Other SchedulingRequest to compare
   * @return true if all parameters are equal, false if not
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof SchedulingRequest)) {
      return false;
    }
    SchedulingRequest that = (SchedulingRequest) o;
<<<<<<< HEAD
    return getID() == that.getID()
        && getStartTime().equals(that.getStartTime())
        && getEndTime().equals(that.getEndTime())
=======
    return getId() == that.getId()
        && getStartTime().equals(that.getStartTime())
        && getEndTime().equals(that.getEndTime())
        && getTimeRequested().equals(that.getTimeRequested())
        && getTimeCompleted().equals(that.getTimeCompleted())
>>>>>>> master
        && getRoom().equals(that.getRoom())
        && getWhoReserved().equals(that.getWhoReserved());
  }

  /**
   * Generates HashCode for current request.
   *
   * @return integer Hash Code
   */
  @Override
  public int hashCode() {
    return Objects.hash(getID(), getStartTime(), getEndTime(), getRoom(),
        getWhoReserved());
  }

  /**
   * Informational String Maker.
   *
   * @return Informational String with parameters of this Scheduling Request
   */
  @Override
  public String toString() {
    return "SanitationRequest{"
<<<<<<< HEAD
        + "id=" + getID()
=======
        + "id=" + getId()
>>>>>>> master
        + ", startTime=" + getStartTime()
        + ", endTime=" + getEndTime()
        + ", locationNode=" + getRoom().toString()
        + ", whoReserved=" + getWhoReserved()
        + '}';
  }
}
