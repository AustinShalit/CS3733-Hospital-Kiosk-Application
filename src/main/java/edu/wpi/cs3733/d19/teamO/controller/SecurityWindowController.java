package edu.wpi.cs3733.d19.teamO.controller;

import com.jfoenix.controls.JFXComboBox;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.NodeDaoDb;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.sql.SQLException;

public class SecurityWindowController extends Controller {

  private NodeDaoDb nodeDao;

  @FXML
  private Button backButton;

  @FXML
  private Button alertbutton;

  @FXML
  private Button getnodesbutton;

  @FXML
  private Text securityTitle;

  @FXML
  private JFXComboBox insertlocationdropdown;

  @FXML
  void chooseLocation(ActionEvent event) {

  }

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
    nodeDao = new NodeDaoDb();
    insertlocationdropdown.getItems().setAll(nodeDao.getAll());
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


}
