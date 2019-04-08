package edu.wpi.cs3733.d19.teamO.controller.v2.request;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import edu.wpi.cs3733.d19.teamO.entity.InternalTransportationRequest;
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
  private JFXTextField dobtxt;
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
    return;
  }

  /**
   * Parse input the user has inputted for the sanitation request.
   *
   * @return If valid input, A SanitationRequest representing the users input. Otherwise null.
   */
  private PatientInfoRequest parse() {
    // validation

    // creation
    /*
    PatientInfoRequest patientInfoRequest = new PatientInfoRequest(0,
        )
        */
    return null;
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    PatientInfoController create();
  }
}
