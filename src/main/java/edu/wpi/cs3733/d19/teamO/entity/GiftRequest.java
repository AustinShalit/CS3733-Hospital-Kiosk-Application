package edu.wpi.cs3733.d19.teamO.entity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class GiftRequest extends ServiceRequest {
    public enum GiftRequestType {
        TEDDYBEAR("TeddyBear"),
        CANDY("Candy"),
        BALLOONS("Balloons"),
        OTHERS("Others");

        private static final Map<String,
                GiftRequest.GiftRequestType> lookup
                = new ConcurrentHashMap<>();

        static {
            for (GiftRequest.GiftRequestType type : values()) {
                lookup.put(type.name(), type);
            }
        }

        private final String name;

        GiftRequestType(final String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        public static GiftRequest
                .GiftRequestType get(final String name) {
            return lookup.get(name);
        }
    }

    private final String person;
    private final GiftRequestType type;

    /**
     * Create a Gift Request.
     */
    public GiftRequest(int id, LocalDateTime timeRequested,
                       LocalDateTime timeCompleted, Node location,
                       String whoCompleted, GiftRequestType type,
                       String description, String person) {
        super(id, timeRequested, timeCompleted, whoCompleted, description, location);
        this.type = type;
        this.person = person;
    }

    /**
     * Initialization of Gift Request.
     */
    public GiftRequest(LocalDateTime timeRequested, Node location,
                       GiftRequestType type, String description,
                       String person) {
        super(timeRequested, description, location);
        this.type = type;
        this.person = person;
    }

    public GiftRequestType getType() {
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
        if (!(o instanceof GiftRequest)) {
            return false;
        }
        GiftRequest that = (GiftRequest) o;
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
        return "GiftRequest{"
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

