package edu.wpi.cs3733.d19.teamO.entity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class FloristRequest extends ServiceRequest {
  public enum FloristRequestType {
    ROSES("Roses"),
    TULIPS("Tulips"),
    DAFFODIL("Daffodil"),
    OTHERS("Others");

    private static final Map<String,
        FloristRequestType> lookup
        = new ConcurrentHashMap<>();

    static {
      for (FloristRequest.FloristRequestType type : values()) {
        lookup.put(type.name(), type);
      }
    }

    private final String name;

    FloristRequestType(final String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return name;
    }

    public static FloristRequest
        .FloristRequestType get(final String name) {
      return lookup.get(name);
    }
  }

  private final String person;
  private final FloristRequestType type;

  /**
   * Create a Florist Transportation Request.
   */
  public FloristRequest(int id, LocalDateTime timeRequested,
                        LocalDateTime timeCompleted, Node location,
                        String whoCompleted, FloristRequestType type,
                        String description, String person) {
    super(id, timeRequested, timeCompleted, whoCompleted, description, location);
    this.type = type;
    this.person = person;
  }

  /**
   * Initialization of Florist Transportation Request.
   */
  public FloristRequest(LocalDateTime timeRequested, Node location,
                        FloristRequestType type, String description,
                        String person) {
    super(timeRequested, description, location);
    this.type = type;
    this.person = person;
  }

  public FloristRequestType getType() {
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
    if (!(o instanceof FloristRequest)) {
      return false;
    }
    FloristRequest that = (FloristRequest) o;
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
    return "FloristRequest{"
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

