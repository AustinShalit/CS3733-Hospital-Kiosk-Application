package edu.wpi.cs3733.d19.teamO.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

class SchedulingRequest {

  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private String whoReserved;
  private Node room;

  // Constructors
  SchedulingRequest(LocalDateTime startTime, LocalDateTime endTime, String name, Node room) {
    this.startTime = startTime;
    this.endTime = endTime;
    this.whoReserved = name;
    this.room = room;
  }

  SchedulingRequest(LocalTime startTime, LocalTime endTime, LocalDate date, String whoReserved,
                    Node room) {
    this.startTime = LocalDateTime.of(date, startTime);
    this.endTime = LocalDateTime.of(date, endTime);
    this.whoReserved = whoReserved;
    this.room = room;
  }

  // Getters and Setters
  public LocalDateTime getStartTime() {
    return this.startTime;
  }

  public LocalDateTime getEndTime() {
    return this.endTime;
  }

  public String getWhoReserved() {
    return this.whoReserved;
  }

  public Node getRoom() {
    return room;
  }

  public void setStartTime(LocalDateTime startTime) {
    this.startTime = startTime;
  }

  public void setEndTime(LocalDateTime endTime) {
    this.endTime = endTime;
  }

  public void setWhoReserved(String name) {
    this.whoReserved = name;
  }

  public void setRoom(Node room) {
    this.room = room;
  }


}
