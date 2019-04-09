package edu.wpi.cs3733.d19.teamO.controller.v2;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import edu.wpi.cs3733.d19.teamO.controller.v2.event.ChangeMainViewEvent;
import edu.wpi.cs3733.d19.teamO.controller.v2.request.AudioVisualController;
import edu.wpi.cs3733.d19.teamO.controller.v2.request.ExternalTransportationController;
import edu.wpi.cs3733.d19.teamO.controller.v2.request.FloristRequestController;
import edu.wpi.cs3733.d19.teamO.controller.v2.request.GiftController;
import edu.wpi.cs3733.d19.teamO.controller.v2.request.ITSupportController;
import edu.wpi.cs3733.d19.teamO.controller.v2.request.InternalTransportationController;
import edu.wpi.cs3733.d19.teamO.controller.v2.request.InterpreterController;
import edu.wpi.cs3733.d19.teamO.controller.v2.request.PatientInfoController;
import edu.wpi.cs3733.d19.teamO.controller.v2.request.ReligiousServiceController;
import edu.wpi.cs3733.d19.teamO.controller.v2.request.ReligiousServiceViewController;
import edu.wpi.cs3733.d19.teamO.controller.v2.request.SupportAnimalController;

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
  private JFXButton interpreter;

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

  @FXML
  private JFXButton viewResolveButton;

  @Inject
  private EventBus eventBus;
  @Inject
  private InternalTransportationController.Factory internalTransportationControllerFactory;
  @Inject
  private AudioVisualController.Factory audioVisualControllerFactory;
  @Inject
  private PatientInfoController.Factory patientInfoControllerFactory;
  @Inject
  private ITSupportController.Factory itSupportControllerFactory;
  @Inject
  private ExternalTransportationController.Factory externalTransportationControllerFactory;
  @Inject
  private GiftController.Factory giftControllerFactory;
  @Inject
  private InterpreterController.Factory interpreterControllerFactory;
  @Inject
  private ReligiousServiceController.Factory religiousServiceControllerFactory;
  @Inject
  private ReligiousServiceViewController.Factory religiousServiceViewControllerFactory;

  @Inject
  private SupportAnimalController.Factory supportAnimalControllerFactory;
  @Inject
  private FloristRequestController.Factory floristRequestControllerFactory;

  @FXML
  void internalTransportationAction(ActionEvent event) {
    eventBus.post(new ChangeMainViewEvent(internalTransportationControllerFactory.create()));
  }

  @FXML
  void audioVisualAction(ActionEvent event) {
    eventBus.post(new ChangeMainViewEvent(audioVisualControllerFactory.create()));
  }

  @FXML
  void patientInfoAction(ActionEvent event) {
    eventBus.post(new ChangeMainViewEvent(patientInfoControllerFactory.create()));
  }

  @FXML
  void itSupportAction(ActionEvent event) {
    eventBus.post(new ChangeMainViewEvent(itSupportControllerFactory.create()));
  }

  @FXML
  void externalTransportationAction(ActionEvent event) {
    eventBus.post(new ChangeMainViewEvent(externalTransportationControllerFactory.create()));
  }

  @FXML
  void giftAction(ActionEvent event) {
    eventBus.post(new ChangeMainViewEvent(giftControllerFactory.create()));
  }

  @FXML
  void interpreterAction(ActionEvent event) {
    eventBus.post(new ChangeMainViewEvent(interpreterControllerFactory.create()));
  }

  @FXML
  void religiousAction(ActionEvent event) {
    eventBus.post(new ChangeMainViewEvent(religiousServiceControllerFactory.create()));
  }

  void floristAction(ActionEvent event) {
    eventBus.post(new ChangeMainViewEvent(floristRequestControllerFactory.create()));
  }

  @FXML
  void viewAction(ActionEvent event) {
    eventBus.post(new ChangeMainViewEvent(religiousServiceViewControllerFactory.create()));
  }


  @FXML
  void supportAnimalAction(ActionEvent event) {
    eventBus.post(new ChangeMainViewEvent(supportAnimalControllerFactory.create()));
  }


  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    RequestController create();
  }
}
