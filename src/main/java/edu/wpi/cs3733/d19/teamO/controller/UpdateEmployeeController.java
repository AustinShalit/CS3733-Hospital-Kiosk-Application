package edu.wpi.cs3733.d19.teamO.controller.v2;

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

import edu.wpi.cs3733.d19.teamO.entity.Login;
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
  private JFXComboBox<Login.EmployeeType> upPositionbox;
  @FXML
  private Label upTitlelbl;
  @FXML
  private JFXButton upSubmitbtn;

  @Inject
  private Database db;

  private final ObjectProperty<Login> login = new SimpleObjectProperty<>();

  @FXML
  void initialize() {
    upPositionbox.getItems().setAll(Login.EmployeeType.values());

    login.addListener((observable, oldValue, newValue) -> {
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
      Login lg = db.getLogin(idInt).get();
      lg.setUsername(upUsernamefield.getText());
      lg.setPassword(upPasswordfield.getText());
      lg.setName(upNamefield.getText());
      lg.setType(upPositionbox.getValue());
      if (db.updateLogin(lg)) {
        DialogHelper.showInformationAlert("Success!",
            "Successfully updated Employee.");
      } else {
        DialogHelper.showErrorAlert("Error.",
            "Unable to  Employee");
      }
    }
  }

  public Login getLogin() {
    return login.get();
  }

  public ObjectProperty<Login> loginProperty() {
    return login;
  }

  public void setLogin(Login login) {
    this.login.set(login);
  }

  @Override
  public Parent getRoot() {
    return upRoot;
  }

  public interface Factory {
    UpdateEmployeeController create();
  }


}
