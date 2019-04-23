package edu.wpi.cs3733.d19.teamO.entity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class SupportAnimalRequest extends ServiceRequest {
	public enum SupportAnimalRequestType {
		Chubbs("Chubbs"),
		Dora("Dora"),
		Marmalade("Marmalade"),
		Jackson("Jackson"),
		Jasper("Jasper"),
		Lois("Lois"),
		Lovey("Lovey"),
		Mimi("Mimi"),
		Oliver("Oliver");

		private static final Map<String,
				SupportAnimalRequest.SupportAnimalRequestType> lookup
				= new ConcurrentHashMap<>();

		static {
			for (SupportAnimalRequest.SupportAnimalRequestType type : values()) {
				lookup.put(type.name(), type);
			}
		}

		private final String name;

		SupportAnimalRequestType(final String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}

		public static SupportAnimalRequest
				.SupportAnimalRequestType get(final String name) {
			return lookup.get(name);
		}
	}

	private final String person;
	private final SupportAnimalRequestType type;

	/**
	 * Create a support animal request.
	 */
	public SupportAnimalRequest(int id, LocalDateTime timeRequested,
	                            LocalDateTime timeCompleted, Node location,
	                            String whoCompleted, SupportAnimalRequestType type,
	                            String description, String person) {
		super(id, timeRequested, timeCompleted, whoCompleted, description, location);
		this.type = type;
		this.person = person;
	}

	/**
	 * Initialization of support animal request.
	 */
	public SupportAnimalRequest(LocalDateTime timeRequested, Node location,
	                            SupportAnimalRequestType type, String description,
	                            String person) {
		super(timeRequested, description, location);
		this.type = type;
		this.person = person;
	}

	public SupportAnimalRequestType getType() {
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
		if (!(o instanceof SupportAnimalRequest)) {
			return false;
		}
		SupportAnimalRequest that = (SupportAnimalRequest) o;
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
		return "SupportAnimalRequest{"
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

