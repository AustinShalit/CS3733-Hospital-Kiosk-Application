package edu.wpi.cs3733.d19.teamO.entity;

import java.time.LocalDateTime;

class ServiceRequest {

  private final int id;
  private final Node node;
  private final LocalDateTime timeReceived;
  private final LocalDateTime timeCompleted;

  // constructor
  ServiceRequest(int id, LocalDateTime timeReceived, LocalDateTime timeCompleted, Node node) {
    this.id = id;
    this.node = node;
    this.timeReceived = timeReceived;
    this.timeCompleted = timeCompleted;
  }

  // getters and setters
  public int getId() {
    return id;
  }

  public LocalDateTime getTimetimeReceived() {
    return timeReceived;
  }

  public LocalDateTime getTimeCompleted() {
    return timeCompleted;
  }

  // functions
  public Node getLocationNode() {
    return node;
  }
}
