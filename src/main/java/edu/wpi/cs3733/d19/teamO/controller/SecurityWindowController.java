package edu.wpi.cs3733.d19.teamO.controller;

import java.sql.SQLException;

import com.jfoenix.controls.JFXComboBox;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.util.Callback;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@SuppressWarnings("PMD")
public class SecurityWindowController extends Controller {

  @FXML
  private Button backButton;

  @FXML
  private Button alertbutton;

  @FXML
  private Button getnodesbutton;

  @FXML
  private Text securityTitle;

  @FXML
  private JFXComboBox<Node> insertlocationdropdown;

  @FXML
  void chooseLocation(ActionEvent event) {

  }

  private Database database;

  @FXML
  void initialize() throws SQLException {
    /*
    insertlocationdropdown.getItems().addAll(
        "Place 1",
        "Place 2",
        "Place 3",
        "Place 4"
    );
    */
    database = new Database();

    insertlocationdropdown.getItems().setAll(database.getAllNodes());
    insertlocationdropdown.setCellFactory(new Callback<ListView<Node>, ListCell<Node>>() {
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

  static {

  }

  @FXML
  void onBackButtonAction(ActionEvent event) {
    if (event.getSource() == backButton) {
      switchScenes("MainWindow.fxml", backButton.getScene().getWindow());
    }
  }


}
