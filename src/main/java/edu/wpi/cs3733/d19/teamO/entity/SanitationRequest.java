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

    private static final Map<String,
        SanitationRequestType> lookup
        = new ConcurrentHashMap<>();

    static {
      for (SanitationRequestType type : values()) {
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

    public static SanitationRequestType get(final String name) {
      return lookup.get(name);
    }
  }

  private final String person;
  private final SanitationRequestType type;

  /**
   * Create a Sanitation Request.
   */
  public SanitationRequest(int id, LocalDateTime timeRequested,
                           LocalDateTime timeCompleted, Node location,
                           String whoCompleted, SanitationRequestType type,
                           String description, String person) {
    super(id, timeRequested, timeCompleted, whoCompleted, description, location);
    this.type = type;
    this.person = person;
  }

  /**
   * Initialization of Sanitation Request.
   */
  public SanitationRequest(LocalDateTime timeRequested, Node location,
                           SanitationRequestType type, String description,
                           String person) {
    super(timeRequested, description, location);
    this.type = type;
    this.person = person;
  }

  public SanitationRequestType getType() {
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
    if (!(o instanceof SanitationRequest)) {
      return false;
    }
    SanitationRequest that = (SanitationRequest) o;
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
    return "SanitationRequest{"
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

