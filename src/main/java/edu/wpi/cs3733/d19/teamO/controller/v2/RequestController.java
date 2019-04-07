package edu.wpi.cs3733.d19.teamO.controller.v2;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

@FxmlController(url = "ServiceRequest.fxml")
@SuppressWarnings("PMD.TooManyFields")
public class RequestController implements Controller {

  @FXML
  private VBox root;

  @FXML
  private JFXButton sanitation;
  @FXML
  private JFXButton perscription;
  @FXML
  private JFXButton interpereter;

  @FXML
  private JFXButton internalTransportation;
  @FXML
  private JFXButton externalTransportation;
  @FXML
  private JFXButton patientInfo;

  @FXML
  private JFXButton gift;
  @FXML
  private JFXButton audioVisual;
  @FXML
  private JFXButton itSupport;


  @FXML
  private JFXButton religious;
  @FXML
  private JFXButton jyans;
  @FXML
  private JFXButton other1;

  @FXML
  private JFXButton other2;
  @FXML
  private JFXButton other3;
  @FXML
  private JFXButton other4;

  @FXML
  void initialize() {

  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    RequestController create();
  }
}
