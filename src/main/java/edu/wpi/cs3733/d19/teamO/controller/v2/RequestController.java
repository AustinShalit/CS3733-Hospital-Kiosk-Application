package edu.wpi.cs3733.d19.teamO.controller.v2;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import edu.wpi.cs3733.d19.teamO.controller.v2.event.ChangeMainViewEvent;
import edu.wpi.cs3733.d19.teamO.controller.v2.request.FloristRequestController;
import edu.wpi.cs3733.d19.teamO.controller.v2.request.FloristRequestViewController;
import edu.wpi.cs3733.d19.teamO.controller.v2.request.InternalTransportationController;

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
  private JFXButton florist;

  @FXML
  private JFXButton other2;
  @FXML
  private JFXButton other3;
  @FXML
  private JFXButton other4;
  @FXML
  private JFXButton viewRequest;

  @Inject
  private EventBus eventBus;
  @Inject
  private InternalTransportationController.Factory internalTransportationControllerFactory;
  @Inject
  private FloristRequestController.Factory floristRequestControllerFactory;
  @Inject
  private FloristRequestViewController.Factory floristRequestViewControllerFactory;

  @FXML
  void internalTransportationAction(ActionEvent event) {
    eventBus.post(new ChangeMainViewEvent(internalTransportationControllerFactory.create()));
  }

  @FXML
  void floristAction(ActionEvent event) {
    eventBus.post(new ChangeMainViewEvent(floristRequestControllerFactory.create()));
  }

  @FXML
  void viewAction(ActionEvent event) {
    eventBus.post(new ChangeMainViewEvent(floristRequestViewControllerFactory.create()));
  }


  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    RequestController create();
  }
}
