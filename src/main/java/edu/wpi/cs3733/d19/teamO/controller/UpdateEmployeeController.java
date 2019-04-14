package edu.wpi.cs3733.d19.teamO.controller;

import java.util.Objects;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
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
  private VBox upRoot;
  @FXML
  private JFXTextField upNamefield;
  @FXML
  private JFXTextField upUsernamefield;
  @FXML
  private JFXTextField upPasswordfield;
  @FXML
  private JFXTextField upIdfield;
  @FXML
  private JFXComboBox<Employee.EmployeeType> upPositionbox;
  @FXML
  private Label upTitlelbl;
  @FXML
  private JFXButton upSubmitbtn;

  @Inject
  private Database db;

  private final ObjectProperty<Employee> employee = new SimpleObjectProperty<>();

  @FXML
  void initialize() {
    upPositionbox.getItems().setAll(Employee.EmployeeType.values());

    employee.addListener((observable, oldValue, newValue) -> {
      upNamefield.setText(newValue.getName());
      upUsernamefield.setText(newValue.getUsername());
      upPasswordfield.setText(newValue.getPassword());
      upPositionbox.setValue(newValue.getType());
      upIdfield.setText(String.valueOf(newValue.getId()));
    });
  }

  @FXML
  void onSubmitAction() {
    if (!upNamefield.getText().isEmpty()
        && !upPasswordfield.getText().isEmpty()
        && !upUsernamefield.getText().isEmpty()
        && Objects.nonNull(upPositionbox.getValue())) {
      int idInt = Integer.parseInt(upIdfield.getText());
      Employee lg = db.getEmployee(idInt).get();
      lg.setUsername(upUsernamefield.getText());
      lg.setPassword(upPasswordfield.getText());
      lg.setName(upNamefield.getText());
      lg.setType(upPositionbox.getValue());
      if (db.updateEmployee(lg)) {
        DialogHelper.showInformationAlert("Success!",
            "Successfully updated Employee.");
      } else {
        DialogHelper.showErrorAlert("Error.",
            "Unable to  Employee");
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
