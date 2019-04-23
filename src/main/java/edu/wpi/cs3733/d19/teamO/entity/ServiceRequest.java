package edu.wpi.cs3733.d19.teamO.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import com.google.common.base.MoreObjects;

class ServiceRequest {
	private int id;
	private final LocalDateTime timeRequested;
	private final LocalDateTime timeCompleted;
	private String whoCompleted;
	private final String description;
	private final Node location;

	public static LocalDateTime defaultTime() {
		return LocalDateTime.of(1, 1,
				1, 1, 1);
	}

	public static Node defaultNode() {
		return new Node(null, 0, 0,
				null, null, null, null, null);
	}

	/**
	 * The constructor for the service request class.
	 *
	 * @param id            This primary key for the ServiceRequest. -1 if ServiceRequest not in
	 *                      Database, and you wish the database to generate the id.
	 * @param timeRequested The time the service request was first requested.
	 * @param timeCompleted The time the service request was completed.
	 *                      Use Globals.defaultTime() instead of null
	 * @param whoCompleted  A string representation of whoever completed the Service Request.
	 * @param description   A description.
	 * @param location      A reference to the Node.
	 */
	ServiceRequest(int id, LocalDateTime timeRequested, LocalDateTime timeCompleted,
	               String whoCompleted, String description, Node location) {
		this.id = id;
		this.timeRequested = timeRequested;
		this.timeCompleted = timeCompleted;
		this.whoCompleted = whoCompleted;
		this.description = description;
		this.location = location;
	}

	ServiceRequest(int id, LocalDateTime timeRequested, String description, Node locationNode) {
		this.id = id;
		this.timeRequested = timeRequested;
		this.description = description;
		this.location = locationNode;

		this.timeCompleted = defaultTime();
		this.whoCompleted = "";
	}

	ServiceRequest(LocalDateTime timeRequested, String description, Node locationNode) {
		this.timeRequested = timeRequested;
		this.description = description;
		this.location = locationNode;

		this.id = -1;
		this.timeCompleted = defaultTime();
		this.whoCompleted = "";
	}

	ServiceRequest(int id, LocalDateTime timeRequested, LocalDateTime timeCompleted,
	               String description) {
		this.id = id;
		this.timeRequested = timeRequested;
		this.timeCompleted = timeCompleted;
		this.description = description;
		location = defaultNode();
	}

	ServiceRequest(int id) {
		this.id = id;

		this.timeRequested = defaultTime();
		this.timeCompleted = defaultTime();
		this.description = "";
		this.location = defaultNode();
		this.whoCompleted = "";
	}
	// getters and setters

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public LocalDateTime getTimeRequested() {
		return timeRequested;
	}

	public LocalDateTime getTimeCompleted() {
		return timeCompleted;
	}

	/**
	 * Return the time completed represented as a string, unless it equals
	 * the default time defined for this project.
	 */
	public String getTimeCompletedString() {
		if (defaultTime().equals(timeCompleted)) {
			return "";
		} else {
			return timeCompleted.toString();
		}
	}

	public String getWhoCompleted() {
		return whoCompleted;
	}

	public void setWhoCompleted(String whoCompleted) {
		this.whoCompleted = whoCompleted;
	}

	public String getDescription() {
		return description;
	}

	public Node getLocationNode() {
		return location;
	}

	public String getLocationNodeIdString() {
		return location.getNodeId();
	}

	public String getLocationNodeLongName() {
		return location.getLongName();
	}

	public int getTimeDifference() {
		if (timeRequested.isAfter(timeCompleted)) {
			return -1; //"Invalid time period";
		} else if (timeCompleted.minusMinutes(120).isAfter(timeRequested)) {
			return 1; //"Request over two hours";
		} else if (timeCompleted.minusMinutes(60).isAfter(timeRequested)) {
			return 2; //"Request from an hour to two hours";
		} else if (timeCompleted.minusMinutes(30).isAfter(timeRequested)) {
			return 3; //"Request from half hour to an hour";
		} else {
			return 4; //"Request less than half hour";
		}
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("id", id)
				.add("timeRequested", timeRequested)
				.add("timeCompleted", timeCompleted)
				.add("whoCompleted", whoCompleted)
				.add("description", description)
				.add("location", location)
				.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ServiceRequest that = (ServiceRequest) o;
		return id == that.id
				&& Objects.equals(timeRequested, that.timeRequested)
				&& Objects.equals(timeCompleted, that.timeCompleted)
				&& Objects.equals(whoCompleted, that.whoCompleted)
				&& Objects.equals(description, that.description)
				&& Objects.equals(location, that.location);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, timeRequested, timeCompleted, whoCompleted, description, location);
	}
}
