package edu.wpi.cs3733.d19.teamO.entity;

import java.time.LocalDateTime;

class ServiceRequest {

  private final int id;
  private final Node locationNode;
  private final LocalDateTime timeRequested;
  private final LocalDateTime timeCompleted;

  // constructor
  ServiceRequest(int id, LocalDateTime timeRequested, LocalDateTime timeCompleted, Node locationNode) {
    this.id = id;
    this.locationNode = locationNode;
    this.timeRequested = timeRequested;
    this.timeCompleted = timeCompleted;
  }

  // getters and setters
  public int getId() {
    return this.id;
  }

  public LocalDateTime getTimeRequested() {
    return this.timeRequested;
  }

  public LocalDateTime getTimeCompleted() {
    return this.timeCompleted;
  }

  // functions
  public Node getLocationNode() {
    return this.locationNode;
  }
}
