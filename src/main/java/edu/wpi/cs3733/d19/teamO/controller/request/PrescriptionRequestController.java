package edu.wpi.cs3733.d19.teamO.controller.v2.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.controller.v2.Controller;
import edu.wpi.cs3733.d19.teamO.controller.v2.DialogHelper;
import edu.wpi.cs3733.d19.teamO.controller.v2.FxmlController;
import edu.wpi.cs3733.d19.teamO.controller.v2.RequestController;
import edu.wpi.cs3733.d19.teamO.controller.v2.event.ChangeMainViewEvent;
import edu.wpi.cs3733.d19.teamO.entity.PrescriptionRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "PrescriptionRequest.fxml")
public class PrescriptionRequestController implements Controller {

  private static final Logger logger = Logger.getLogger(
      PrescriptionRequestController.class.getName());

  @FXML
  private JFXTextField patientName;
  @FXML
  private JFXTextField medName;
  @FXML
  private JFXDatePicker patientDOB;
  @FXML
  private JFXTextField medDosage;
  @FXML
  private JFXTextArea medDescription;
  @FXML
  private JFXTextField employeeName;
  @FXML
  private Label submissionDate;
  @FXML
  private BorderPane root;

  @Inject
  private EventBus eventBus;
  @Inject
  private Database db;
  @Inject
  private RequestController.Factory requestControllerFactory;

  @FXML
  void initialize() {
    submissionDate.setText(LocalDate.now().toString());
  }

  public void onBackButtonAction() {
    eventBus.post(new ChangeMainViewEvent(requestControllerFactory.create()));
  }

  /**
   * Submit button for a new Prescription Request.
   */
  public void onSubmitButtonAction() {
    PrescriptionRequest request = parseUserPrescriptionRequest();
    if (request == null) {
      logger.log(Level.WARNING,
          "Unable to parse Prescription Request.",
          "Unable to parse Prescription Request.");
      return;
    }

    if (db.insertPrescriptionRequest(request)) {
      String message = "Successfully submitted request.";
      DialogHelper.showInformationAlert("Success!", message);
    } else {
      DialogHelper.showErrorAlert("Error.",
          "Unable to submit Prescription Request request.");
    }
  }

  private PrescriptionRequest parseUserPrescriptionRequest() {
    // if input is valid, parse it and return a new PrescriptionRequest
    if (!employeeName.getText().isEmpty()
        && !patientName.getText().isEmpty()
        && Objects.nonNull(patientDOB.getValue())
        && !medName.getText().isEmpty()
        && !medDosage.getText().isEmpty()
        && !medDescription.getText().isEmpty()

    ) {

      LocalDateTime now = LocalDateTime.now();
      String empName = employeeName.getText();
      String patName = patientName.getText();
      LocalDate patDOB = patientDOB.getValue();
      String medicationName = medName.getText();
      String medicationDosage = medDosage.getText();
      String medicationDirections = medDescription.getText();

      return new PrescriptionRequest(now, empName, patName, patDOB, medicationName,
          medicationDosage, medicationDirections);
    }

    // otherwise, some input was invalid
    DialogHelper.showErrorAlert("Error.",
        "Please make sure all fields are filled out.");
    return null;
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    PrescriptionRequestController create();
  }
}
