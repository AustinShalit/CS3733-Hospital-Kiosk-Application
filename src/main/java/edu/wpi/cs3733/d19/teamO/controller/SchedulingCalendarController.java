package edu.wpi.cs3733.d19.teamO.controller;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;

import edu.wpi.cs3733.d19.teamO.entity.SchedulingRequest;

@FxmlController(url = "SchedulingCalendar.fxml")
public class SchedulingCalendarController implements Controller {

	@FXML
	private CalendarView root;

	private final CalendarSource calendarSource = new CalendarSource("Database");

	private final ListProperty<SchedulingRequest> requests = new SimpleListProperty<>();

	/**
	 * Create a new Controller.
	 */
	public SchedulingCalendarController() {
		requests.addListener((ListChangeListener<SchedulingRequest>) c -> {
			while (c.next()) {
				c.getAddedSubList().stream()
						.map(SchedulingRequest::getEntry)
						.forEach(this::addEntry);
				c.getRemoved().stream()
						.map(SchedulingRequest::getEntry)
						.forEach(this::removeEntry);
			}
		});
	}

	@FXML
	void initialize() {
		root.setShowAddCalendarButton(false);  // Do not allow users to add calendars
		root.setShowPrintButton(false);  // Printing is broken on Java 8
		root.getCalendarSources().clear();  // Remove default calendar

		root.getCalendarSources().add(calendarSource);
	}

	public ObservableList<SchedulingRequest> getRequests() {
		return requests.get();
	}

	public ListProperty<SchedulingRequest> requestsProperty() {
		return requests;
	}

	public void setRequests(ObservableList<SchedulingRequest> requests) {
		this.requests.set(requests);
	}

	private void addEntry(final Entry<SchedulingRequest> entry) {
		calendarSource.getCalendars()
				.stream()
				.filter(calendar -> calendar.getName().equals(entry.getLocation()))
				.findFirst()
				.orElseGet(() -> {
					Calendar calendar = new Calendar(entry.getLocation());
					calendarSource.getCalendars().add(calendar);
					return calendar;
				})
				.addEntries(entry);
	}

	private void removeEntry(final Entry<SchedulingRequest> entry) {
		entry.removeFromCalendar();
	}

	@Override
	public Parent getRoot() {
		return root;
	}

	public interface Factory {
		SchedulingCalendarController create();
	}
}
