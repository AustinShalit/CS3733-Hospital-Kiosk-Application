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
  private HomeController.Factory homeControllerFactory;
  @Inject
  private Database db;

  private Set<Employee> info;

  @FXML
  void initialize() {
    info = db.getAllEmployee();
  }

  @FXML
  void loginButtonAction() {
    // gets the user input
    Employee employee = parseUserLogin();
    if (employee == null) {
      logger.log(Level.WARNING,
          "Unable to parse Username and Password.",
          "Unable to parse User Employee info");
      return;
    }
    boolean check = false;
    // checks every Employee info in set
    for (Employee l : info) {
      if (l.loginEquals(employee)) {
        check = true;
      }
    }

    // if info typed was right, you go to main window screen
    if (check) {
      eventBus.post(new ChangeMainViewEvent(homeControllerFactory.create()));
      username.setText(null);
      password.setText(null);
      loginFail.setText("Login Success");
    } else {
      loginFail.setText("Incorrect username or password");
      new Shake(loginFail).play();
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

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    LoginController create();
  }
}
