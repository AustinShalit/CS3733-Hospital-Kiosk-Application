package edu.wpi.cs3733.d19.teamO.controller;

import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import edu.wpi.cs3733.d19.teamO.entity.Employee;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "AddEmployee.fxml")
@SuppressWarnings("PMD.TooManyFields")
public class AddEmployeeController implements Controller {

  private static final Logger logger =
      Logger.getLogger(AddEmployeeController.class.getName());

  @FXML
  VBox root;
  @FXML
  private JFXTextField nameField;
  @FXML
  private JFXTextField usernameField;
  @FXML
  private JFXTextField passwordField;
  @FXML
  private JFXTextField phoneField;
  @FXML
  private JFXComboBox<Employee.EmployeeType> positionBox;
  @FXML
  private JFXButton submitbtn;
  @FXML
  private JFXCheckBox avBox;
  @FXML
  private JFXCheckBox externalTransportBox;
  @FXML
  private JFXCheckBox floristBox;
  @FXML
  private JFXCheckBox giftBox;
  @FXML
  private JFXCheckBox internalTransportBox;
  @FXML
  private JFXCheckBox interpreterBox;
  @FXML
  private JFXCheckBox itSupportBox;
  @FXML
  private JFXCheckBox patientInfoBox;
  @FXML
  private JFXCheckBox prescriptionBox;
  @FXML
  private JFXCheckBox religiousBox;
  @FXML
  private JFXCheckBox sanitationBox;
  @FXML
  private JFXCheckBox securityBox;
  @FXML
  private JFXCheckBox supportAnimalBox;

  @Inject
  private Database db;

  @FXML
  void initialize() {
    positionBox.getItems().setAll(Employee.EmployeeType.values());

    submitbtn.disableProperty().bind(
        Bindings.isEmpty(nameField.textProperty())
        .or(Bindings.isEmpty(usernameField.textProperty()))
        .or(Bindings.isEmpty(passwordField.textProperty()))
        .or(Bindings.isNull(positionBox.valueProperty()))
        .or(Bindings.isEmpty(phoneField.textProperty()))
    );
  }

  @FXML
  void onSubmitButtonAction() {
    Employee external = parseUserEmployee();
    if (external == null) {
      logger.log(Level.WARNING,
          "Unable to parse Employee Data.",
          "Unable to parse Employee Data.");
      return;
    }

    nameField.setText(null);
    usernameField.setText(null);
    passwordField.setText(null);
    phoneField.setText(null);
    positionBox.getSelectionModel().clearSelection();
    positionBox.setValue(null);
    avBox.setSelected(false);
    externalTransportBox.setSelected(false);
    floristBox.setSelected(false);
    giftBox.setSelected(false);
    internalTransportBox.setSelected(false);
    interpreterBox.setSelected(false);
    itSupportBox.setSelected(false);
    patientInfoBox.setSelected(false);
    prescriptionBox.setSelected(false);
    religiousBox.setSelected(false);
    sanitationBox.setSelected(false);
    securityBox.setSelected(false);
    supportAnimalBox.setSelected(false);

    if (db.insertEmployee(external)) {
      DialogHelper.showInformationAlert("Success!",
          "Successfully added new Employee.");
    } else {
      DialogHelper.showErrorAlert("Error.",
          "Unable to add new Employee");
    }
  }

  /**
   * Parse input the user has inputted for the employee.
   *
   * @return If valid input, A employee representing the users input. Otherwise null.
   */
  @FXML
  private Employee parseUserEmployee() {
    // if input is valid, parse it and return a new Employee
    if (!nameField.getText().isEmpty()
        && !usernameField.getText().isEmpty()
        && !passwordField.getText().isEmpty()
        && !phoneField.getText().isEmpty()
        && Objects.nonNull(positionBox.getValue())) {

      String name = nameField.getText();
      String username = usernameField.getText();
      String password = passwordField.getText();
      String phoneNumber = phoneField.getText();
      String type = positionBox.getValue().toString().toUpperCase(new Locale("EN"));
      Employee.EmployeeType externalEmployeeType = Employee.EmployeeType.valueOf(type);

      Employee newemp = new Employee(username, password, name, externalEmployeeType, phoneNumber);
      newemp.getEmployeeAttributes().setCanFulfillAudioVisual(avBox.isSelected());
      newemp.getEmployeeAttributes().setCanFulfillExternalTransport(
          externalTransportBox.isSelected());
      newemp.getEmployeeAttributes().setCanFulfillFlorist(floristBox.isSelected());
      newemp.getEmployeeAttributes().setCanFulfillGift(giftBox.isSelected());
      newemp.getEmployeeAttributes().setCanFulfillInternalTransport(
          internalTransportBox.isSelected());
      newemp.getEmployeeAttributes().setCanFulfillInterpreter(interpreterBox.isSelected());
      newemp.getEmployeeAttributes().setCanFulfillITSupport(itSupportBox.isSelected());
      newemp.getEmployeeAttributes().setCanFulfillPatientInfo(patientInfoBox.isSelected());
      newemp.getEmployeeAttributes().setCanFulfillPrescription(prescriptionBox.isSelected());
      newemp.getEmployeeAttributes().setCanFulfillReligious(religiousBox.isSelected());
      newemp.getEmployeeAttributes().setCanFulfillSanitation(sanitationBox.isSelected());
      newemp.getEmployeeAttributes().setCanFulfillSecurity(securityBox.isSelected());
      newemp.getEmployeeAttributes().setCanFulfillSupportAnimal(supportAnimalBox.isSelected());

      return newemp;
    }

    // otherwise, some input was invalid
    DialogHelper.showErrorAlert("Error.",
        "Please make sure all fields were filled out");
    return null;
  }


  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    AddEmployeeController create();
  }
}
