package edu.wpi.cs3733.d19.teamO.entity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class InterpreterRequest extends ServiceRequest {
	public enum Language {
		ENG("English"),
		SPA("Spanish"),
		POR("Portuguese"),
		ZHO("Chinese"),
		FRA("French"),
		ITA("Italian"),
		RUS("Russian"),
		VIE("Vietnamese"),
		ELL("Greek"),
		ARA("Arabic"),
		KHM("Cambodian/Khmer"),
		OTH("Others");

		private static final Map<String, InterpreterRequest.Language> lookup
				= new ConcurrentHashMap<>();

		static {
			for (InterpreterRequest.Language type : values()) {
				lookup.put(type.name(), type);
			}
		}

		private final String name;

		Language(final String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}

		public static InterpreterRequest.Language get(final String name) {
			return lookup.get(name);
		}
	}

	private final Language language;
	private final String name;

	/**
	 * Constructor.
	 *
	 * @param id            Id
	 * @param timeRequested Time 1
	 * @param timeCompleted Time 2
	 * @param whoCompleted  Who
	 * @param description   Describe
	 * @param node          Node
	 * @param language      Language
	 * @param name          Name
	 */
	public InterpreterRequest(int id, LocalDateTime timeRequested, LocalDateTime timeCompleted,
	                          String whoCompleted, String description, Node node, Language language,
	                          String name) {
		super(id, timeRequested, timeCompleted, whoCompleted, description, node);
		this.language = language;
		this.name = name;
	}

	/**
	 * Another constructor.
	 *
	 * @param timeRequested Time 1
	 * @param timeCompleted Time 2
	 * @param whoCompleted  Who
	 * @param description   Describe
	 * @param node          Where
	 * @param language      Language
	 * @param name          Name
	 */
	public InterpreterRequest(LocalDateTime timeRequested, LocalDateTime timeCompleted,
	                          String whoCompleted, String description, Node node, Language language,
	                          String name) {
		super(-1, timeRequested, timeCompleted, whoCompleted, description, node);
		this.language = language;
		this.name = name;
	}

	/**
	 * Yet another constructor.
	 *
	 * @param timeRequested Time 1
	 * @param locationNode  Place
	 * @param language      Language
	 * @param description   Describe
	 * @param name          Name
	 */
	public InterpreterRequest(LocalDateTime timeRequested, Node locationNode,
	                          Language language, String description, String name) {
		super(timeRequested, description, locationNode);
		this.language = language;
		this.name = name;
	}

	public Language getLanguage() {
		return this.language;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof InterpreterRequest)) {
			return false;
		}
		InterpreterRequest that = (InterpreterRequest) o;
		return getId() == that.getId()
				&& getTimeRequested().equals(that.getTimeRequested())
				&& getTimeCompleted().equals(that.getTimeCompleted())
				&& getWhoCompleted().equals(that.getWhoCompleted())
				&& getDescription().equals(that.getDescription())
				&& getLocationNode().equals(that.getLocationNode())
				&& getLanguage() == that.getLanguage()
				&& getName().equals(that.getName());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getTimeRequested(), getTimeCompleted(), getLocationNode(),
				getWhoCompleted(), getLanguage(), getDescription());
	}

	@Override
	public String toString() {
		return "InterpreterRequest{"
				+ "id=" + getId()
				+ ", timeRequested=" + getTimeRequested()
				+ ", timeCompleted=" + getTimeCompleted()
				+ ", locationNode=" + getLocationNode().toString()
				+ ", whoCompleted=" + getWhoCompleted()
				+ ", language=" + getLanguage()
				+ ", description='" + getDescription()
				+ ", name=" + getName() + '\''
				+ '}';
	}
}
