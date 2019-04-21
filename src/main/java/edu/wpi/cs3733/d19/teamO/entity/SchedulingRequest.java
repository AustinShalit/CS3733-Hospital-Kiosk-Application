package edu.wpi.cs3733.d19.teamO.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class SchedulingRequest {

    private int id;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final LocalDateTime timeRequested;
    private final LocalDateTime timeCompleted;
    private final String whoReserved;
    private final Node room;

    /**
     * Constructor with id and full start and end times specified.
     *
     * @param id            ID
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
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.timeRequested = timeRequested;
        this.timeCompleted = timeCompleted;
        this.whoReserved = name;
        this.room = room;
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
        this.id = -1;
        this.startTime = startTime;
        this.endTime = endTime;
        this.timeRequested = timeRequested;
        this.timeCompleted = timeCompleted;
        this.whoReserved = name;
        this.room = room;
    }

    // Getters and Setters
    public int getId() {
        return this.id;
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    public LocalDateTime getTimeRequested() {
        return timeRequested;
    }

    public LocalDateTime getTimeCompleted() {
        return timeCompleted;
    }

    public String getWhoReserved() {
        return this.whoReserved;
    }

    public Node getRoom() {
        return room;
    }

    public String getRoomName() {
        return room.getShortName();
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Used to check if the meeting is during a certain time.
     *
     * @param localDateTime The time to check
     * @return True if the meeting is during the supplied time, False otherwise.
     */
    public boolean isDuring(LocalDateTime localDateTime) {
        return (localDateTime.isAfter(startTime) || localDateTime.isEqual(startTime))
                && (localDateTime.isBefore(endTime) || localDateTime.isEqual(endTime));
    }

    /**
     * Used to check if the given time range is during this meeting.
     *
     * @param startTime the start of the time range
     * @param endTime   the end of the time range
     * @return True if the meeting is during the supplied time range, False otherwise.
     */
    public boolean isDuring(LocalDateTime startTime, LocalDateTime endTime) {
        if (this.endTime.isBefore(startTime)
                || this.endTime.isEqual(startTime)) {
            return false;
        }

        return !(endTime.isBefore(this.startTime)
                || endTime.isEqual(this.startTime));
    }

    /**
     * Use to check if this scheduling request would happen at the same time and place as
     * the other scheduling request.
     *
     * @param schedulingRequest The other scheduling request.
     * @return True if there would be a conflict, false otherwise.
     */
    public boolean conflictsWith(SchedulingRequest schedulingRequest) {
        if (this.id == schedulingRequest.id) {
            return false;
        }

        if (!room.equals(schedulingRequest.room)) {
            return false;
        }

        return isDuring(schedulingRequest.startTime, schedulingRequest.endTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SchedulingRequest)) {
            return false;
        }
        SchedulingRequest that = (SchedulingRequest) o;
        return getId() == that.getId()
                && getStartTime().equals(that.getStartTime())
                && getEndTime().equals(that.getEndTime())
                && getTimeRequested().equals(that.getTimeRequested())
                && getTimeCompleted().equals(that.getTimeCompleted())
                && getRoom().equals(that.getRoom())
                && getWhoReserved().equals(that.getWhoReserved());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getStartTime(), getEndTime(), getTimeRequested(),
                getTimeCompleted(), getRoom(), getWhoReserved());
    }

    @Override
    public String toString() {
        return "SchedulingRequest{"
                + "id=" + getId()
                + ", startTime=" + getStartTime()
                + ", endTime=" + getEndTime()
                + ", timeRequested=" + getTimeRequested()
                + ", timeCompleted=" + getTimeCompleted()
                + ", locationNode=" + getRoom().toString()
                + ", whoReserved=" + getWhoReserved()
                + '}';
    }
}
