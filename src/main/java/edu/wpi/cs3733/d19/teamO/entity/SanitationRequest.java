package edu.wpi.cs3733.d19.teamO.entity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class SanitationRequest extends ServiceRequest {

  public enum SanitationRequestType {
    SPILL("Spill"),
    VOMIT("Vomit"),
    BEDDING("Bedding"),
    OTHERS("Others");

    private static final Map<String, SanitationRequest.SanitationRequestType> lookup
        = new ConcurrentHashMap<>();

    static {
      for (SanitationRequest.SanitationRequestType type : values()) {
        lookup.put(type.name(), type);
      }
    }

    private final String name;

    SanitationRequestType(final String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return name;
    }

    public static SanitationRequest.SanitationRequestType get(final String name) {
      return lookup.get(name);
    }
  }

  private final SanitationRequestType type;
  private final String description;

  /**
   * Create a SanitationRequest.
   */
  public SanitationRequest(int id, LocalDateTime timeRequested, LocalDateTime timeCompleted,
                           Node locationNodeID,
                           SanitationRequestType type, String description) {
    super(id, timeRequested, timeCompleted, locationNodeID);
    this.type = type;
    this.description = description;
  }

  public SanitationRequestType getType() {
    return type;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof SanitationRequest)) return false;
    SanitationRequest that = (SanitationRequest) o;
    return getId() == that.getId() &&
        getTimeReceived().equals(that.getTimeReceived()) &&
        getTimeCompleted().equals(that.getTimeCompleted()) &&
        getLocationNode().equals(that.getLocationNode()) &&
        type == that.type &&
        description.equals(that.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getTimeReceived(), getTimeCompleted(), getLocationNode(), type, description);
  }

}
