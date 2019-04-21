package edu.wpi.cs3733.d19.teamO.controller;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import animatefx.animation.Shake;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.controller.event.ChangeMainViewEvent;
import edu.wpi.cs3733.d19.teamO.entity.Employee;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "LoginWindow.fxml")
public class LoginController implements Controller {

  private static final Logger logger = Logger.getLogger(LoginController.class.getName());

  @FXML
  BorderPane root;
  @FXML
  private JFXButton loginButton;
  @FXML
  private JFXTextField username;
  @FXML
  private JFXPasswordField password;
  @FXML
  private Label loginFail;
  @FXML
  private Label welcomeLabel;
  @FXML
  private Label loginLabel;

  @Inject
  private EventBus eventBus;
  @Inject
  private NavigationController.Factory navigationController;
  @Inject
  private Database db;

  private Set<Employee> info;

  @FXML
  void initialize() {
    info = db.getAllEmployee();

    username.requestFocus();

    loginButton.setDisable(true);
    loginButton.disableProperty().bind(
        Bindings.isNull(username.textProperty())
            .or(Bindings.isNull(password.textProperty()))
    );
  }

  @FXML
  void loginButtonAction() {

    username.requestFocus();
    // gets the user input
    Employee employee = parseUserLogin();
    if (employee == null) {
      logger.log(Level.WARNING,
          "Unable to parse Username and Password.",
          "Unable to parse User Employee info");
      return;
    }
    boolean check = false;
    boolean admin = false;
    // checks every Employee info in set
    for (Employee l : info) {
      if (l.loginEquals(employee)) {
        check = true;
        if (l.getType().toString().equals("Admin")) {
          admin = true;
        }
      }
    }

    username.setText(null);
    password.setText(null);

    // if info typed was right, you go to main window screen
    if (check) {
      loginFail.setText("Login Success");
      if (admin) {
        eventBus.post(new ChangeMainViewEvent(navigationController.create(), true, true));
      } else {
        eventBus.post(new ChangeMainViewEvent(navigationController.create()));
      }
    } else {
      loginFail.setText("Incorrect username or password");
      new Shake(loginButton).play();
    }
  }

  /**
   * Validate the username and password fields and return a Employee object.
   *
   * @return A new login object if valid input, otherwise null.
   */
  private Employee parseUserLogin() {
    // checks if input is valid, parses it and returns a new Employee
    if (!username.getText().isEmpty() && !password.getText().isEmpty()) {
      return new Employee(username.getText(), password.getText(), null);
    }

    // otherwise
    loginFail.setText("Incorrect username or password");
    return null;
  }

  public Label getLoginFail() {
    return loginFail;
  }

  public void setLoginFail(String string) {
    loginFail.setText(string);
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    LoginController create();
  }
}
