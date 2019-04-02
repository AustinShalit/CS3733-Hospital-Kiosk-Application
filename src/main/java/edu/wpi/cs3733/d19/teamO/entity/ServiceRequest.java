package edu.wpi.cs3733.d19.teamO.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import com.google.common.base.MoreObjects;

class ServiceRequest {
  private Integer id;
  private final LocalDateTime timeRequested;
  private final LocalDateTime timeCompleted;
  private final String whoCompleted;
  private final String description;
  private final Node location;

  // constructor
  ServiceRequest(Integer id, LocalDateTime timeRequested, LocalDateTime timeCompleted,
                 String whoCompleted, String description, Node location) {
    this.id = id;
    this.timeRequested = timeRequested;
    this.timeCompleted = timeCompleted;
    this.whoCompleted = whoCompleted;
    this.description = description;
    this.location = location;
  }

  // getters and setters

  public void setId(int id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  public LocalDateTime getTimeRequested() {
    return timeRequested;
  }

  public LocalDateTime getTimeCompleted() {
    return timeCompleted;
  }

  public String getWhoCompleted() {
    return whoCompleted;
  }

  public String getDescription() {
    return description;
  }

  // functions
  public Node getLocationNode() {
    return location;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("timeRequested", timeRequested)
        .add("timeCompleted", timeCompleted)
        .add("whoCompleted", whoCompleted)
        .add("description", description)
        .add("location", location)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServiceRequest that = (ServiceRequest) o;
    return id == that.id
        && Objects.equals(timeRequested, that.timeRequested)
        && Objects.equals(timeCompleted, that.timeCompleted)
        && Objects.equals(whoCompleted, that.whoCompleted)
        && Objects.equals(description, that.description)
        && Objects.equals(location, that.location);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, timeRequested, timeCompleted, whoCompleted, description, location);
  }
}
