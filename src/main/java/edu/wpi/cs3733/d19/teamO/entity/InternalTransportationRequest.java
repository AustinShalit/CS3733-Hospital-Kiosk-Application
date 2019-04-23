package edu.wpi.cs3733.d19.teamO.entity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class InternalTransportationRequest extends ServiceRequest {
	public enum InternalTransportationRequestType {
		STRETCHER("Stretcher"),
		DISABLED("Disabled"),
		DELIVERY("Delivery"),
		OTHERS("Others");

		private static final Map<String,
				InternalTransportationRequest.InternalTransportationRequestType> lookup
				= new ConcurrentHashMap<>();

		static {
			for (InternalTransportationRequest.InternalTransportationRequestType type : values()) {
				lookup.put(type.name(), type);
			}
		}

		private final String name;

		InternalTransportationRequestType(final String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}

		public static InternalTransportationRequest
				.InternalTransportationRequestType get(final String name) {
			return lookup.get(name);
		}
	}

	private final String person;
	private final InternalTransportationRequestType type;

	/**
	 * Create a Internal Transportation Request.
	 */
	public InternalTransportationRequest(int id, LocalDateTime timeRequested,
	                                     LocalDateTime timeCompleted, Node location,
	                                     String whoCompleted, InternalTransportationRequestType type,
	                                     String description, String person) {
		super(id, timeRequested, timeCompleted, whoCompleted, description, location);
		this.type = type;
		this.person = person;
	}

	/**
	 * Initialization of Internal Transportation Request.
	 */
	public InternalTransportationRequest(LocalDateTime timeRequested, Node location,
	                                     InternalTransportationRequestType type, String description,
	                                     String person) {
		super(timeRequested, description, location);
		this.type = type;
		this.person = person;
	}

	public InternalTransportationRequestType getType() {
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
		if (!(o instanceof InternalTransportationRequest)) {
			return false;
		}
		InternalTransportationRequest that = (InternalTransportationRequest) o;
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
		return "InternalTransportationRequest{"
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

