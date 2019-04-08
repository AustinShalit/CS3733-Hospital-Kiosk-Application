package edu.wpi.cs3733.d19.teamO.controller.v2;

import java.sql.SQLException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "Navigation.fxml")
public class NavigationController implements Controller {

  Database database = new Database();

  @FXML
  BorderPane root;

  @FXML
  JFXButton bathroomButton;
  @FXML
  JFXButton elevatorButton;
  @FXML
  JFXButton exitButton;
  @FXML
  JFXButton informationButton;
  @FXML
  JFXComboBox<Node> fromComboBox;
  @FXML
  JFXComboBox<Node> toComboBox;
  @FXML
  JFXButton goButton;

  public NavigationController() throws SQLException {
  }

  @FXML
  void initialize() {
    DialogHelper.populateComboBox(database, fromComboBox);
    DialogHelper.populateComboBox(database, toComboBox);
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    NavigationController create();
  }
}
