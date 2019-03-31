package edu.wpi.cs3733.d19.teamO.entity;

import java.time.LocalDateTime;

public class SanitationRequest extends ServiceRequest {

  public enum SanitationRequestType {
    SPILL("Spill"),
    VOMIT("Vomit"),
    BEDDING("Bedding"),
    OTHERS("Others");

    private final String name;

    SanitationRequestType(final String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return name;
    }
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


