package edu.wpi.cs3733.d19.teamO.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class SchedulingRequest {

  private int id;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private LocalDateTime timeRequested;
  private LocalDateTime timeCompleted;
  private String whoReserved;
  private Node room;

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
  public int getID() {
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

  public void setID(int id) {
    this.id = id;
  }

  public void setStartTime(LocalDateTime startTime) {
    this.startTime = startTime;
  }

  public void setEndTime(LocalDateTime endTime) {
    this.endTime = endTime;
  }

  public void setTimeRequested(LocalDateTime timeRequested) {
    this.timeRequested = timeRequested;
  }

  public void setTimeCompleted(LocalDateTime timeCompleted) {
    this.timeCompleted = timeCompleted;
  }

  public void setWhoReserved(String name) {
    this.whoReserved = name;
  }

  public void setRoom(Node room) {
    this.room = room;
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
    return getID() == that.getID()
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
    return Objects.hash(getID(), getStartTime(), getEndTime(), getTimeRequested(), getTimeCompleted(), getRoom(),
            getWhoReserved());
  }

  /**
   * Informational String Maker
   * @return Informational String with parameters of this Scheduling Request
   */
  @Override
  public String toString() {
    return "SanitationRequest{"
            + "id=" + getID()
            + ", startTime=" + getStartTime()
            + ", endTime=" + getEndTime()
            + ", locationNode=" + getRoom().toString()
            + ", whoReserved=" + getWhoReserved()
            + '}';
  }

}
