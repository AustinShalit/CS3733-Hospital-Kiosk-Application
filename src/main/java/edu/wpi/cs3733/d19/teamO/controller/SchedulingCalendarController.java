package edu.wpi.cs3733.d19.teamO.controller;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import com.calendarfx.view.CalendarView;
import com.google.inject.Inject;

import javafx.fxml.FXML;
import javafx.scene.Parent;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.SchedulingRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "SchedulingCalendar.fxml")
public class SchedulingCalendarController implements Controller {

  @FXML
  private CalendarView root;
  @Inject
  private Database database;

  @FXML
  void initialize() {
    root.setShowAddCalendarButton(false);  // Do not allow users to add calendars
    root.getCalendarSources().clear();  // Remove default calendar

    Collection<Calendar> calendars = getCalendarsForNodes();
    styleCalendars(calendars);
    CalendarSource calendarSource = new CalendarSource("Database");
    calendarSource.getCalendars().addAll(calendars);
    root.getCalendarSources().add(calendarSource);
  }

  private Collection<Calendar> getCalendarsForNodes() {
    Collection<SchedulingRequest> reservations = database.getAllSchedulingRequests();
    return database.getAllNodes().stream()
        .filter(node -> node.getNodeType().isSchedulable())  // Rooms we can schedule
        .sorted(Comparator.comparing(Node::getLongName))  // Calendar list should be sorted by name
        .map(node -> {
          Calendar calendar = new Calendar(node.getLongName());
          calendar.setReadOnly(true);  // Do not allow user to edit
          calendar.addEntries(
              reservations.stream()
                  .filter(reservation -> reservation.getRoom().equals(node)) // Get our reservations
                  .map(reservation -> {
                    Entry<SchedulingRequest> entry = new Entry<>(reservation.getWhoReserved(),
                        new Interval(reservation.getStartTime(), reservation.getEndTime()));
                    entry.setUserObject(reservation);
                    entry.setLocation(node.getLongName());
                    return entry;
                  }).collect(Collectors.toList()));
          return calendar;
        }).collect(Collectors.toList());
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    SchedulingCalendarController create();
  }

  private static void styleCalendars(Collection<Calendar> calendars) {
    int style = 0;
    for (Calendar calendar : calendars) {
      calendar.setStyle(Calendar.Style.getStyle(style++));
    }
  }
}
