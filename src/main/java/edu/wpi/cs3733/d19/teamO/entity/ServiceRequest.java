package edu.wpi.cs3733.d19.teamO.entity;

import java.time.LocalDateTime;

class ServiceRequest {

    private final LocalDateTime timeRequested;
    private final LocalDateTime timeCompleted;
    private final String whoCompleted;
    private final String description;
    private final Node node;

    // constructor
    ServiceRequest(LocalDateTime timeRequested, LocalDateTime timeCompleted, String whoCompleted, String description, Node node){
        this.timeRequested = timeRequested;
        this.timeCompleted = timeCompleted;
        this.whoCompleted = whoCompleted;
        this.description = description;
        this.node = node;
    }

    // getters and setters
    public LocalDateTime getTimeRequested() {
        return timeRequested;
    }

    public LocalDateTime getTimeCompleted() {
        return timeCompleted;
    }

    public String getWhoCompleted() {
        return whoCompleted;
    }

    public String getDescription() {
        return description;
    }

    // functions
    public Node getLocationNode(){
            return node;
    }
}
