package edu.wpi.cs3733.d19.teamO.controller;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import edu.wpi.cs3733.d19.teamO.entity.Node;

public class SidePanelPopOutController extends Controller {

  @FXML // fx:id="navBarBackImage"
  private ImageView navBarBackImage; // Value injected by FXMLLoader

  @FXML // fx:id="startCurrLocJFXRadio"
  private JFXRadioButton startCurrLocJFXRadio; // Value injected by FXMLLoader

  @FXML // fx:id="startLocationToggle"
  private ToggleGroup startLocationToggle; // Value injected by FXMLLoader

  @FXML // fx:id="startSelLocJFXRadio"
  private JFXRadioButton startSelLocJFXRadio; // Value injected by FXMLLoader

  @FXML // fx:id="startSelLocJFXCombo"
  private JFXComboBox<Node> startSelLocJFXCombo; // Value injected by FXMLLoader

  @FXML // fx:id="multiPathButton"
  private JFXButton multiPathButton; // Value injected by FXMLLoader

  @FXML // fx:id="endCurrLocJFXRadio"
  private JFXRadioButton endCurrLocJFXRadio; // Value injected by FXMLLoader

  @FXML // fx:id="endLocationToggle"
  private ToggleGroup endLocationToggle; // Value injected by FXMLLoader

  @FXML // fx:id="endSelLocJFXRadio"
  private JFXRadioButton endSelLocJFXRadio; // Value injected by FXMLLoader

  @FXML // fx:id="endSelLocJFXCombo"
  private JFXComboBox<Node> endSelLocJFXCombo;

  @FXML // fx:id="generatePathButton"
  private JFXButton generatePathButton;

  @FXML
  void initialize() throws IOException {
    Image image = new Image(getClass().getResource("navSideBarBack.jpg").openStream());
    navBarBackImage.setImage(image);
  }

  public void onStartCurrLocRadioAction(ActionEvent actionEvent) {
  }

  public void onStartSelLocRadioAction(ActionEvent actionEvent) {
  }

  public void onStartSelLocComboAction(ActionEvent actionEvent) {
  }

  public void onEndCurrLocRadioAction(ActionEvent actionEvent) {
  }

  public void onEndSelLocRadioAction(ActionEvent actionEvent) {
  }

  public void onEndSelLocComboAction(ActionEvent actionEvent) {
  }

  public void onGeneratePathButtonAction(ActionEvent actionEvent) {
  }
}

