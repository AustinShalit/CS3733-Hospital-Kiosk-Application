package edu.wpi.cs3733.d19.teamO.entity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ExternalTransportationRequest extends ServiceRequest {
  public enum ExternalTransportationRequestType {
    AMBULANCE("Ambulance"),
    HELICOPTER("Helicopter"),
    OTHERS("Others");

    private static final Map<String,
        ExternalTransportationRequest.ExternalTransportationRequestType> lookup
        = new ConcurrentHashMap<>();

    static {
      for (ExternalTransportationRequest.ExternalTransportationRequestType type : values()) {
        lookup.put(type.name(), type);
      }
    }

    private final String name;

    ExternalTransportationRequestType(final String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return name;
    }

    public static ExternalTransportationRequest
        .ExternalTransportationRequestType get(final String name) {
      return lookup.get(name);
    }
  }

  private final String person;
  private final ExternalTransportationRequestType type;

  /**
   * Create a External Transportation Request.
   */
  public ExternalTransportationRequest(int id, LocalDateTime timeRequested,
                                       LocalDateTime timeCompleted, Node location,
                                       String whoCompleted, ExternalTransportationRequestType type,
                                       String description, String person) {
    super(id, timeRequested, timeCompleted, whoCompleted, description, location);
    this.type = type;
    this.person = person;
  }

  /**
   * Initialization of External Transportation Request.
   */
  public ExternalTransportationRequest(LocalDateTime timeRequested, Node location,
                                       ExternalTransportationRequestType type, String description,
                                       String person) {
    super(timeRequested, description, location);
    this.type = type;
    this.person = person;
  }

  public ExternalTransportationRequestType getType() {
    return type;
  }

  public String getPerson() {
    return person;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ExternalTransportationRequest)) {
      return false;
    }
    ExternalTransportationRequest that = (ExternalTransportationRequest) o;
    return getId() == that.getId()
        && getTimeRequested().equals(that.getTimeRequested())
        && getTimeCompleted().equals(that.getTimeCompleted())
        && getLocationNode().equals(that.getLocationNode())
        && getWhoCompleted().equals(that.getWhoCompleted())
        && type == that.type
        && getDescription().equals(that.getDescription())
        && person.equals(that.person);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getTimeRequested(), getTimeCompleted(), getLocationNode(),
        getWhoCompleted(), type, getDescription(), person);
  }

  @Override
  public String toString() {
    return "ExternalTransportationRequest{"
        + "id=" + getId()
        + ", timeRequested=" + getTimeRequested()
        + ", timeCompleted=" + getTimeCompleted()
        + ", locationNode=" + getLocationNode().toString()
        + ", whoCompleted=" + getWhoCompleted()
        + ", type=" + type
        + ", description=" + getDescription()
        + ", personName=" + person + '\''
        + '}';
  }

}

