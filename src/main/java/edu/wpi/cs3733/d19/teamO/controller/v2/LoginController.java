package edu.wpi.cs3733.d19.teamO.controller.v2;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.controller.SanitationWindowController;
import edu.wpi.cs3733.d19.teamO.controller.v2.event.ChangeMainViewEvent;
import edu.wpi.cs3733.d19.teamO.entity.Employee;
import edu.wpi.cs3733.d19.teamO.entity.Login;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "LoginWindow.fxml")
public class LoginController implements Controller {

  @FXML
  private BorderPane root;
  @FXML
  private JFXButton loginButton;
  @FXML
  private JFXTextField username;
  @FXML
  private JFXPasswordField password;
  @FXML
  private Label loginFail;

  @Inject
  private EventBus eventBus;
  @Inject
  private HomeController.Factory homeControllerFactory;
  @Inject
  private Database db;

  @FXML
  void initialize() {
    // Test employees
    Employee adminWong = new Employee(1234, "Admin", Employee.EmployeeType.ADMIN);
    Employee teamO = new Employee(12, "TeamO", Employee.EmployeeType.DEFAULT);
    // Insert employees
    db.insertEmployee(adminWong);
    db.insertEmployee(teamO);
    // Test user login info
    Login user1 = new Login("admin", "wong", adminWong);
    Login user2 = new Login("teamo", "won", teamO);

    db.insertLogin(user1);
    db.insertLogin(user2);
  }

  @FXML
  void loginButtonAction() {
    // gets the user input
    Login login = parseUserLogin();
    if (login == null) {
      Logger logger = Logger.getLogger(SanitationWindowController.class.getName());
      logger.log(Level.WARNING,
          "Unable to parse Username and Password.",
          "Unable to parse User Login info");
      return;
    }

    Set<Login> info = db.getAllLogin();
    boolean check = false;
    // checks every Login info in set
    for (Login l : info) {
      if (l.equals(login)) {
        check = true;
      }
    }

    // if info typed was right, you go to main window screen
    if (check) {
      eventBus.post(new ChangeMainViewEvent(homeControllerFactory.create()));
      //switchScenes("./v2/Main.fxml", loginButton.getScene().getWindow());
    } else {
      loginFail.setText("Incorrect username or password");
      //bounceTextAnimation(loginFail);
    }
  }

  /**
   * Validate the username and password fields and return a Login object.
   *
   * @return A new login object if valid input, otherwise null.
   */
  private Login parseUserLogin() {
    // checks if input is valid, parses it and returns a new Login
    if (!username.getText().isEmpty() && !password.getText().isEmpty()) {
      return new Login(username.getText(), password.getText());
    }

    // otherwise
    loginFail.setText("Incorrect username or password");
    // bounceTextAnimation(loginFail);
    return null;
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    LoginController create();
  }
}
