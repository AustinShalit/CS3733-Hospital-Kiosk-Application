package edu.wpi.cs3733.d19.teamO.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import com.calendarfx.model.Entry;
import com.google.common.base.MoreObjects;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@SuppressWarnings("PMD.TooManyMethods")
public class SchedulingRequest {

  private final IntegerProperty id = new SimpleIntegerProperty(-1);
  private final ObjectProperty<LocalDateTime> startTime = new SimpleObjectProperty<>();
  private final ObjectProperty<LocalDateTime> endTime = new SimpleObjectProperty<>();
  private final ObjectProperty<LocalDateTime> timeRequested = new SimpleObjectProperty<>();
  private final ObjectProperty<LocalDateTime> timeCompleted = new SimpleObjectProperty<>();
  private final StringProperty whoReserved = new SimpleStringProperty();
  private final ObjectProperty<Node> room = new SimpleObjectProperty<>();

  private final ReadOnlyObjectProperty<Entry<SchedulingRequest>> entry
      = new SimpleObjectProperty<>(new Entry<>());

  private SchedulingRequest() {
    entry.get().titleProperty().bind(whoReserved);
    startTime.addListener((observable, oldValue, newValue) -> {
      entry.get().changeStartDate(newValue.toLocalDate());
      entry.get().changeStartTime(newValue.toLocalTime());
    });
    endTime.addListener((observable, oldValue, newValue) -> {
      entry.get().changeEndDate(newValue.toLocalDate());
      entry.get().changeEndTime(newValue.toLocalTime());
    });
    entry.get().locationProperty().bind(
        Bindings.createStringBinding(() -> {
          if (room.get() == null) {
            return "Unknown";
          }

          return room.get().getLongName();
        }, room));
    entry.get().setUserObject(this);
  }

  /**
   * Constructor without id specified.
   *
   * @param startTime     Start time
   * @param endTime       End Time
   * @param timeRequested Time Request opened
   * @param timeCompleted Time Request approved
   * @param name          Who requested the room
   * @param room          Which room to reserve
   */
  public SchedulingRequest(LocalDateTime startTime, LocalDateTime endTime,
                           LocalDateTime timeRequested, LocalDateTime timeCompleted,
                           String name, Node room) {
    this();

    this.startTime.set(startTime);
    this.endTime.set(endTime);
    this.timeRequested.set(timeRequested);
    this.timeCompleted.set(timeCompleted);
    this.whoReserved.set(name);
    this.room.set(room);
  }

  /**
   * Constructor.
   *
   * @param id            The ID
   * @param startTime     Start time
   * @param endTime       End Time
   * @param timeRequested Time Request opened
   * @param timeCompleted Time Request approved
   * @param name          Who requested the room
   * @param room          Which room to reserve
   */
  public SchedulingRequest(int id, LocalDateTime startTime, LocalDateTime endTime,
                           LocalDateTime timeRequested, LocalDateTime timeCompleted,
                           String name, Node room) {
    this(startTime, endTime, timeRequested, timeCompleted, name, room);
    this.id.set(id);
  }

  public int getId() {
    return id.get();
  }

  public IntegerProperty idProperty() {
    return id;
  }

  public void setId(int id) {
    this.id.set(id);
  }

  public LocalDateTime getStartTime() {
    return startTime.get();
  }

  public ObjectProperty<LocalDateTime> startTimeProperty() {
    return startTime;
  }

  public void setStartTime(LocalDateTime startTime) {
    this.startTime.set(startTime);
  }

  public LocalDateTime getEndTime() {
    return endTime.get();
  }

  public ObjectProperty<LocalDateTime> endTimeProperty() {
    return endTime;
  }

  public void setEndTime(LocalDateTime endTime) {
    this.endTime.set(endTime);
  }

  public LocalDateTime getTimeRequested() {
    return timeRequested.get();
  }

  public ObjectProperty<LocalDateTime> timeRequestedProperty() {
    return timeRequested;
  }

  public void setTimeRequested(LocalDateTime timeRequested) {
    this.timeRequested.set(timeRequested);
  }

  public LocalDateTime getTimeCompleted() {
    return timeCompleted.get();
  }

  public ObjectProperty<LocalDateTime> timeCompletedProperty() {
    return timeCompleted;
  }

  public void setTimeCompleted(LocalDateTime timeCompleted) {
    this.timeCompleted.set(timeCompleted);
  }

  public String getWhoReserved() {
    return whoReserved.get();
  }

  public StringProperty whoReservedProperty() {
    return whoReserved;
  }

  public void setWhoReserved(String whoReserved) {
    this.whoReserved.set(whoReserved);
  }

  public Node getRoom() {
    return room.get();
  }

  public ObjectProperty<Node> roomProperty() {
    return room;
  }

  public void setRoom(Node room) {
    this.room.set(room);
  }

  public Entry<SchedulingRequest> getEntry() {
    return entry.get();
  }

  public ReadOnlyObjectProperty<Entry<SchedulingRequest>> entryProperty() {
    return entry;
  }

  /**
   * Used to check if the meeting is during a certain time.
   *
   * @param localDateTime The time to check
   * @return True if the meeting is during the supplied time, False otherwise.
   */
  public boolean isDuring(LocalDateTime localDateTime) {
    return (localDateTime.isAfter(startTime.get()) || localDateTime.isEqual(startTime.get()))
        && (localDateTime.isBefore(endTime.get()) || localDateTime.isEqual(endTime.get()));
  }

  /**
   * Used to check if the given time range is during this meeting.
   *
   * @param startTime the start of the time range
   * @param endTime   the end of the time range
   * @return True if the meeting is during the supplied time range, False otherwise.
   */
  public boolean isDuring(LocalDateTime startTime, LocalDateTime endTime) {
    if (this.endTime.get().isBefore(startTime)
        || this.endTime.get().isEqual(startTime)) {
      return false;
    }

    return !(endTime.isBefore(this.startTime.get())
        || endTime.isEqual(this.startTime.get()));
  }

  /**
   * Use to check if this scheduling request would happen at the same time and place as
   * the other scheduling request.
   *
   * @param schedulingRequest The other scheduling request.
   * @return True if there would be a conflict, false otherwise.
   */
  public boolean conflictsWith(SchedulingRequest schedulingRequest) {
    if (this.id.get() == schedulingRequest.id.get()) {
      return false;
    }

    if (!room.get().equals(schedulingRequest.room.get())) {
      return false;
    }

    return isDuring(schedulingRequest.startTime.get(), schedulingRequest.endTime.get());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SchedulingRequest that = (SchedulingRequest) o;
    return Objects.equals(id.get(), that.id.get())
        && Objects.equals(startTime.get(), that.startTime.get())
        && Objects.equals(endTime.get(), that.endTime.get())
        && Objects.equals(timeRequested.get(), that.timeRequested.get())
        && Objects.equals(timeCompleted.get(), that.timeCompleted.get())
        && Objects.equals(whoReserved.get(), that.whoReserved.get())
        && Objects.equals(room.get(), that.room.get());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id.get(), startTime.get(), endTime.get(), timeRequested.get(),
        timeCompleted.get(), whoReserved.get(), room.get());
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("startTime", startTime)
        .add("endTime", endTime)
        .add("timeRequested", timeRequested)
        .add("timeCompleted", timeCompleted)
        .add("whoReserved", whoReserved)
        .add("room", room)
        .toString();
  }
}
