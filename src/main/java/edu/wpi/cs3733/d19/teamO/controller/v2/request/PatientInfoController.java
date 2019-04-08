package edu.wpi.cs3733.d19.teamO.controller.v2.request;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.logging.Logger;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.controller.v2.Controller;
import edu.wpi.cs3733.d19.teamO.controller.v2.DialogHelper;
import edu.wpi.cs3733.d19.teamO.controller.v2.FxmlController;
import edu.wpi.cs3733.d19.teamO.controller.v2.RequestController;
import edu.wpi.cs3733.d19.teamO.controller.v2.event.ChangeMainViewEvent;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.PatientInfoRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "PatientInfo.fxml")
public class PatientInfoController implements Controller {

  private static final Logger logger = Logger.getLogger(PatientInfoController.class.getName());

  @FXML
  private BorderPane root;
  @FXML
  private JFXTextField nametxt;
  @FXML
  private JFXDatePicker patientDOB;
  @FXML
  private JFXComboBox<PatientInfoRequest.PatientInfoSex> sexbox;
  @FXML
  private JFXComboBox<Node> locationComboBox;
  @FXML
  private JFXTextArea descriptiontxt;
  @FXML
  private JFXButton submitbtn;
  @FXML
  private JFXButton backbtn;

  @Inject
  private EventBus eventBus;
  @Inject
  private Database db;
  @Inject
  private RequestController.Factory requestControllerFactory;

  @FXML
  void initialize() {
    sexbox.getItems().setAll(PatientInfoRequest.PatientInfoSex.values());
    DialogHelper.populateComboBox(db, locationComboBox);
  }

  @FXML
  void onbackButtonAction() {
    eventBus.post(new ChangeMainViewEvent(requestControllerFactory.create()));
  }

  @FXML
  void onSubmitButtonAction() {
    PatientInfoRequest req = parse();
    if (Objects.nonNull(req)) {
      db.insertPatientInfoRequest(req);
      DialogHelper.showInformationAlert("Success!",
          "Inserted Patient Info into Database!");;
    } else {
      DialogHelper.showErrorAlert("Error.",
          "Please make sure all fields are filled out.");
    }
  }

  /**
   * Parse input the user has inputted for the sanitation request.
   *
   * @return If valid input, A PatientInfoRequest representing the users input. Otherwise null.
   */
  private PatientInfoRequest parse() {
    // validation
    if (nametxt.getText().isEmpty() // check that the name field is filled out
        || Objects.isNull(patientDOB.getValue()) // check if patientDOB is filled out
        || Objects.isNull(locationComboBox.getValue()) // check that location field is filled out
        || Objects.isNull(sexbox.getValue())) { // check if patient sex is selected
      return null;
    }

    return new PatientInfoRequest(LocalDateTime.now(),
        locationComboBox.getValue(),
        descriptiontxt.getText(),
        nametxt.getText(),
        patientDOB.getValue(),
        sexbox.getValue());
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    PatientInfoController create();
  }
}
