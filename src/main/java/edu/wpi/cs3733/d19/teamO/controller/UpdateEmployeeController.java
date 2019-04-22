package edu.wpi.cs3733.d19.teamO.controller;

import java.util.Objects;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import edu.wpi.cs3733.d19.teamO.entity.Employee;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "UpdateEmployee.fxml")
@SuppressWarnings("PMD.TooManyFields")
public class UpdateEmployeeController implements Controller {

  @FXML
  VBox upRoot;
  @FXML
  private JFXTextField upNamefield;
  @FXML
  private JFXTextField upUsernamefield;
  @FXML
  private JFXTextField upPasswordfield;
  @FXML
  private JFXTextField upPhonefield;
  @FXML
  private JFXTextField upIdfield;
  @FXML
  private JFXComboBox<Employee.EmployeeType> upPositionbox;
  @FXML
  private Label upTitlelbl;
  @FXML
  private JFXButton upSubmitbtn;
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

  private final ObjectProperty<Employee> employee = new SimpleObjectProperty<>();

  @FXML
  void initialize() {
    upPositionbox.getItems().setAll(Employee.EmployeeType.values());

    employee.addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        upNamefield.setText(newValue.getName());
        upUsernamefield.setText(newValue.getUsername());
        upPasswordfield.setText(newValue.getPassword());
        upPhonefield.setText(newValue.getPhone());
        upPositionbox.setValue(newValue.getType());
        upIdfield.setText(String.valueOf(newValue.getId()));
        avBox.setSelected(newValue.getEmployeeAttributes().getCanFulfillAudioVisual());
        externalTransportBox.setSelected(
            newValue.getEmployeeAttributes().getCanFulfillExternalTransport());
        floristBox.setSelected(newValue.getEmployeeAttributes().getCanFulfillFlorist());
        giftBox.setSelected(newValue.getEmployeeAttributes().getCanFulfillGift());
        internalTransportBox.setSelected(
            newValue.getEmployeeAttributes().getCanFulfillInternalTransport());
        interpreterBox.setSelected(newValue.getEmployeeAttributes().getCanFulfillInterpreter());
        itSupportBox.setSelected(newValue.getEmployeeAttributes().getCanFulfillITSupport());
        patientInfoBox.setSelected(newValue.getEmployeeAttributes().getCanFulfillPatientInfo());
        prescriptionBox.setSelected(newValue.getEmployeeAttributes().getCanFulfillPrescription());
        religiousBox.setSelected(newValue.getEmployeeAttributes().getCanFulfillReligious());
        sanitationBox.setSelected(newValue.getEmployeeAttributes().getCanFulfillSanitation());
        securityBox.setSelected(newValue.getEmployeeAttributes().getCanFulfillSecurity());
        supportAnimalBox.setSelected(newValue.getEmployeeAttributes().getCanFulfillSupportAnimal());
      }
    });
  }

  @FXML
  void onSubmitAction() {
    if (!upNamefield.getText().isEmpty()
        && !upPasswordfield.getText().isEmpty()
        && !upUsernamefield.getText().isEmpty()
        && !upPhonefield.getText().isEmpty()
        && Objects.nonNull(upPositionbox.getValue())) {
      int idInt = Integer.parseInt(upIdfield.getText());
      Employee lg = db.getEmployee(idInt).get();
      lg.setUsername(upUsernamefield.getText());
      lg.setPassword(upPasswordfield.getText());
      lg.setName(upNamefield.getText());
      lg.setPhone(upPasswordfield.getText());
      lg.setType(upPositionbox.getValue());
      lg.getEmployeeAttributes().setCanFulfillAudioVisual(avBox.isSelected());
      lg.getEmployeeAttributes().setCanFulfillExternalTransport(externalTransportBox.isSelected());
      lg.getEmployeeAttributes().setCanFulfillFlorist(floristBox.isSelected());
      lg.getEmployeeAttributes().setCanFulfillGift(giftBox.isSelected());
      lg.getEmployeeAttributes().setCanFulfillInternalTransport(internalTransportBox.isSelected());
      lg.getEmployeeAttributes().setCanFulfillInterpreter(interpreterBox.isSelected());
      lg.getEmployeeAttributes().setCanFulfillITSupport(itSupportBox.isSelected());
      lg.getEmployeeAttributes().setCanFulfillPatientInfo(patientInfoBox.isSelected());
      lg.getEmployeeAttributes().setCanFulfillPrescription(prescriptionBox.isSelected());
      lg.getEmployeeAttributes().setCanFulfillReligious(religiousBox.isSelected());
      lg.getEmployeeAttributes().setCanFulfillSanitation(sanitationBox.isSelected());
      lg.getEmployeeAttributes().setCanFulfillSecurity(securityBox.isSelected());
      lg.getEmployeeAttributes().setCanFulfillSupportAnimal(supportAnimalBox.isSelected());
      if (db.updateEmployee(lg)) {
        DialogHelper.showInformationAlert("Success!",
            "Successfully updated Employee.");
      } else {
        DialogHelper.showErrorAlert("Error.",
            "Unable to update Employee");
      }
    }
  }

  public Employee getEmployee() {
    return employee.get();
  }

  public ObjectProperty<Employee> employeeProperty() {
    return employee;
  }

  public void setEmployee(Employee employee) {
    this.employee.set(employee);
  }

  @Override
  public Parent getRoot() {
    return upRoot;
  }

  public interface Factory {
    UpdateEmployeeController create();
  }


}
