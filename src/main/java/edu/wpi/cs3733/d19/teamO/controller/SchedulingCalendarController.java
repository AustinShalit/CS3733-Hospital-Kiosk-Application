package edu.wpi.cs3733.d19.teamO.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

@FxmlController(url = "SchedulingCalendar.fxml")
public class SchedulingCalendarController implements Controller {

  @FXML
  private BorderPane root;

  @FXML
  void initialize() {

  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    SchedulingCalendarController create();
  }
}
