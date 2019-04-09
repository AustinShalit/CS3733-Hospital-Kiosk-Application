package edu.wpi.cs3733.d19.teamO.controller.v2;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

@FxmlController(url = "CheckRequests.fxml")
@SuppressWarnings("PMD.TooManyFields")
public class CheckRequestsController implements Controller {
  @FXML
  private VBox root;

  @FXML
  private JFXButton sanitationReview;
  @FXML
  private JFXButton prescriptionReview;
  @FXML
  private JFXButton interpreterReview;

  @FXML
  private JFXButton internalTransportationReview;
  @FXML
  private JFXButton externalTransportationReview;
  @FXML
  private JFXButton patientInfoReview;

  @FXML
  private JFXButton giftReview;
  @FXML
  private JFXButton audioVisualReview;
  @FXML
  private JFXButton itSupportReview;


  @FXML
  private JFXButton religiousReview;
  @FXML
  private JFXButton suuportAnimalReview;
  @FXML
  private JFXButton other1;

  @FXML
  private JFXButton other2;
  @FXML
  private JFXButton other3;
  @FXML
  private JFXButton other4;

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    CheckRequestsController create();
  }

}
