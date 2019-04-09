package edu.wpi.cs3733.d19.teamO.controller.v2;

import java.sql.SQLException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@SuppressWarnings("PMD.TooManyFields")
@FxmlController(url = "Scheduling.fxml")
public class SchedulingController implements Controller {

  private final Database database = new Database();

  @FXML
  BorderPane root;

  @FXML
  JFXButton conferenceButton;
  @FXML
  JFXButton labButton;

  @FXML
  JFXButton francis15Button;
  @FXML
  JFXButton francis45Button;
  @FXML
  JFXButton btmButton;
  @FXML
  JFXButton shapiroButton;
  @FXML
  JFXButton towerButton;


  @FXML
  JFXComboBox<Node> roomComboBox;
  @FXML
  JFXDatePicker date;
  @FXML
  JFXTimePicker startTime;
  @FXML
  JFXTimePicker endTime;

  @FXML
  JFXButton goButton;

  @FXML
  TabPane tabPane;
  @FXML
  Tab mapTab;
  @FXML
  Tab tableTab;

  public SchedulingController() throws SQLException {
  }

  @FXML
  void initialize() {
    DialogHelper.populateComboBox(database, roomComboBox);

    // set tab pane to span entire width
    tabPane.widthProperty().addListener((observable, oldValue, newValue) -> {
      tabPane.setTabMinWidth(tabPane.getWidth() / 2);
      tabPane.setTabMaxWidth(tabPane.getWidth() / 2);
    });
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    SchedulingController create();
  }
}
