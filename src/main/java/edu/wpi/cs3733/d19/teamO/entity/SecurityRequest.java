package edu.wpi.cs3733.d19.teamO.entity;

import java.time.LocalDateTime;

public class SecurityRequest extends ServiceRequest {


  public SecurityRequest(int id, LocalDateTime timeRequested, LocalDateTime timeCompleted,
                         String whoCompleted, String description, Node node) {
    super(id, timeRequested, timeCompleted, whoCompleted, description, node);
  }

  public SecurityRequest(LocalDateTime timeRequested, LocalDateTime timeCompleted,
                         String whoCompleted, String description, Node node) {
    super(null, timeRequested, timeCompleted, whoCompleted, description, node);
  }
}


