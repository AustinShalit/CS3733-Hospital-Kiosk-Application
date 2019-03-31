package edu.wpi.cs3733.d19.teamO.entity;

import java.time.LocalDateTime;

class ServiceRequest {

  private final int id;
  private final Node locationNode;
  private final LocalDateTime timeReceived;
  private final LocalDateTime timeCompleted;

  // constructor
  ServiceRequest(int id, LocalDateTime timeReceived, LocalDateTime timeCompleted, Node locationNode) {
    this.id = id;
    this.locationNode = locationNode;
    this.timeReceived = timeReceived;
    this.timeCompleted = timeCompleted;
  }

  // getters and setters
  public int getId() {
    return this.id;
  }

  public LocalDateTime getTimeReceived() {
    return this.timeReceived;
  }

  public LocalDateTime getTimeCompleted() {
    return this.timeCompleted;
  }

  // functions
  public Node getLocationNode() {
    return this.locationNode;
  }
}
