package edu.wpi.cs3733.d19.teamO.controller;

import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import edu.wpi.cs3733.d19.teamO.entity.Login;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "AddEmployee.fxml")
@SuppressWarnings("PMD.TooManyFields")
public class AddEmployeeController implements Controller {

  private static final Logger logger =
      Logger.getLogger(AddEmployeeController.class.getName());

  @FXML
  private VBox root;
  @FXML
  private JFXTextField namefield;
  @FXML
  private JFXTextField usernamefield;
  @FXML
  private JFXTextField passwordfield;
  @FXML
  private JFXComboBox<Login.EmployeeType> positionbox;
  @FXML
  private Label titlelbl;
  @FXML
  private JFXButton submitbtn;

  @Inject
  private Database db;

  @FXML
  void initialize() {
    positionbox.getItems().setAll(Login.EmployeeType.values());
  }

  @FXML
  void onSubmitButtonAction() {
    Login external = parseUserEmployee();
    if (external == null) {
      logger.log(Level.WARNING,
          "Unable to parse Employee Data.",
          "Unable to parse Employee Data.");
      return;
    }

    if (db.insertLogin(external)) {
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
  private Login parseUserEmployee() {
    // if input is valid, parse it and return a new Employee
    if (!namefield.getText().isEmpty()
        && !usernamefield.getText().isEmpty()
        && !passwordfield.getText().isEmpty()
        && Objects.nonNull(positionbox.getValue())) {

      String name = namefield.getText();
      String username = usernamefield.getText();
      String password = passwordfield.getText();
      String type = positionbox.getValue().toString().toUpperCase(new Locale("EN"));
      Login.EmployeeType externalEmployeeType = Login.EmployeeType.valueOf(type);
      Login newemp = new Login(username, password, name, externalEmployeeType);

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
