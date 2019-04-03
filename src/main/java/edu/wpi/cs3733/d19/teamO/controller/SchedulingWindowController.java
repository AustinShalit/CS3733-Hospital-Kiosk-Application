package edu.wpi.cs3733.d19.teamO.controller;

import java.sql.SQLException;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.Callback;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@SuppressWarnings("PMD")
public class SchedulingWindowController extends Controller {

  private Database database;

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
   * Check to make sure Scheduling Request is valid.
   *
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

  /**
   * Populate the Room selection ComboBox.
   *
   * @throws SQLException When stuff goes wrong.
   */
  @FXML
  public void initialize() throws SQLException {
    database = new Database();
    roomComboBox.getItems().addAll(database.getAllNodes());
    roomComboBox.setCellFactory(new Callback<ListView<Node>, ListCell<Node>>() {
      @Override
      public ListCell<Node> call(ListView<Node> param) {
        return new ListCell<Node>() {
          private final Text text;

          {
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
