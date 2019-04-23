package edu.wpi.cs3733.d19.teamO.entity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ReligiousServiceRequest extends ServiceRequest {
	public enum ReligiousServiceRequestType {
		//need to change
		//smera
		BLESSING("Blessing"),
		LAST_RITES("Last Rites"),
		PRAYER_FOR_HEALING("Prayer for Healing"),
		OTHERS("Others");

		private static final Map<String,
				ReligiousServiceRequest.ReligiousServiceRequestType> lookup
				= new ConcurrentHashMap<>();

		static {
			for (ReligiousServiceRequest.ReligiousServiceRequestType type : values()) {
				lookup.put(type.name(), type);
			}
		}

		private final String name;

		ReligiousServiceRequestType(final String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}

		public static ReligiousServiceRequest
				.ReligiousServiceRequestType get(final String name) {
			return lookup.get(name);
		}
	}

	private final String person;
	private final ReligiousServiceRequestType type;

	/**
	 * Create a Religious Service Request.
	 */
	public ReligiousServiceRequest(int id, LocalDateTime timeRequested,
	                               LocalDateTime timeCompleted, Node location,
	                               String whoCompleted, ReligiousServiceRequestType type,
	                               String description, String person) {
		super(id, timeRequested, timeCompleted, whoCompleted, description, location);
		this.type = type;
		this.person = person;
	}

	/**
	 * Initialization of Religious Service Request.
	 */
	public ReligiousServiceRequest(LocalDateTime timeRequested, Node location,
	                               ReligiousServiceRequestType type, String description,
	                               String person) {
		super(timeRequested, description, location);
		this.type = type;
		this.person = person;
	}

	public ReligiousServiceRequestType getType() {
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
		if (!(o instanceof ReligiousServiceRequest)) {
			return false;
		}
		ReligiousServiceRequest that = (ReligiousServiceRequest) o;
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
		return "ReligiousServiceRequest{"
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

