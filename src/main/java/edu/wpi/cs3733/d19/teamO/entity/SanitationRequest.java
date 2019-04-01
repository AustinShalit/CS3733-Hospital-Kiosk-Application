package edu.wpi.cs3733.d19.teamO.entity;

import java.time.LocalDateTime;

public class SanitationRequest extends ServiceRequest {

  public enum SanitationRequestType {
    SPILL,
    VOMIT,
    BEDDING,
    OTHERS;
  }

  private final SanitationRequestType type;

  // constructor
  SanitationRequest(LocalDateTime timeRequested, LocalDateTime timeCompleted, String whoCompleted,
                    String description, Node node, SanitationRequestType type) {
    super(timeRequested, timeCompleted, whoCompleted, description, node);
    this.type = type;
  }

  public SanitationRequestType getType() {
    return type;
  }
}
