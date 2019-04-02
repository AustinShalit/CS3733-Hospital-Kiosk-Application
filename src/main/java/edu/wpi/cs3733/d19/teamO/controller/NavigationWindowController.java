package edu.wpi.cs3733.d19.teamO.controller;

import java.sql.SQLException;
import java.util.Set;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import javafx.util.StringConverter;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

public class NavigationWindowController extends Controller {

  @FXML
  private JFXButton backButton;
  @FXML
  private JFXButton addAStopButton;
  @FXML
  private JFXButton genPathButton;

  @FXML
  private JFXRadioButton startCurrLocJFXRadio;
  @FXML
  private JFXRadioButton startSelLocJFXRadio;
  @FXML
  private JFXRadioButton endCurrLocJFXRadio;
  @FXML
  private JFXRadioButton endSelLocJFXRadio;

  @FXML
  private JFXComboBox startSelLocJFXCombo;
  @FXML
  private JFXComboBox endSelLocJFXCombo;

  @FXML
  private ToggleGroup startLocationToggle;
  @FXML
  private ToggleGroup endLocationToggle;

  @FXML
  void initialize() throws SQLException {
    Database database = new Database();
    Set<Node> nodeSet = database.getAllNodes();

    ObservableList<Node> locations = FXCollections.observableArrayList();
    for (Node node : nodeSet) {
      locations.add(node);
    }
    for (Node node : locations) {
      startSelLocJFXCombo.getItems().add(node);
      endSelLocJFXCombo.getItems().add(node);
    }

    StringConverter<Node> str = new StringConverter<Node>() {
      @Override
      public String toString(Node object) {
        if (object != null) {
          return object.getShortName();
        }
        return null;
      }

      @Override
      public Node fromString(String string) {
        return null;
      }
    };

    startSelLocJFXCombo.setConverter(str);
    endSelLocJFXCombo.setConverter(str);
  }

  @FXML
  void onStartCurrLocRadioAction() {
    startSelLocJFXCombo.setDisable(true);
    startCurrLocJFXRadio.setText("Not implemented yet.");
  }

  @FXML
  void onStartSelLocRadioAction() {
    startCurrLocJFXRadio.setText("Current Location");
    startSelLocJFXCombo.setDisable(false);
  }

  @FXML
  void onStartSelLocComboAction() {
    validateGenButton();
  }

  @FXML
  void onEndCurrLocRadioAction() {
    endSelLocJFXCombo.setDisable(true);
    endCurrLocJFXRadio.setText("Not implemented yet.");
  }

  @FXML
  void onEndSelLocRadioAction() {
    endCurrLocJFXRadio.setText("Current Location");
    endSelLocJFXCombo.setDisable(false);
  }

  @FXML
  void onEndSelLocComboAction() {
    validateGenButton();
  }

  @FXML
  void onBackButtonAction() {
    switchScenes("MainWindow.fxml", backButton.getScene().getWindow());
  }

  @FXML
  void setGenPathButtonAction() {
    //BreadthFirstSearchAlgorithm bfsAlgorithm = new BreadthFirstSearchAlgorithm(new Database());
    // bfsAlgorithm.getPath();
  }

  void validateGenButton() {
    if (startSelLocJFXCombo.getValue() != null && endSelLocJFXCombo.getValue() != null) {
      genPathButton.setDisable(false);
    } else {
      genPathButton.setDisable(true);
    }
  }
}
