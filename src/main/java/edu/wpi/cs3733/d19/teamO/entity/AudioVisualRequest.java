package edu.wpi.cs3733.d19.teamO.entity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class AudioVisualRequest extends ServiceRequest {
  public enum AudioVisualRequestType {
    PROJECTOR("Projector"),
    SPEAKER("Speaker"),
    MIC("Mic"),
    OTHERS("Others");

    private static final Map<String,
        AudioVisualRequest.AudioVisualRequestType> lookup
        = new ConcurrentHashMap<>();

    static {
      for (AudioVisualRequest.AudioVisualRequestType type : values()) {
        lookup.put(type.name(), type);
      }
    }

    private final String name;

    AudioVisualRequestType(final String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return name;
    }

    public static AudioVisualRequest
        .AudioVisualRequestType get(final String name) {
      return lookup.get(name);
    }
  }

  private final String person;
  private final AudioVisualRequestType type;

  /**
   * Create a Audio Visual Request.
   */
  public AudioVisualRequest(int id, LocalDateTime timeRequested,
                            LocalDateTime timeCompleted, Node location,
                            String whoCompleted, AudioVisualRequestType type,
                            String description, String person) {
    super(id, timeRequested, timeCompleted, whoCompleted, description, location);
    this.type = type;
    this.person = person;
  }

  /**
   * Initialization of Audio Visual Request.
   */
  public AudioVisualRequest(LocalDateTime timeRequested, Node location,
                            AudioVisualRequestType type, String description,
                            String person) {
    super(timeRequested, description, location);
    this.type = type;
    this.person = person;
  }

  public AudioVisualRequestType getType() {
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
    if (!(o instanceof AudioVisualRequest)) {
      return false;
    }
    AudioVisualRequest that = (AudioVisualRequest) o;
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
    return "AudioVisualRequest{"
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

