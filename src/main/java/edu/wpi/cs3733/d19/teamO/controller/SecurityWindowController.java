package edu.wpi.cs3733.d19.teamO.controller;

import java.sql.SQLException;
import java.time.LocalDateTime;

import com.jfoenix.controls.JFXComboBox;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.SecurityRequest;
import javafx.util.Callback;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@SuppressWarnings("PMD")
public class SecurityWindowController extends Controller {

  private Database database;

  @FXML
  private Button backButton;

  @FXML
  private Button alertbutton;

  @FXML
  private Button filePicker;

  @FXML
  private Text securityTitle;

  @FXML
  private JFXComboBox insertlocationdropdown;

  @FXML
  void chooseLocation(ActionEvent event) {

  }


  @FXML
  void initialize() throws SQLException {
    database = new Database();

    /*
    insertlocationdropdown
        .getItems()
        .addAll(
            database
                .getAllNodes()
                .stream()
                .map(Node::getNodeId)
                .collect(Collectors.toList())
      );
    */

    insertlocationdropdown.getItems().setAll(database.getAllNodes());
    insertlocationdropdown.setCellFactory(new Callback<ListView<Node>, ListCell<Node>>() {
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


  @FXML
  void onBackButtonAction(ActionEvent event) {
    if (event.getSource() == backButton) {
      switchScenes("MainWindow.fxml", backButton.getScene().getWindow());
    }
  }

  @FXML
  void sendAlert(ActionEvent e) {
    if (e.getSource() == alertbutton) {
      Node node = database.getNode(insertlocationdropdown.getValue().toString()).get();
      database.insertSecurityRequest(
          new SecurityRequest(
              LocalDateTime.now(),
              null,
              null,
              null,
              node
          ));
    }
  }

}
