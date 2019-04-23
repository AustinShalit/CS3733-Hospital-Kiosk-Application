package edu.wpi.cs3733.d19.teamO.controller;

import java.awt.Dimension;
import java.awt.Toolkit;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;

import foodRequest.FoodRequest;
import foodRequest.ServiceException;
import floral.api.FloralApi;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

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
  StackPane root;
  @FXML
  VBox menu;
  @FXML
  private BorderPane content;

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
  private JFXButton food;

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
  void initialize() {
    menu.setPickOnBounds(false);
    content.setPickOnBounds(false);

    menu.heightProperty().addListener(
        (observable, oldValue, newValue) -> {
          sanitation.setPrefHeight(newValue.doubleValue() / menu.getChildren().size());
          prescription.setPrefHeight(newValue.doubleValue() / menu.getChildren().size());
          interpreter.setPrefHeight(newValue.doubleValue() / menu.getChildren().size());
          audioVisual.setPrefHeight(newValue.doubleValue() / menu.getChildren().size());
          internalTransportation.setPrefHeight(newValue.doubleValue() / menu.getChildren().size());
          externalTransportation.setPrefHeight(newValue.doubleValue() / menu.getChildren().size());
          patientInfo.setPrefHeight(newValue.doubleValue() / menu.getChildren().size());
          gift.setPrefHeight(newValue.doubleValue() / menu.getChildren().size());
          itSupport.setPrefHeight(newValue.doubleValue() / menu.getChildren().size());
          religious.setPrefHeight(newValue.doubleValue() / menu.getChildren().size());
          animalSupport.setPrefHeight(newValue.doubleValue() / menu.getChildren().size());
          florist.setPrefHeight(newValue.doubleValue() / menu.getChildren().size());
          food.setPrefHeight(newValue.doubleValue() / menu.getChildren().size());
        });
  }

  @FXML
  void internalTransportationAction(ActionEvent event) {
    content.setCenter(internalTransportationControllerFactory.create().root);
  }

  @FXML
  void sanitationAction(ActionEvent event) {
    content.setCenter(sanitationControllerFactory.create().root);
  }

  @FXML
  void audioVisualAction(ActionEvent event) {
    content.setCenter(audioVisualViewControllerFactory.create().root);
  }

  @FXML
  void patientInfoAction(ActionEvent event) {
    content.setCenter(patientInfoControllerFactory.create().root);
  }

  @FXML
  void itSupportAction(ActionEvent event) {
    content.setCenter(itSupportControllerFactory.create().root);
  }

  @FXML
  void externalTransportationAction(ActionEvent event) {
    content.setCenter(externalTransportationViewControllerFactory.create().root);
  }

  @FXML
  void giftAction(ActionEvent event) {
    content.setCenter(giftControllerFactory.create().root);
  }

  @FXML
  void interpreterAction(ActionEvent event) {
    content.setCenter(interpreterControllerFactory.create().root);
  }

  @FXML
  void religiousAction(ActionEvent event) {
    content.setCenter(religiousServiceControllerFactory.create().root);
  }

  @FXML
  void floristAction(ActionEvent event) {
    FloralApi floralApi = new FloralApi();
    try {
      floralApi.run(5, 5, null, null);
    } catch (Exception e) {
      System.out.println("Floral API failed");
      e.printStackTrace();
    }

  }


  @FXML
  void supportAnimalAction(ActionEvent event) {
    content.setCenter(supportAnimalControllerFactory.create().root);
  }

  @FXML
  void prescriptionAction() {
    content.setCenter(prescriptionRequestControllerFactory.create().root);
  }

  @FXML
  void foodAction() {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    FoodRequest foodRequest = new FoodRequest();
    try {
      foodRequest.run(
          0,
          0,
          screenSize.width,
          screenSize.height,
           null,
          null,
          null);
    } catch (ServiceException exception) {
      System.out.println("Failed to run API");
      exception.printStackTrace();
    }
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    RequestController create();
  }
}
