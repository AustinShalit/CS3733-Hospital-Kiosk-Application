package edu.wpi.cs3733.d19.teamO.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.List;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;

import edu.wpi.cs3733.d19.teamO.entity.SchedulingRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.NodeDaoDb;
import edu.wpi.cs3733.d19.teamO.entity.database.SchedulingRequestDaoDb;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class SchedulingWindowController extends Controller {

  private NodeDaoDb nodeDao;

  @FXML
  private Button backButton;
  @FXML
  private Button submitButton;
  @FXML
  private ComboBox<Node> roomComboBox;
  @FXML
  private TextField nameBox;
  @FXML
  private JFXTimePicker startChoiceBox;
  @FXML
  private JFXTimePicker endChoiceBox;
  @FXML
  private JFXDatePicker datePicker;
  @FXML
  private Label submitStatus;


  @FXML
  void onBackButtonAction(ActionEvent event) {
    if (event.getSource() == backButton) {
      switchScenes("MainWindow.fxml", backButton.getScene().getWindow());
    }
  }

  /**
   * Check to make sure Scheduling Request is valid
   * @param e Action Event from Submit button
   */
  @FXML
  void onSubmitButtonAction(ActionEvent e) {
    if (e.getSource() == submitButton
        && nameBox != null
        && startChoiceBox.getValue() != null
        && endChoiceBox.getValue() != null
        && datePicker.getValue() != null
        && roomComboBox.getValue() != null) {
      submitStatus.setText("All fields are filled. Nice!");
    } else {
      submitStatus.setText("Make sure all fields are filled in.");
    }

  }

  @FXML
  public void initialize() throws SQLException {
    nodeDao = new NodeDaoDb();
    roomComboBox.getItems().addAll(nodeDao.getAll());
    roomComboBox.setCellFactory(new Callback<ListView<Node>, ListCell<Node>>() {
      @Override
      public ListCell<Node> call(ListView<Node> param) {
        return new ListCell<Node>() {
          private final Text text; {
            setContentDisplay(ContentDisplay.CENTER);
            text = new Text();
          }
          @Override
          protected void updateItem(Node item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
              setGraphic(null);
            } else {
              text.setText(item.getLongName());
              setGraphic(text);
            }
          }
        };
      }
    });

  }

}
