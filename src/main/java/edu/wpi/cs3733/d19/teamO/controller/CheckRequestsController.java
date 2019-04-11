package edu.wpi.cs3733.d19.teamO.controller;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.scene.Parent;
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
import edu.wpi.cs3733.d19.teamO.controller.request.ReligiousServiceViewController;
import edu.wpi.cs3733.d19.teamO.controller.request.SupportAnimalViewController;

@FxmlController(url = "CheckRequests.fxml")
@SuppressWarnings({"PMD.TooManyFields", "PMD.TooManyMethods"})
public class CheckRequestsController implements Controller {

  @FXML
  private VBox root;

  @FXML
  private JFXButton checksanitation;
  @FXML
  private JFXButton checkperscription;
  @FXML
  private JFXButton checkinterpreter;

  @FXML
  private JFXButton checkinternalTransportation;
  @FXML
  private JFXButton checkexternalTransportation;
  @FXML
  private JFXButton checkpatientInfo;

  @FXML
  private JFXButton checkgift;
  @FXML
  private JFXButton checkaudioVisual;
  @FXML
  private JFXButton checkitSupport;


  @FXML
  private JFXButton checkreligious;
  @FXML
  private JFXButton checkanimalSupport;
  @FXML
  private JFXButton checkflorist;

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
  private InterpreterViewController.Factory interpreterViewControllerFactory;
  @Inject
  private InternalTransportationViewController.Factory internalTransportationViewControllerFactory;
  @Inject
  private ExternalTransportationViewController.Factory externalTransporationViewControllerFactory;
  @Inject
  private PatientInfoViewController.Factory patientInfoViewControllerFactory;
  @Inject
  private GiftViewController.Factory giftViewControllerFactory;
  @Inject
  private AudioVisualViewController.Factory audioVisualViewControllerFactory;
  @Inject
  private SupportAnimalViewController.Factory supportAnimalViewControllerFactory;
  @Inject
  private ReligiousServiceViewController.Factory religiousServiceViewControllerFactory;
  @Inject
  private ITSupportViewController.Factory itSupportViewControllerFactory;
  @Inject
  private FloristRequestViewController.Factory floristRequestViewControllerFactory;

  @FXML
  void prescriptionViewAction() {

  }

  @FXML
  void interpreterViewAction() {
    eventBus.post(new ChangeMainViewEvent(interpreterViewControllerFactory.create()));
  }

  @FXML
  void itransViewAction() {
    eventBus.post(new ChangeMainViewEvent(internalTransportationViewControllerFactory.create()));
  }

  @FXML
  void etransViewAction() {
    eventBus.post(new ChangeMainViewEvent(externalTransporationViewControllerFactory.create()));
  }

  @FXML
  void patientViewAction() {
    eventBus.post(new ChangeMainViewEvent(patientInfoViewControllerFactory.create()));
  }

  @FXML
  void giftViewAction() {
    eventBus.post(new ChangeMainViewEvent(giftViewControllerFactory.create()));
  }

  @FXML
  void audioViewAction() {
    eventBus.post(new ChangeMainViewEvent(audioVisualViewControllerFactory.create()));
  }

  @FXML
  void animalViewAction() {
    eventBus.post(new ChangeMainViewEvent(supportAnimalViewControllerFactory.create()));
  }

  @FXML
  void itViewAction() {
    eventBus.post(new ChangeMainViewEvent(itSupportViewControllerFactory.create()));
  }

  @FXML
  void religiousViewAction() {
    eventBus.post(new ChangeMainViewEvent(religiousServiceViewControllerFactory.create()));
  }

  @FXML
  void floristViewAction() {
    eventBus.post(new ChangeMainViewEvent(floristRequestViewControllerFactory.create()));
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    CheckRequestsController create();
  }
}
