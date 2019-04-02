package edu.wpi.cs3733.d19.teamO.controller;

import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.util.Callback;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.SanitationRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@SuppressWarnings("PMD")
public class SanitationWindowController extends Controller {

  private Database database;

  @FXML
  private Button backButton;
  @FXML
  private Button submitButton;
  @FXML
  private Label titleLabel;
  @FXML
  private TextArea descriptionTextArea;
  @FXML
  private ComboBox<Node> locationComboBox;
  @FXML
  private ComboBox<SanitationRequest.SanitationRequestType> categoryComboBox;

  @FXML
  void initialize() throws SQLException {
    database = new Database();
    locationComboBox.getItems().setAll(database.getAllNodes());
    // Super verbose, but makes it so that just the long name for each node is displayed
    locationComboBox.setCellFactory(new Callback<ListView<Node>, ListCell<Node>>() {
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
    categoryComboBox.getItems().setAll(SanitationRequest.SanitationRequestType.values());
    //    locationComboBox.getItems().setAll(db.getAllSanitationRequests());
  }


  @FXML
  void onBackButtonAction() {
    switchScenes("MainSanitationWindow.fxml", backButton.getScene().getWindow());
  }


  @FXML
  void onSubmitButtonAction() {
    submitButton.setText("Not yet implemented");
  }
}
