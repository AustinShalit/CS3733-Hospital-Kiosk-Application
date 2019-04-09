package edu.wpi.cs3733.d19.teamO.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class PatientInfoRequest extends ServiceRequest {

  private final String patientName;
  private final LocalDate patientDOB;
  private final PatientInfoSex patientSex;


  /**
   * Initialization of Patient Info Object.
   */
  public PatientInfoRequest(int id,
                            LocalDateTime timeRequested,
                            LocalDateTime timeCompleted,
                            Node location,
                            String whoCompleted,
                            String description,
                            String patientName,
                            LocalDate patientDOB,
                            PatientInfoSex patientSex) {
    super(id, timeRequested, timeCompleted, whoCompleted, description, location);
    this.patientName = patientName;
    this.patientDOB = patientDOB;
    this.patientSex = patientSex;
  }

  /**
   * Initialization of Patient Info Object.
   */
  public PatientInfoRequest(LocalDateTime timeRequested,
                            Node location,
                            String description,
                            String patientName,
                            LocalDate patientDOB,
                            PatientInfoSex patientSex) {
    super(timeRequested, description, location);
    this.patientName = patientName;
    this.patientDOB = patientDOB;
    this.patientSex = patientSex;
  }

  public String getPatientName() {
    return patientName;
  }

  public LocalDate getPatientDOB() {
    return patientDOB;
  }

  public PatientInfoSex getPatientSex() {
    return patientSex;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PatientInfoRequest)) {
      return false;
    }
    PatientInfoRequest that = (PatientInfoRequest) o;
    return getId() == that.getId()
        && getTimeRequested().equals(that.getTimeRequested())
        && getTimeCompleted().equals(that.getTimeCompleted())
        && getLocationNode().equals(that.getLocationNode())
        && getWhoCompleted().equals(that.getWhoCompleted())
        && getDescription().equals(that.getDescription())
        && patientName.equals(that.patientName)
        && patientDOB.equals(that.patientDOB)
        && patientSex == that.patientSex;
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getTimeRequested(), getTimeCompleted(), getLocationNode(),
        getWhoCompleted(), getDescription(), patientName, patientDOB, patientSex);
  }

  @Override
  public String toString() {
    return "PatientInfoRequest{"
        + "id=" + getId()
        + ", timeRequested=" + getTimeRequested()
        + ", timeCompleted=" + getTimeCompleted()
        + ", locationNode=" + getLocationNode().toString()
        + ", whoCompleted=" + getWhoCompleted()
        + ", description=" + getDescription()
        + ", patientName=" + patientName
        + ", patientDOB=" + patientDOB.toString()
        + ", patientSex=" + patientSex.toString() + '\''
        + '}';
  }

  public enum PatientInfoSex {
    MALE("Male"),
    FEMALE("Female");

    private static final Map<
        String,
        PatientInfoSex> lookup = new ConcurrentHashMap<>();

    static {
      for (PatientInfoSex sex : values()) {
        lookup.put(sex.name, sex);
      }
    }

    private final String name;

    PatientInfoSex(final String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return name;
    }

    public static PatientInfoRequest.PatientInfoSex get(final String name) {
      return lookup.get(name);
    }
  }

}

