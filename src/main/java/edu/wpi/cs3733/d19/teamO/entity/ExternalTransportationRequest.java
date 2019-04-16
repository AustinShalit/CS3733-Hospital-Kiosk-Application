package edu.wpi.cs3733.d19.teamO.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
  private final LocalTime timePicked;
  private final LocalDate datePicked;

  /**
   * Create a External Transportation Request.
   */
  public ExternalTransportationRequest(int id, LocalDateTime timeRequested,
                                       LocalDateTime timeCompleted, Node location,
                                       String whoCompleted, ExternalTransportationRequestType type,
                                       String description, String person, LocalDate datePicked, LocalTime timePicked) {
    super(id, timeRequested, timeCompleted, whoCompleted, description, location);
    this.type = type;
    this.person = person;
    this.timePicked = timePicked;
    this.datePicked = datePicked;
  }

  /**
   * Initialization of External Transportation Request.
   */
  public ExternalTransportationRequest(LocalDateTime timeRequested, Node location,
                                       ExternalTransportationRequestType type, String description,
                                       String person, LocalDate datePicked, LocalTime timePicked) {
    super(timeRequested, description, location);
    this.type = type;
    this.person = person;
    this.timePicked = timePicked;
    this.datePicked = datePicked;
  }

  public ExternalTransportationRequestType getType() {
    return type;
  }

  public String getPerson() {
    return person;
  }

  public LocalDate getDatePicked(){
    return datePicked;
  }

  public LocalTime getTimePicked(){
    return timePicked;
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
        && getTimePicked().equals(that.getTimePicked())
        && getDatePicked().equals(that.getDatePicked())
        && getDescription().equals(that.getDescription())
        && person.equals(that.person);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getTimeRequested(), getTimeCompleted(), getLocationNode(),
        getWhoCompleted(), type, timePicked, datePicked, getDescription(), person);
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
        + ", timePicked" + timePicked
        + ", datePicked" + datePicked
        + ", description=" + getDescription()
        + ", personName=" + person + '\''
        + '}';
  }
}

