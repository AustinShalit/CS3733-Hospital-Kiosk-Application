package edu.wpi.cs3733.d19.teamO.controller;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import edu.wpi.cs3733.d19.teamO.controller.event.ChangeMainViewEvent;
import edu.wpi.cs3733.d19.teamO.controller.request.AudioVisualViewController;
import edu.wpi.cs3733.d19.teamO.controller.request.ExternalTransportationViewController;
import edu.wpi.cs3733.d19.teamO.controller.request.FloristRequestViewController;
import edu.wpi.cs3733.d19.teamO.controller.request.GiftViewController;
import edu.wpi.cs3733.d19.teamO.controller.request.ITSupportViewController;
import edu.wpi.cs3733.d19.teamO.controller.request.InternalTransportationViewController;
import edu.wpi.cs3733.d19.teamO.controller.request.InterpreterViewController;
import edu.wpi.cs3733.d19.teamO.controller.request.PatientInfoViewController;
import edu.wpi.cs3733.d19.teamO.controller.request.PrescriptionViewController;
import edu.wpi.cs3733.d19.teamO.controller.request.ReligiousServiceViewController;
import edu.wpi.cs3733.d19.teamO.controller.request.SanitationViewController;
import edu.wpi.cs3733.d19.teamO.controller.request.SupportAnimalViewController;

@FxmlController(url = "ServiceRequest.fxml")
@SuppressWarnings({"PMD.TooManyFields", "PMD.TooManyMethods"})
public class RequestController implements Controller {

  @FXML
  BorderPane root;

  @FXML
  private JFXButton sanitation;
  @FXML
  private JFXButton prescription;
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
  private JFXButton animalSupport;
  @FXML
  private JFXButton florist;

  @FXML
  private JFXButton viewRequest;

  @Inject
  private EventBus eventBus;
  @Inject
  private InternalTransportationViewController.Factory internalTransportationControllerFactory;
  @Inject
  private AudioVisualViewController.Factory audioVisualViewControllerFactory;
  @Inject
  private PatientInfoViewController.Factory patientInfoControllerFactory;
  @Inject
  private ITSupportViewController.Factory itSupportControllerFactory;
  @Inject
  private ExternalTransportationViewController.Factory externalTransportationViewControllerFactory;
  @Inject
  private GiftViewController.Factory giftControllerFactory;
  @Inject
  private InterpreterViewController.Factory interpreterControllerFactory;
  @Inject
  private ReligiousServiceViewController.Factory religiousServiceControllerFactory;
  @Inject
  private SupportAnimalViewController.Factory supportAnimalControllerFactory;
  @Inject
  private FloristRequestViewController.Factory floristRequestControllerFactory;
  @Inject
  private PrescriptionViewController.Factory prescriptionRequestControllerFactory;
  @Inject
  private SanitationViewController.Factory sanitationControllerFactory;


  @FXML
  void internalTransportationAction(ActionEvent event) {
    eventBus.post(new ChangeMainViewEvent(internalTransportationControllerFactory.create()));
  }

  @FXML
  void sanitationAction(ActionEvent event) {
    eventBus.post(new ChangeMainViewEvent(sanitationControllerFactory.create()));
  }

  @FXML
  void audioVisualAction(ActionEvent event) {
    eventBus.post(new ChangeMainViewEvent(audioVisualViewControllerFactory.create()));
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
    eventBus.post(new ChangeMainViewEvent(externalTransportationViewControllerFactory.create()));
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

  @FXML
  void floristAction(ActionEvent event) {
    eventBus.post(new ChangeMainViewEvent(floristRequestControllerFactory.create()));
  }


  @FXML
  void supportAnimalAction(ActionEvent event) {
    eventBus.post(new ChangeMainViewEvent(supportAnimalControllerFactory.create()));
  }

  @FXML
  void prescriptionAction() {
    eventBus.post(new ChangeMainViewEvent(prescriptionRequestControllerFactory.create()));
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    RequestController create();
  }
}
