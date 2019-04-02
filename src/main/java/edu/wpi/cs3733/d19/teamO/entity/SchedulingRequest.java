package edu.wpi.cs3733.d19.teamO.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class SchedulingRequest {

  private int id;
  private final LocalDateTime startTime;
  private final LocalDateTime endTime;
  private final LocalDateTime timeRequested;
  private final LocalDateTime timeCompleted;
  private final String whoReserved;
  private final Node room;

  // Constructors
  public SchedulingRequest(LocalDateTime startTime, LocalDateTime endTime, LocalDateTime timeRequested,
                    LocalDateTime timeCompleted, String name, Node room) {
    this.startTime = startTime;
    this.endTime = endTime;
    this.timeRequested = timeRequested;
    this.timeCompleted = timeCompleted;
    this.whoReserved = name;
    this.room = room;
  }

  public SchedulingRequest(LocalTime startTime, LocalTime endTime, LocalDate date, LocalDateTime timeRequested,
                    LocalDateTime timeCompleted, String whoReserved, Node room) {
    this.startTime = LocalDateTime.of(date, startTime);
    this.endTime = LocalDateTime.of(date, endTime);
    this.timeRequested = timeRequested;
    this.timeCompleted = timeCompleted;
    this.whoReserved = whoReserved;
    this.room = room;
  }

  // Getters and Setters
  public int getId() {
    return this.id;
  }

  public LocalDateTime getStartTime() {
    return this.startTime;
  }

  public LocalDateTime getEndTime() {
    return this.endTime;
  }

  public LocalDateTime getTimeRequested() {
    return timeRequested;
  }

  public LocalDateTime getTimeCompleted() {
    return timeCompleted;
  }

  public String getWhoReserved() {
    return this.whoReserved;
  }

  public Node getRoom() {
    return room;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * Check equality
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
    return getId() == that.getId()
            && getStartTime().equals(that.getStartTime())
            && getEndTime().equals(that.getEndTime())
            && getTimeRequested().equals(that.getTimeRequested())
            && getTimeCompleted().equals(that.getTimeCompleted())
            && getRoom().equals(that.getRoom())
            && getWhoReserved().equals(that.getWhoReserved());
  }

  /**
   * Generates HashCode I guess?
   * @return integer Hash Code
   */
  @Override
  public int hashCode() {
    return Objects.hash(getId(), getStartTime(), getEndTime(), getTimeRequested(), getTimeCompleted(), getRoom(),
            getWhoReserved());
  }

  /**
   * Informational String Maker
   * @return Informational String with parameters of this Scheduling Request
   */
  @Override
  public String toString() {
    return "SanitationRequest{"
            + "id=" + getId()
            + ", startTime=" + getStartTime()
            + ", endTime=" + getEndTime()
            + ", locationNode=" + getRoom().toString()
            + ", whoReserved=" + getWhoReserved()
            + '}';
  }

}
