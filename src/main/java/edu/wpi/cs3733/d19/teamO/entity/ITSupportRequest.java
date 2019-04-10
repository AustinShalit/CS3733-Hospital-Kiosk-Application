package edu.wpi.cs3733.d19.teamO.entity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ITSupportRequest extends ServiceRequest {
  public enum ITSupportRequestType {
    COMPUTER("Computer"),
    SOFTWARE("Software"),
    EQUIPMENT("Equipment"),
    OTHERS("Others");

    private static final Map<String,
        ITSupportRequest.ITSupportRequestType> lookup
        = new ConcurrentHashMap<>();

    static {
      for (ITSupportRequest.ITSupportRequestType type : values()) {
        lookup.put(type.name(), type);
      }
    }

    private final String name;

    ITSupportRequestType(final String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return name;
    }

    public static ITSupportRequest
        .ITSupportRequestType get(final String name) {
      return lookup.get(name);
    }
  }

  private final String person;
  private final ITSupportRequestType type;

  /**
   * Create a IT Support Request.
   */
  public ITSupportRequest(int id, LocalDateTime timeRequested,
                          LocalDateTime timeCompleted, Node location,
                          String whoCompleted, ITSupportRequestType type,
                          String description, String person) {
    super(id, timeRequested, timeCompleted, whoCompleted, description, location);
    this.type = type;
    this.person = person;
  }

  /**
   * Initialization of IT Support Request.
   */
  public ITSupportRequest(LocalDateTime timeRequested, Node location,
                          ITSupportRequestType type, String description,
                          String person) {
    super(timeRequested, description, location);
    this.type = type;
    this.person = person;
  }

  public ITSupportRequestType getType() {
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
    if (!(o instanceof ITSupportRequest)) {
      return false;
    }
    ITSupportRequest that = (ITSupportRequest) o;
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
    return "ITSupportRequest{"
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

